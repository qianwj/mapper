package cn.nest.spider.service;

import java.io.OutputStream;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.tuple.Pair;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.springframework.stereotype.Service;


import cn.nest.spider.dao.commons.WebpageDAO;
import cn.nest.spider.entity.commons.Webpage;
import cn.nest.spider.gather.commons.CommonSpider;
import cn.nest.spider.util.ResultBundle;
import cn.nest.spider.util.ResultBundleBuilder;
import cn.nest.spider.util.ResultList;

@Service
public class WebpageService {

	
	@Resource(name = "webpageDAO")
	private WebpageDAO dao;
	
	@Resource(name = "resultBuilder")
	private ResultBundleBuilder builder;
	
	@Resource(name = "spider")
	private CommonSpider spider;
	
	
	public ResultList<Webpage> findBySpiderUUID(String uuid, int page, int size) {
		return builder.bundleList(uuid, () -> dao.findBySpiderUUID(uuid, page, size));
	}
	
	public ResultList<Webpage> findByDomain(String domain, int page, int size) {
		return builder.bundleList(domain, () -> dao.findByDomain(domain, size, page));
	}
	
	public ResultList<Webpage> findByDomains(Collection<String> domains, int page, int size) {
		return builder.bundleList(domains.toString(), () -> dao.findByDomains(domains, size, page));
	}
	
	public ResultList<Webpage> searchByQuery(String query, int page, int size) {
		return builder.bundleList(query, () -> dao.findByQuery(query, page, size));
	}
	
	public ResultBundle<Pair<List<Webpage>, Long>> listAll(int page, int size) {
		return builder.bundle(null, () -> dao.listAll(page, size));
	}
	
	public ResultBundle<Webpage> findWebpage(String id) {
		return builder.bundle(id, () -> dao.getWebpageById(id));
	}
	
	public ResultBundle<Pair<List<Webpage>, Long>> findByKeyWordAndDomain(String query, String domain, int page, int size) {
		return builder.bundle("query:" + query + ", domain:" + domain, () -> dao.findByKeywordAndDomain(query, domain, page, size));
	}
	
	public ResultBundle<Boolean> delWebpage(String id) {
		return builder.bundle(id, () -> dao.delById(id));
	}
	
	public ResultBundle<String> delWebpageByDomain(String domain) {
		return builder.bundle(domain, () -> spider.delByDomain(domain));
	}
	
	public ResultBundle<Map<String, Long>> countDomain(int size) {
		return builder.bundle(null, () -> dao.countDomain(size));
	}
	
	public ResultBundle<Map<String, Long>> countWordByDomain(String domain) {
		return builder.bundle(null, () -> dao.countWordByDomain(domain));
	}
	
	public ResultBundle<Map<Date, Long>> countDomainByGatherTime(String domain) {
		return builder.bundle(domain, () -> dao.countDomainByGatherTime(domain));
	}
	
	public ResultBundle<List<Webpage>> moreLikeThis(String id, int page, int size) {
		return builder.bundle(id, () -> dao.moreLikeThis(id, page, size));
	}
	
	public ResultBundle<Pair<String, List<Webpage>>> startScroll() {
		return builder.bundle(null, () -> dao.startScroll());
	}
	
	public ResultList<Webpage> scrollAll(String scrollId) {
		return builder.bundleList(scrollId, () -> dao.ScrollAllWebpage(scrollId));
	}

	
	public ResultBundle<Pair<Map<String, List<Bucket>>, List<Webpage>>> relatedInfo(String query, int size) {
		return builder.bundle(null, () -> dao.relatedInfo(query, size));
	}
	
	public void exportTitleContentPairBySpiderUUID(String uuid, OutputStream out) {
		dao.exportTitleContentPairBySpiderUUID(uuid, out);
	}
	
	public void exportWebpageJsonBySpiderUUID(String uuid, boolean includeRaw, OutputStream out) {
		dao.exportWebpageJsonBySpiderUUID(uuid, includeRaw, out);
	}
	
	public void exportWebpageJSONByDomain(String domain, Boolean includeRaw, OutputStream outputStream) {
        dao.exportWebpageJsonByDomain(domain, includeRaw, outputStream);
    }
}
