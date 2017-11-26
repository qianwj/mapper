package cn.nest.spider.dao.commons;

import java.io.OutputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.elasticsearch.action.DocWriteResponse.Result;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MoreLikeThisQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.sort.SortOrder;
import org.joda.time.DateTime;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

import cn.nest.spider.dao.ElasticsearchDAO;
import cn.nest.spider.dao.es.ESClient;
import cn.nest.spider.entity.async.Task;
import cn.nest.spider.entity.commons.Webpage;

@Repository("webpageDAO")
public class WebpageDAO extends ElasticsearchDAO<Webpage> {
	
	private static final String INDEX_NAME = "commons";
	
	private static final String TYPE_NAME = "webpage";
	
	private static final TimeValue SCROLL_TIMEOUT = TimeValue.timeValueMinutes(1);
	
	private static final Gson GSON = new GsonBuilder()
			.registerTypeAdapter(Date.class, (JsonDeserializer<Date>)(json, typeOfT, context) -> new Date(json.getAsJsonPrimitive().getAsLong()))
			.registerTypeAdapter(Date.class, (JsonSerializer<Date>)(src, typeOfSrc, context) -> new JsonPrimitive(src.getTime()))
			.setDateFormat(DateFormat.LONG).create();
	
	private static final Logger LOG = LogManager.getLogger(WebpageDAO.class);
	
	public WebpageDAO() {}
	
    @Autowired
	public WebpageDAO(ESClient esClient) {
		super(esClient, INDEX_NAME, TYPE_NAME);
	}

	@Override
	public boolean check() {
		return esClient.checkCommonIndex() && esClient.checkWebpageType();
	}

	@Override
	public String index(Webpage page) {
		IndexResponse response = null;
		try {
			response = client.prepareIndex(INDEX_NAME, TYPE_NAME)
					.setSource(GSON.toJson(page), XContentType.JSON).get();
			return response.getId();
		} catch(Exception e) {
			LOG.error("索引 webpage 出错， 由于 " + e.getLocalizedMessage());
		}
		return null;
	}

	public List<Webpage> findBySpiderUUID(String uuid, int page, int size) {
		SearchResponse response = search().setSize(size).setFrom(size*(page-1))
				.setQuery(QueryBuilders.matchQuery("spiderUUID", uuid)).get();
		return hitsToList(response.getHits());
	}
	
	public List<Webpage> findByDomain(String domain, int size, int page) {
		SearchResponse response = null;
		try {
			response = search().setSize(size).setFrom(size*(page-1)).addSort("gatherTime", SortOrder.DESC)
					.setQuery(QueryBuilders.matchQuery("domain", domain)).get();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(response == null)
				return null;
		}
		 
		return hitsToList(response.getHits());
	}
	
	public List<Webpage> findByDomains(Collection<String> domains, int size, int page) {
		if(domains.size() == 0)
			return new ArrayList<>();
		BoolQueryBuilder builder = QueryBuilders.boolQuery();
		domains.forEach(domain -> builder.should(QueryBuilders.matchQuery("domain", domain)));
		SearchResponse response = search().setSize(size).setFrom(size*(page-1)).addSort("gatherTime", SortOrder.DESC)
				.setQuery(builder).get();
		return hitsToList(response.getHits());
	}
	
	/**
	 * 搜索文章
	 * @param query
	 * @param page
	 * @param size
	 * @return
	 */
	public List<Webpage> findByQuery(String query, int page, int size) {
		SearchResponse response = search().setSize(size).setFrom(size*(page-1))
				.setQuery(QueryBuilders.queryStringQuery(query).analyzer("query_ansj").defaultField("content")).get();
		return hitsToList(response.getHits());
	}
	
	/**
	 * 根据关键词和域名查找网页
	 * @param query
	 * @param domain
	 * @param page
	 * @param size
	 * @return
	 */
	public Pair<List<Webpage>, Long> findByKeywordAndDomain(String query, String domain, int page, int size) {
		QueryBuilder keywordQuery, domainQuery;
		if(StringUtils.isBlank(query))
			query = "*";
		keywordQuery = QueryBuilders.queryStringQuery(query).analyzer("query_ansj").defaultField("content");
		if(StringUtils.isBlank(domain))
			domain = "*";
		else
			domain = "*" + domain + "*";
		domainQuery = QueryBuilders.queryStringQuery(query).defaultField("domain");
		SearchHits hits = search().setSize(size).setFrom(size*(page-1)).setQuery(keywordQuery)
				.setPostFilter(domainQuery).get().getHits();
		return Pair.of(hitsToList(hits), hits.getTotalHits());
	}
	
