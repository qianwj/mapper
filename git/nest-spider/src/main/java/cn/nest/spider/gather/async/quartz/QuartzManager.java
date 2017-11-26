package cn.nest.spider.gather.async.quartz;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.stereotype.Component;

/**
 * 
 * @author Qian Wenjin
 *
 */
@Component
public class QuartzManager {
	
	private static final Logger LOG = LogManager.getLogger(QuartzManager.class);

	private Scheduler scheduler = null;
	
	public QuartzManager() {
		try {
			scheduler = new StdSchedulerFactory().getScheduler();
		} catch (SchedulerException e) {
			LOG.fatal("定时任务日程创建失败");
		}
	}
	
	public Pair<TriggerKey, JobKey> addJob(String jobName, String group, String trigName, String trigGroup, Class<? extends Job> jobClass, Map<String, Object> data, int hours) {
		try {
			JobDetail detail = JobBuilder.newJob().ofType(jobClass)
					.usingJobData(new JobDataMap(data)).withIdentity(jobName, group).build();
			
			Trigger trigger = TriggerBuilder.newTrigger().forJob(jobName, group).withIdentity(trigName, trigGroup)
					.withSchedule(SimpleScheduleBuilder.repeatHourlyForever(hours)).build();
			//启动
			if(!scheduler.isShutdown())
				scheduler.start();
			
			scheduler.scheduleJob(detail, trigger);
			return Pair.of(trigger.getKey(), detail.getKey());
		} catch(SchedulerException e) {
			LOG.error("作业调度创建异常， 由于 " + e.getLocalizedMessage());
		    throw new RuntimeException(e);
		}
	}
	
	public Pair<JobDetail, Trigger> findInfo(JobKey key) {
		try {
			JobDetail detail = scheduler.getJobDetail(key);
			Trigger trigger = scheduler.getTriggersOfJob(key).get(0);
			return Pair.of(detail, trigger);
		} catch(SchedulerException e) {
			LOG.debug("查找任务信息异常， 由于 " + e.getLocalizedMessage());
			return null;
		}
	}
	
	public Set<JobKey> listAll(String group) {
		try {
			return scheduler.getJobKeys(GroupMatcher.jobGroupEquals(group));
		} catch (SchedulerException e) {
			LOG.error("查找任务关键字失败， 由于 " + e.getLocalizedMessage());
			return new HashSet<>();
		}
	}
	
    public void startJobs() {
        try {
            scheduler.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void shutdownJobs() {
        try {
            if (!scheduler.isShutdown()) {
                scheduler.shutdown();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public void removeJob(JobKey key) {
    	try {
			TriggerKey trigKey = scheduler.getTriggersOfJob(key).get(0).getKey();
			scheduler.pauseTrigger(trigKey);
			scheduler.unscheduleJob(trigKey);
			scheduler.deleteJob(key);
		} catch (SchedulerException e) {
			LOG.error("任务删除失败， 由于 " + e.getLocalizedMessage());
			throw new RuntimeException(e);
		}
    }
}
