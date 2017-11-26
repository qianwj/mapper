package cn.nest.spider.gather.async.quartz;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import cn.nest.spider.entity.commons.SpiderInfo;
import cn.nest.spider.service.CommonSpiderService;

/**
 * 该作业不允许并发执行
 * @author web
 *
 */
@DisallowConcurrentExecution
public class SpiderJob extends QuartzJobBean {
	
	private static final Logger LOG = LogManager.getLogger(SpiderJob.class);
	
	private SpiderInfo info;
	
	private CommonSpiderService service;
	
	public SpiderInfo getInfo() {
		return info;
	}

	public SpiderJob setInfo(SpiderInfo info) {
		this.info = info;
		return this;
	}

	public CommonSpiderService getService() {
		return service;
	}

	public SpiderJob setService(CommonSpiderService service) {
		this.service = service;
		return this;
	}

	@Override
	protected void executeInternal(JobExecutionContext ctx) throws JobExecutionException {
		LOG.info("开始定时网页采集任务，网站：{}，模板ID：{}", info.getSiteName(), info.getId());
		String uuid = service.start(info).getResult();
		LOG.info("定时网页采集任务完成，网站：{}，模板ID：{},任务ID：{}", info.getSiteName(), info.getId(), uuid);
	}

}
