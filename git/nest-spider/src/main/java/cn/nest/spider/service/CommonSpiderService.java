package cn.nest.spider.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DateBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cn.nest.spider.dao.commons.SpiderInfoDAO;
import cn.nest.spider.entity.commons.SpiderInfo;
import cn.nest.spider.entity.commons.Webpage;
import cn.nest.spider.gather.async.quartz.QuartzManager;
import cn.nest.spider.gather.async.quartz.SpiderJob;
import cn.nest.spider.gather.commons.CommonSpider;
import cn.nest.spider.util.ResultBundle;
import cn.nest.spider.util.ResultBundleBuilder;
import cn.nest.spider.util.ResultList;
import cn.nest.spider.util.StaticValue;
import cn.nest.spider.util.Supplier;

/**
 * 
 * @author web
 * 2017/10/10
 */

@Service
public class CommonSpiderService {
	
	private static final Logger LOG = LogManager.getLogger(CommonSpiderService.class);

	private final String QUARTZ_JOB_GROUP_NAME = "webpage-spider-job";
	
	private final String QUARTZ_TRIGGER_GROUP_NAME = "webpage-spider-trigger";
	
	private final String QUARTZ_TRIGGER_SUFFIX = "-hours";
	
	@Resource(name = "spider")
	private CommonSpider spider;
	
	@Autowired
	private ResultBundleBuilder builder;
	
	@Autowired
	private QuartzManager manager;
	
	@Resource(name = "spiderInfoDAO")
	private SpiderInfoDAO dao;
	
	@Autowired
	private StaticValue sValue;
	
	private Gson gson = new Gson();
	
	private Supplier<String> ok = () -> "OK";
	
	public ResultBundle<String> start(SpiderInfo info) {
		//如果id为空就直接存储
		if(sValue.isNeedES())
			if(StringUtils.isBlank(info.getId())) {
				validateSpiderInfo(info);
				info.setId(dao.index(info));
			} else {
				dao.update(info);
			}
		return builder.bundle(info.getId(), () -> spider.start(info));
	}
	
	public ResultBundle<String> start(String spiderInfoJson) {
		Preconditions.checkArgument(StringUtils.isNotBlank(spiderInfoJson), "爬虫模板为空");
		SpiderInfo info = gson.fromJson(spiderInfoJson, SpiderInfo.class);
		return start(info);
	}
	
	public ResultList<String> startAll(List<String> infoIdList) {
		return builder.bundleList(infoIdList.toString(), () -> {
			List<String> taskIdList = new ArrayList<>();
			for(String id:infoIdList) 
				try {
					taskIdList.add(spider.start(dao.findById(id)));
				} catch(Exception e) {
					LOG.error("任务出错， ID:" + id + ", 由于：" + e.getLocalizedMessage());
				}
			return taskIdList;
		});
	}
	
	public ResultBundle<String> stop(String uuid) {
		spider.stop(uuid);
		return builder.bundle(uuid, ok);
	}
	
	public ResultBundle<String> delete(String uuid) {
		spider.delete(uuid);
		return builder.bundle(uuid, ok);
	}
	
	public ResultBundle<String> delAll() {
		spider.delAll();
		return builder.bundle(null, ok);
	}
	
	public ResultBundle<Map<Object, Object>> runtimeInfo(String uuid, boolean extra) {
		return builder.bundle(uuid, () -> spider.getSpiderRuntimeInfo(uuid, extra));
	}
	
	public ResultBundle<Map<String, Map<Object, Object>>> listAllSpiders(boolean extra) {
		return builder.bundle(null, () -> spider.listAllSpiders(extra));
	}
	