	public Webpage getWebpageById(String id) {
		GetResponse response = client.prepareGet(INDEX_NAME, TYPE_NAME, id).get();
		Preconditions.checkArgument(response.isExists(), "无法找到ID为%s的文章", id);
		return hitToInfo(response.getSourceAsString(), id);
	}
	
	public Pair<List<Webpage>, Long> listAll(int page, int size) {
		SearchHits hits = searchAll().setSize(size).setFrom(size*(page-1))
				.addSort("gatherTime", SortOrder.DESC).get().getHits();
		return Pair.of(hitsToList(hits), hits.getTotalHits());
	}
	
	/**
	 * 更新网页
	 * @param page
	 * @return
	 */
	public boolean update(Webpage page) {
		UpdateResponse response = client.prepareUpdate(INDEX_NAME, TYPE_NAME, page.getId())
				.setDoc(GSON.toJson(page), XContentType.JSON).get();
		return response.getResult() == Result.UPDATED;
	}
	
	public boolean update(List<Webpage> pageList) {
		BulkRequestBuilder builder = client.prepareBulk();
		pageList.stream().forEach(page -> {
			builder.add(new UpdateRequest().index(INDEX_NAME).type(TYPE_NAME).id(page.getId())
					.doc(GSON.toJson(page), XContentType.JSON));
		});
		return builder.get().hasFailures();
	}
	
	public boolean delById(String id) {
		DeleteResponse response = client.prepareDelete(INDEX_NAME, TYPE_NAME, id).get();
		return response.getResult() == Result.DELETED;
	}
	
	public boolean delByDomain(String domain, Task task) {
		return delByQuery(QueryBuilders.matchQuery("domain", domain), task);
	}
	
	public void exportTitleContentPair(QueryBuilder builder, OutputStream out) {
		exportData(builder, response -> {
			List<List<String>> list = new LinkedList<>();
			hitsToList(response.getHits()).forEach(page -> list.add(Lists.newArrayList(page.getTitle())));
			return list;
		}, response -> {
			List<String> list = new LinkedList<>();
			hitsToList(response.getHits()).forEach(page -> list.add(page.getContent()));
			return list;
		}, out);
	}
	
	public void exportWebpageJson(QueryBuilder builder, boolean includeRaw, OutputStream out) {
		exportData(builder, response -> new LinkedList<>(), response -> {
			List<String> list = new LinkedList<>();
			hitsToList(response.getHits())
			  .forEach(page -> list.add(GSON.toJson(includeRaw? page:page.setRawHTML(null))));
			return list;
		}, out);
	}
	
	public void exportTitleContentPairBySpiderUUID(String uuid, OutputStream out) {
		exportTitleContentPair(QueryBuilders.matchQuery("spiderUUID", uuid), out);
	}
	
	public void exportWebpageJsonBySpiderUUID(String uuid, boolean includeRaw, OutputStream out) {
		exportWebpageJson(QueryBuilders.matchQuery("spiderUUID", uuid).operator(Operator.AND), includeRaw, out);
	}
	
	public void exportWebpageJsonByDomain(String domain, boolean includeRaw, OutputStream out) {
		exportWebpageJson(QueryBuilders.matchQuery("domain", domain).operator(Operator.AND), includeRaw, out);
	}
	
	public Pair<String, List<Webpage>> startScroll(QueryBuilder builder, int size) {
		SearchResponse response = search().setSize(size).setQuery(builder)
				.setScroll(SCROLL_TIMEOUT).get();
		return new MutablePair<>(response.getScrollId(), hitsToList(response.getHits()));
	}
	
	public Pair<String, List<Webpage>> startScroll() {
		return startScroll(QueryBuilders.matchAllQuery(), 50);
	}
	
	public List<Webpage> ScrollAllWebpage(String scrollId) {
		Preconditions.checkArgument(StringUtils.isNotBlank(scrollId), "scrollId不能为空");
		return hitsToList(client.prepareSearch(scrollId).setScroll(SCROLL_TIMEOUT)
				.setQuery(QueryBuilders.matchAllQuery()).get().getHits());
	}
	
