package cn.nest.spider.dao.commons;

import java.util.LinkedList;
import java.util.List;


import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.elasticsearch.action.DocWriteResponse.Result;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;

import cn.nest.spider.dao.ElasticsearchDAO;
import cn.nest.spider.dao.es.ESClient;
import cn.nest.spider.entity.commons.SpiderInfo;

@Repository("spiderInfoDAO")
public class SpiderInfoDAO extends ElasticsearchDAO<SpiderInfo> {
	
	private static final String INDEX_NAME = "spiderinfo";
	
	private static final String TYPE_NAME = "spiderinfo";
	
	private static final Logger LOG = LogManager.getLogger(SpiderInfoDAO.class);
	
	private static final Gson GSON = new Gson();
	
	@Autowired
	public SpiderInfoDAO(@Qualifier(value = "esClient")ESClient esClient) {
		super(esClient, INDEX_NAME, TYPE_NAME);
	}

	@Override
	protected boolean check() {
		return esClient.checkSpiderInfoIndex() && esClient.checkSpiderInfoType();
	}

	@Override
	public String index(SpiderInfo info) {
		if(findByDomain(info.getDomain(), 1, 10).size() > 0) {
			List<SpiderInfo> mayDuplicate = new LinkedList<>();
			List<SpiderInfo> tmp;
			int page = 1;
			do {
				tmp = findByDomain(info.getDomain(), page++, 10);
				mayDuplicate.addAll(tmp);
			}while(tmp.size()>0);
			if(mayDuplicate.indexOf(info) != -1 && (info = mayDuplicate.get(mayDuplicate.indexOf(info))) != null) {
				LOG.info("已含有此模板，不再存储");
				return info.getId();
			}
		}
		String id = client.prepareIndex(INDEX_NAME, TYPE_NAME)
				.setSource(GSON.toJson(info), XContentType.JSON).get().getId();
		info.setId(id);
		return client.prepareUpdate(INDEX_NAME, TYPE_NAME, id)
				.setDoc(GSON.toJson(info), XContentType.JSON).get().getId();
	}
	
	public List<SpiderInfo> listAll(int page, int size) {
		SearchResponse response = searchAll().setSize(size).setFrom(size*(page-1)).get();
		return hitsToList(response.getHits());
	}
	
	public SpiderInfo findById(String id) {
		GetResponse response = client.prepareGet(INDEX_NAME, TYPE_NAME, id).get();
		Preconditions.checkArgument(response.isExists(), "爬虫模板不存在， 模板ID:" + id);
		return hitToInfo(response.getSourceAsString(), id);
	}
	
	public List<SpiderInfo> findByDomain(String domain, int page, int size) {
		SearchResponse response = search().setSize(size).setFrom(size*(page-1))
				.setQuery(QueryBuilders.matchQuery("domain", domain).operator(Operator.AND)).get();
		return hitsToList(response.getHits());
	}
	
	public boolean delById(String id) {
		DeleteResponse response = client.prepareDelete(INDEX_NAME, TYPE_NAME, id).get();
		return response.getResult() == Result.DELETED;
	}
	
	public boolean delByDomain(String domain) {
		return delByQuery(QueryBuilders.matchQuery("domain", domain), null);
	}
	
	public String update(SpiderInfo info) {
		String id = info.getId();
		Preconditions.checkArgument(StringUtils.isNotBlank(info.getId()), "爬虫模板不可为空");
		UpdateResponse response = client.prepareUpdate(INDEX_NAME, TYPE_NAME, info.getId())
				   .setDoc(GSON.toJson(info), XContentType.JSON).get();
		String res = response.getId();
		if(StringUtils.isBlank(res)) {
			LOG.error("不存在ID为" + id +"的爬虫模板");
			throw new RuntimeException("更新失败， 因为该模板不存在");
		} else
			return res;
	}
	
	private SearchRequestBuilder searchAll() {
		return client.prepareSearch(INDEX_NAME).setTypes(TYPE_NAME).setQuery(QueryBuilders.matchAllQuery());
	}
	
	private SpiderInfo hitToInfo(String jsonSrc, String id) {
		SpiderInfo info = GSON.fromJson(jsonSrc, SpiderInfo.class);
		info.setId(id);
		return info;
	}
	
	private SpiderInfo hitToInfo(SearchHit hit) {
		SpiderInfo info = GSON.fromJson(hit.getSourceAsString(), SpiderInfo.class);
		info.setId(hit.getId());
		return info;
	}
	
	private List<SpiderInfo> hitsToList(SearchHits hits) {
		List<SpiderInfo> list = new LinkedList<>();
		hits.forEach(hit -> list.add(hitToInfo(hit)));
		return list;
	}

}