	public ResultList<Webpage> testSpiderInfo(String spiderInfoJson) {
		SpiderInfo info = gson.fromJson(spiderInfoJson, SpiderInfo.class);
		try {
			validateSpiderInfo(info);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
		return builder.bundleList(spiderInfoJson, () -> spider.testSpiderInfo(info));
	}
	
	public ResultBundle<String> addIgnoredUrl(String postfix) {
		spider.addIgnoredUrl(postfix);
		return builder.bundle(postfix, ok);
	}
	
	public ResultList<String> getIgnoredUrls() {
		return builder.bundleList(null, () -> spider.getIgnoredUrls());
	}
	
	/**
	 * 创建定时任务， 指定任务经过多长时间运行一次
	 * @param info
	 * @param hours  时间间隔
	 * @return
	 */
	public ResultBundle<String> createQuartzJob(String infoId, int hours) {
		SpiderInfo info = dao.findById(infoId);
		Map<String, Object> data = new HashMap<>();
		data.put("spiderInfo", spider);
		data.put("commonWebpageService", this);
		manager.addJob(info.getId(), QUARTZ_JOB_GROUP_NAME, String.valueOf(hours) + "-" + info.getId() + QUARTZ_TRIGGER_SUFFIX, QUARTZ_TRIGGER_GROUP_NAME, SpiderJob.class, data, hours);
		return builder.bundle(infoId, () -> "OK");
	}
	
	public ResultBundle<Map<String, Triple<SpiderInfo, JobKey, Trigger>>> listAllJobs() {
		Map<String, Triple<SpiderInfo, JobKey, Trigger>> result = new HashMap<>();
		for(JobKey key:manager.listAll(QUARTZ_JOB_GROUP_NAME)) {
			Pair<JobDetail, Trigger> pair = manager.findInfo(key);
			SpiderInfo info = (SpiderInfo)pair.getLeft().getJobDataMap().get("spiderInfo");
			result.put(info.getId(), Triple.of(info, key, pair.getRight()));
		}
		return builder.bundle(null, () -> result);
	}
	
	public ResultBundle<String> removeQuartzJob(String spiderInfoId) {
		manager.removeJob(JobKey.jobKey(spiderInfoId, QUARTZ_JOB_GROUP_NAME));
		return builder.bundle(spiderInfoId, ok);
	}
	
	public ResultBundle<String> checkQuartzJob(String spiderInfoId) {
		try {
			Pair<JobDetail, Trigger> pair = manager.findInfo(JobKey.jobKey(spiderInfoId, QUARTZ_JOB_GROUP_NAME));
			SpiderInfo info = dao.findById(spiderInfoId);
			if(pair == null && info != null)
				return builder.bundle(spiderInfoId, () -> "true");
			else
				return builder.bundle(spiderInfoId, () -> "爬虫模板不存在或爬虫任务已添加至定时任务");
		} catch(Exception e) {
			return builder.bundle(spiderInfoId, () -> e.getLocalizedMessage());
		}
	}
	
	public String exportQuartz() {
		Map<String, Long> result = new HashMap<>();
		for(JobKey key:manager.listAll(QUARTZ_JOB_GROUP_NAME)) {
			Pair<JobDetail, Trigger> pair = manager.findInfo(key);
			String name = ((SpiderInfo)pair.getLeft().getJobDataMap().get("spiderInfo")).getId();
			Long hours = ((SimpleTrigger)((SimpleScheduleBuilder)pair.getRight().getScheduleBuilder()).build()).getRepeatInterval()/DateBuilder.MILLISECONDS_IN_HOUR;
			result.put(name, hours);
		}
		return gson.toJson(result);
	}
	
	public void importQuartz(String json) {
		Map<String, Integer> result = gson.fromJson(json, new TypeToken<Map<String, Integer>>(){}.getType());
		result.entrySet().forEach(entry -> createQuartzJob(entry.getKey(), entry.getValue()));
	}
	
	public int progress() {
		return spider.getProgress();
	}
	
	private void validateSpiderInfo(SpiderInfo info) {
		Preconditions.checkArgument(info.getStartUrl().size()>0, "起始地址列表不能为空");
		Preconditions.checkArgument(StringUtils.isNotBlank(info.getDomain()), "域名不能为空");
		Preconditions.checkArgument(!info.getDomain().contains("/"), "域名不能包含‘/’");
		Preconditions.checkArgument(info.getThread()>0, "线程数必须大于零");
		Preconditions.checkArgument(StringUtils.isNotBlank(info.getSiteName()), "网站名称不可为空");
		Preconditions.checkArgument(info.getTimeout()>=1000, "超时时间必须大于等于1秒");
		boolean expression = false;
		if(info.getDynamicFields() != null) {
			expression = info.getDynamicFields().stream()
			   .filter(config -> 
			       StringUtils.isBlank(config.getName()) 
			          || (StringUtils.isBlank(config.getRegex()) && StringUtils.isBlank(config.getXpath()))
			   ).count() == 0;
			Preconditions.checkArgument(expression, "动态字段配置含有无效配置,每一个动态字段都必须有name,而且正则和xpath不可同时为空,请检查");
		}
	}
}