	/**
	 * 统计每个网站的文章数
	 * @param size
	 * @return Map<域名， 文章数>
	 */
	public Map<String, Long> countDomain(int size) {
		SearchResponse response = searchAll()
				.addAggregation(AggregationBuilders.terms("domain").field("domain").size(size)
						.order(Terms.Order.count(false))).get();
		List<Bucket> list =  (List<Bucket>)((Terms) response.getAggregations().get("domain")).getBuckets();
		Map<String, Long> count = new LinkedHashMap<>();
		list.stream().filter(bucket -> bucket.getKeyAsString().length()>1).forEach(bucket -> {
			count.put(bucket.getKeyAsString(), bucket.getDocCount());
		});
		return count;
	}
	
	/**
	 * 统计网站中的词频
	 * @param domain
	 * @return Map<词，词频>
	 */
	public Map<String, Long> countWordByDomain(String domain) {
		SearchResponse response = search().setQuery(QueryBuilders.matchQuery("domain", domain))
				.addAggregation(AggregationBuilders.terms("content").field("content").size(200)).get();
		List<Bucket> list = (List<Bucket>)((Terms)response.getAggregations().get("content")).getBuckets();
		Map<String, Long> count = new LinkedHashMap<>();
		list.stream().filter(bucket -> bucket.getKeyAsString().length() > 1).forEach(bucket -> {
			count.put(bucket.getKeyAsString(), bucket.getDocCount());
		});
		return count;
	}
	
	/**
	 * 统计指定网站每天抓取数量
	 * @param domain
	 * @return
	 */
	public Map<Date, Long> countDomainByGatherTime(String domain) {
		SearchResponse response = search().setQuery(QueryBuilders.matchQuery("domain", domain))
				.addAggregation(AggregationBuilders.dateHistogram("agg").field("gatherTime")
						.dateHistogramInterval(DateHistogramInterval.DAY).order(Histogram.Order.KEY_DESC)).get();
		Map<Date, Long> count = new LinkedHashMap<>();
		for(Histogram.Bucket bucket:((Histogram)(Histogram)response.getAggregations().get("agg")).getBuckets()) 
			count.put(((DateTime)bucket.getKey()).toDate(), bucket.getDocCount());
		return count;
	}
	
	public Pair<Map<String, List<Bucket>>, List<Webpage>> relatedInfo(String query, int size) {
		SearchResponse response = search().setQuery(QueryBuilders.queryStringQuery(query))
				.addAggregation(AggregationBuilders.terms("People").field("namedEntity.nr"))
				.addAggregation(AggregationBuilders.terms("Location").field("namedEntity.ns"))
				.addAggregation(AggregationBuilders.terms("Institution").field("namedEntity.nt"))
				.addAggregation(AggregationBuilders.terms("Keyword").field("keyword"))
				.setSize(size).get();
		Map<String, List<Bucket>> map = new HashMap<>();
		map.put("People", response.getAggregations().get("People"));
		map.put("Location", response.getAggregations().get("Location"));
		map.put("Institution", response.getAggregations().get("Institution"));
		map.put("Keyword", response.getAggregations().get("Keyword"));
		return Pair.of(map, hitsToList(response.getHits()));
	}
	
	/**
	 * 根据网页ID获取相似页面
	 * @param id
	 * @param page
	 * @param size
	 * @return
	 */
	public List<Webpage> moreLikeThis(String id, int page, int size) {
		MoreLikeThisQueryBuilder.Item[] items = {new MoreLikeThisQueryBuilder.Item(INDEX_NAME, TYPE_NAME, id)};
		String[] fields = {"content"};
		SearchResponse response = search().setSize(size).setFrom(size*(page-1))
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
				.setQuery(QueryBuilders.moreLikeThisQuery(fields, null, items)
						.minTermFreq(1).maxQueryTerms(12)).get();
		return hitsToList(response.getHits());
	}
	
    private SearchRequestBuilder searchAll() {
    	return search().setQuery(QueryBuilders.matchAllQuery());
    }
	
	private List<Webpage> hitsToList(SearchHits hits) {
		List<Webpage> list = new LinkedList<>();
		hits.forEach(hit -> list.add(hitToInfo(hit)));
		return list;
	}
	
	private Webpage hitToInfo(SearchHit hit) {
	    return GSON.fromJson(hit.getSourceAsString(), Webpage.class).setId(hit.getId());
	}
	
	private Webpage hitToInfo(String src, String id) {
		return GSON.fromJson(src, Webpage.class).setId(id);
	}
}
