package cn.nest.spider.dao;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import com.google.common.base.Preconditions;

import cn.nest.spider.dao.es.ESClient;
import cn.nest.spider.entity.async.Task;

/**
 * ElasticSearch数据接口
 * @author web
 *
 */
public abstract class ElasticsearchDAO<T> {

	private static final TimeValue SCROLL_TIMEOUT = TimeValue.timeValueMinutes(5);
	
	private static final Logger LOG = LogManager.getLogger(ElasticsearchDAO.class);
	
	private String indexName;
	
	private String typeName;
	
	protected Client client;
	
	protected Queue<T> queue = new ConcurrentLinkedDeque<>();
	
	protected ESClient esClient;
	
	public ElasticsearchDAO() {}
	
	public ElasticsearchDAO(ESClient esClient, String indexName, String typeName) {
		this.esClient = esClient;
		this.indexName = indexName;
		this.typeName = typeName;
		initClient(esClient);
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				while(queue.size() > 0)
					index(queue.remove());
			}
		}, 3000, 1000);
	}
	
	protected abstract boolean check();
	
	protected abstract String index(T t);
	
	private void initClient(ESClient esclient) {
		if(client != null) 
			LOG.info("ES客户端已初始化");
		this.client = esclient.getClient();
		LOG.debug("检查index, type是否存在");
	}
	
	protected boolean delByQuery(QueryBuilder builder, Task task) {
		BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();
		SearchRequestBuilder searchRequestBuilder = client.prepareSearch(indexName, typeName)
				.setQuery(builder).setSize(100)
				.setScroll(SCROLL_TIMEOUT);
		SearchResponse response = searchRequestBuilder.get();
		String query = builder.toString();
		int num = bulkRequestBuilder.numberOfActions();
		while(true) {
			for(SearchHit hit:response.getHits()) {
				bulkRequestBuilder.add(new DeleteRequest(indexName, typeName, hit.getId()));
				if(task!=null)
					task.increaseCount();
			}
			response = client.prepareSearchScroll(response.getScrollId())
					.setScroll(SCROLL_TIMEOUT).get();
			if(response.getHits().getHits().length == 0) {
				if(task!=null)
					task.setDescription("按照query：%s 加载数据，已经添加%s条， 准备执行删除...", query, num);
				LOG.debug("准备执行删除， query:" + builder.toString());
				break;
			} else {
				if(task!=null)
					task.setDescription("按query:%s删除数据， 已添加%s条", query, num);
				LOG.debug("删除数据已添加" + num + "条， query:" + query);
			}
		}
		if(num <= 0) {
			if(task!=null)
				task.setDescription("执行删除时未找到数据， query:", query);
			LOG.debug("执行删除时未找到数据，请检查参数，query:" + query);
			return false;
		}
		BulkResponse bulkResponse = bulkRequestBuilder.get();
		if(bulkResponse.hasFailures()) {
			if(task!=null)
				task.setDescription("按query：%s删除部分数据失败，由于%s", query, bulkResponse.buildFailureMessage());
			LOG.error("删除部分数据失败, query:" + query + ", 由于 " + bulkResponse.buildFailureMessage());
		} else {
			if(task!=null)
				task.setDescription("删除数据成功， query:%s, 耗时:%sms", query, bulkResponse.getTookInMillis());
			LOG.error("删除数据成功， query:" + query + ", 耗时:" + bulkResponse.getTookInMillis() + "ms");
		}
		return bulkResponse.hasFailures();
	} 
	
	/**
	 * 获取库中符合条件的数据量
	 * @param builder
	 * @return
	 */
	protected long getCountBy(QueryBuilder builder) {
		return search().setQuery(builder).get().getHits().getTotalHits();
	}
	
	/**
	 * 导出数据
	 * @param builder
	 * @param labelsSupplier
	 * @param contentSupplier
	 * @param out  经过分词器的输出流
	 */
	protected void exportData(QueryBuilder builder, Function<SearchResponse, List<List<String>>> labelsSupplier, 
			Function<SearchResponse, List<String>> contentSupplier, OutputStream out) {
		final int size = 50;
		String scrollId = null;
		int page = 1;
		while(true) {
			LOG.debug("正在输出第" + page + "页，query:" + builder);
			SearchResponse response;
			if(StringUtils.isBlank(scrollId)) {
				response = search().setQuery(builder).setSize(size)
						.setScroll(SCROLL_TIMEOUT).get();
				scrollId = response.getScrollId();
			} else {
				response = client.prepareSearchScroll(scrollId)
						.setScroll(SCROLL_TIMEOUT).get();
			}
			final List<List<String>> labels = labelsSupplier.apply(response);
			final List<String> contentList = contentSupplier.apply(response);
			Preconditions.checkNotNull(labels);
			Preconditions.checkNotNull(contentList);
			if(contentList.size()<=0)
				break;
			//开始分词
			List<String> combine;
			if(labels.size() > 0) {
				combine = labels.stream().map(list -> list.parallelStream().collect(Collectors.joining("/")))
						.collect(Collectors.toList());
				for(int i = 0;i<labels.size();i++) 
					combine.set(i, combine.get(i) + " " + contentList.get(i));
			} else
				combine = contentList;
			page++;
			try {
				IOUtils.write(combine.stream().collect(Collectors.joining("\n")) + "\n", out, "utf-8");
				out.flush();
			} catch(IOException e) {
				LOG.error("文件写出错误， 由于 " + e.getLocalizedMessage());
			}
		}
	}
	
	public long getTotal() {
		return search().setQuery(QueryBuilders.matchAllQuery()) //SearchRequestBuilder
				.get()                             //SearchResponse
				.getHits().getTotalHits();         
	}
	
	protected SearchRequestBuilder search() {
		return client.prepareSearch(indexName).setTypes(typeName);
	}
}
