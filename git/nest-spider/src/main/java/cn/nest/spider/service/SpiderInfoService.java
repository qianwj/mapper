package cn.nest.spider.service;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import cn.nest.spider.dao.commons.SpiderInfoDAO;
import cn.nest.spider.entity.commons.SpiderInfo;
import cn.nest.spider.gather.commons.CommonSpider;
import cn.nest.spider.util.ResultBundle;
import cn.nest.spider.util.ResultBundleBuilder;
import cn.nest.spider.util.ResultList;

@Service
public class SpiderInfoService {

	@Resource(name = "spiderInfoDAO")
	private SpiderInfoDAO dao;
	
	@Resource(name = "spider")
	private CommonSpider spider;
	
	@Autowired
	private ResultBundleBuilder builder;
	
	private Gson gson = new Gson();
	
	/**
     * 列出库中所有爬虫模板
     *
     * @param size 页面容量
     * @param page 页码
     * @return
     */
    public ResultList<SpiderInfo> listAll(int page, int size) {
        return builder.bundleList(null, () -> dao.listAll(page, size));
    }

    /**
     * 根据domain获取结果
     *
     * @param domain 网站域名
     * @param size   每页数量
     * @param page   页码
     * @return
     */
    public ResultList<SpiderInfo> findByDomain(String domain, int page, int size) {
        return builder.bundleList(domain, () -> dao.findByDomain(domain, page, size));
    }

    /**
     * 索引爬虫模板
     *
     * @param spiderInfo 爬虫模板
     * @return 如果爬虫模板索引成功则返回模板id, 否则返回error;
     */
    public ResultBundle<String> index(SpiderInfo info) {
    	return builder.bundle(info.getDomain(), () -> StringUtils.isBlank(info.getId())?dao.index(info):dao.update(info));
    }

    /**
     * 根据网站domain删除数据
     *
     * @param domain 网站域名
     * @return 是否全部数据删除成功
     */
    public ResultBundle<Boolean> delByDomain(String domain) {
        return builder.bundle(domain, () -> dao.delByDomain(domain));
    }

    /**
     * 根据id删除网页模板
     *
     * @param id 网页模板id
     * @return 是否删除
     */
    public ResultBundle<Boolean> delById(String id) {
        return builder.bundle(id, () -> dao.delById(id));
    }

    /**
     * 根据爬虫模板id获取指定爬虫模板
     *
     * @param id 爬虫模板id
     * @return
     */
    public ResultBundle<SpiderInfo> getById(String id) {
        return builder.bundle(id, () -> dao.findById(id));
    }

    /**
     * 更新爬虫模板
     *
     * @param spiderInfo 爬虫模板实体
     * @return 爬虫模板id
     */
    public ResultBundle<String> update(SpiderInfo spiderInfo) {
        return builder.bundle(spiderInfo.getId(), () -> dao.update(spiderInfo));
    }
	
    public ResultBundle<String> update(String spiderInfoId, String spiderInfoJson, List<String> callbackUrls) {
		SpiderInfo info = gson.fromJson(spiderInfoJson, SpiderInfo.class);
		return builder.bundle(spiderInfoId, () -> spider.updateBySpiderInfoId(spiderInfoId, info, callbackUrls));
	}
}
