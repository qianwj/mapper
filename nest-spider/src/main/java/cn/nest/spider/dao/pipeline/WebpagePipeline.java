package cn.nest.spider.dao.pipeline;

import java.nio.charset.Charset;
import java.text.DateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.common.util.set.Sets;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

import cn.nest.spider.dao.ElasticsearchDAO;
import cn.nest.spider.dao.es.ESClient;
import cn.nest.spider.entity.commons.SpiderInfo;
import cn.nest.spider.entity.commons.Webpage;

import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.scheduler.component.DuplicateRemover;

@Component
public class WebpagePipeline extends ElasticsearchDAO<Webpage> implements Pipeline, DuplicateRemover {
	
	private static final String INDEX_NAME = "commons";
	
	private static final String TYPE_NAME = "webpage";
	
	private static final String DYNAMIC_FIELD = "dynamic_field";
	
	private static final Gson GSON = new GsonBuilder()
			.registerTypeAdapter(Date.class, (JsonDeserializer<Date>)(json, typeOfT, context) -> new Date(json.getAsJsonPrimitive().getAsLong()))
			.registerTypeAdapter(Date.class, (JsonSerializer<Date>)(src, typeOfSrc, context) -> new JsonPrimitive(src.getTime()))
			.setDateFormat(DateFormat.LONG).create();
	
	private static final Logger LOG = LogManager.getLogger(WebpagePipeline.class);
	
	private static int count = 0;
	
	private Map<String, Set<String>> urls = new ConcurrentHashMap<>();
	
	@Autowired
	public WebpagePipeline(ESClient esClient) {
		super(esClient, INDEX_NAME, TYPE_NAME);
	}
	
	/**
	 * 将Webmagic中的ResultItems转换为Webpage
	 * @param items
	 * @return
	 */
	public static Webpage convertToWebpage(ResultItems items) {
		if(items.toString().indexOf("ResultItems{fields={}") > 0) {
			return null;
		}
		Webpage page = new Webpage().setContent(items.get("content")).setTitle(items.get("title"))
				.setUrl(items.get("url")).setDomain(items.get("domain")).setSpiderInfoId(items.get("spiderInfoId"))
				.setGathertime(items.get("gathertime")).setSpiderUUID(items.get("spiderUUID"))
				.setKeywords(items.get("keywords")).setSummary(items.get("summary"))
				.setNamedEntity(items.get("namedEntity")).setPublishTime(items.get("publishTime"))
				.setCategory(items.get("category")).setRawHTML(items.get("rawHTML"))
				.setDynamicFields(items.get(DYNAMIC_FIELD)).setStaticFields(items.get("staticField"))
				.setAttachmentList(items.get("attachmentList")).setImageList(items.get("imageList"))
				.setProcessTime(items.get("processTime"));
		return 	page.setId(Hashing.sha256().hashString(page.getUrl(), Charset.forName("utf-8")).toString());
	}

	@Override
	public void process(ResultItems resultItems, Task task) {
		SpiderInfo info = resultItems.get("spiderInfo");
		Webpage page = convertToWebpage(resultItems);
		/*
		 * guava22.0不再对MD5()提供支持，
		 * 如果想更安全，使用sha256(), 
		 * 如果想更快，使用goodFastHash()
		 * */
		try {
			client.prepareIndex(INDEX_NAME, TYPE_NAME)
			   .setId(Hashing.sha256().hashString(page.getUrl(), Charset.forName("utf-8")).toString())
			   .setSource(GSON.toJson(page), XContentType.JSON)
			   .get();
		} catch(Exception e) {
			LOG.error("索引Webpage出错， 由于 " + e.getLocalizedMessage());
		}
	}

	@Override
	protected boolean check() {
		return esClient.checkCommonIndex() && esClient.checkWebpageType();
	}

	@Override
	protected String index(Webpage page) {
		return null;
	}

	@Override
	public boolean isDuplicate(Request request, Task task) {
		Set<String> tempList = urls.computeIfAbsent(task.getUUID(), t -> Sets.newConcurrentHashSet());
        //初始化已缓存网站列表
		if(tempList.add(request.getUrl())) {//如果网站未抓取，则进一步检查ES
			GetResponse response = client.prepareGet(INDEX_NAME, TYPE_NAME, Hashing.sha256().hashString(
					request.getUrl(), Charset.forName("utf-8")).toString()
			).get();
			return response.isExists();
		} else 
			return true;
	}

	@Override
	public void resetDuplicateCheck(Task task) {
		
	}

	@Override
	public int getTotalRequestsCount(Task task) {
		return count++;
	}

	public void deleteUrls(String taskId) {
		urls.remove(taskId);
		LOG.info("任务已停止， 准备清除url， 任务ID:" + taskId);
	}
}
