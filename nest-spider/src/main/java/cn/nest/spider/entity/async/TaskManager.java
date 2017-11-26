package cn.nest.spider.entity.async;


import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.base.Preconditions;

import cn.nest.spider.entity.util.State;
import cn.nest.spider.util.HttpClientUtil;

@Component
@Scope("singleton")
public class TaskManager {

	private static final Logger LOG = LogManager.getLogger(TaskManager.class);
	
	private Logger log = LogManager.getLogger("TASK_LOG");
	
	private Map<String, Task> taskMap = new LinkedHashMap<>();
	
	@Autowired
	private HttpClientUtil client;
	
	
	public Collection<Task> getAllTask(boolean extra) {
	    log.info("任务获取开始， 试否获取额外信息：" + extra);
	    return extra?taskMap.values():taskMap.values().stream().map(task -> {
	    	Task t = null;
	    	try {
	    		t = (Task)task.clone();
	    		t.setExtraInfo(null);
	    		return t;
	    	} catch(CloneNotSupportedException e) {
	    		LOG.error("克隆失败， 由于 " + e.getLocalizedMessage());
	    		e.printStackTrace();
	    	}
	    	return null;
	    }).collect(Collectors.toList());
	}
	
	public Task findTask(String id, boolean extra) {
		log.info("获取任务实体， 任务ID:" + id);
		Task t = taskMap.get(id);
		if(t != null && !extra) {
			try {
				t = (Task)t.clone();
			} catch (CloneNotSupportedException e) {
				LOG.error("克隆失败， 由于 " + e.getLocalizedMessage());
				e.printStackTrace();
			}
			t.setExtraInfo(null);
		}
		return t;
	}
	
	public Task findTask(String id) {
		return findTask(id, true);
	}
	
	public int getTaskCount(String id) {
		if(taskMap.get(id) == null)
			throw new IllegalArgumentException("当前任务不存在， 任务ID:" + id);
		return taskMap.get(id).getCount();
	}
	
	public Task initTask(String id, String name, String callbackUrl, String callbackPara) {
		Task task = new Task(id, name, System.currentTimeMillis());
		task.addCallbackUrl(callbackUrl);
		task.setCallbackPara(callbackPara + "&taskId=" + id).setDescription("任务" + name + "已初始化");
		taskMap.put(task.getTaskId(), task);
		return task;
	}
	
	public Task initTask(String id, String name, List<String> callbackUrl, String callbackPara) {
		Task task = new Task(name, id, System.currentTimeMillis());
		task.setCallbackURL(callbackUrl).setCallbackPara(callbackPara + "&taskId=" + id)
		.setDescription("任务" + name + "已初始化");
		taskMap.put(task.getTaskId(), task);
		LOG.info(task.getDescriptions());
		return task;
	}
	
	public Task initTask(String name, String callbackUrl, String callbackPara) {
		return initTask(UUID.randomUUID().toString(), name, callbackUrl, callbackPara);
	}
	
	public Task initTask(String name) {
		return initTask(name, null, null);
	}
	
	public void startTask(Task task) {
		task.setState(State.RUNNING);
		task.setDescription("任务%s已开始， 任务ID:%s", task.getName(), task.getTaskId());
	}
	
	public synchronized void increaseCount(Task task) {
		task.setCount(task.getCount() + 1);
	}
	
	public void stopTask(Task task) {
		String id = task.getTaskId();
		log.info("任务已停止， 任务ID:" + id);
		try {
			boolean useCallback = task.getCallbackURL().size() > 0;
			if(useCallback) {
				task.getCallbackURL().stream().forEach(url -> {
					if(!StringUtils.isBlank(url) && url.startsWith("http")) {
						task.setDescription("使用HTTP回调");
						log.info("任务使用HTTP回调， 任务ID:" + id);
						String urlWithPara;
						if(!url.contains("?")) {
							urlWithPara = url + "?" + task.getCallbackPara();
						} else {
							urlWithPara = url + "&" + task.getCallbackPara();
						}
						LOG.debug("任务线程结束， urlWithPara:" + urlWithPara);
						String callbackReturn = client.get(urlWithPara);
						LOG.info("任务线程结束， 回调返回结果：" + callbackReturn);
						task.setDescription("任务回调完成， url:%s, 返回结果:%s", urlWithPara, callbackReturn);
					}
				});
			} else {
				LOG.info("回调地址为空， 不进行回调。 任务ID:" + id);
			}
		} catch(Exception e) {
			LOG.error("任务回调出错， 任务ID:" + id + ", 由于 " + e.getLocalizedMessage());
		}
		task.setState(State.STOP);
		task.setTime(System.currentTimeMillis());
		task.setDescription("任务已停止， 任务ID:%s", id);
		log.info("任务已停止，任务ID:" + id);
	}
	
	public void stopTask(String id) {
		Task t = taskMap.get(id);
		if(t != null)
			stopTask(t);
	}
	
	public boolean findRunningTaskByName(String name) {
		return taskMap.values().stream().filter(task -> 
			name.equals(task.getName()) && task.getState() == State.RUNNING
		).count() > 0;
	}
	
	public boolean findScheduledTaskByName(String name) {
		return taskMap.values().stream().filter(task ->
		    name.equals(task.getName()) && task.getTimeUnit() != null && task.getPeriod() > 0
		).count() > 0;
	}
	
	public boolean findTask(Function<Task, Boolean> condition) {
		return taskMap.values().stream().filter(task -> {
			Boolean b = condition.apply(task);
			return b == null?false:b;
		}).count() > 0;
	}
	
	public void delTask(String id) {
		Task task = findTask(id);
		task.getDescriptions().clear();
		task.getExtraInfo().clear();
		log.info("任务开始删除， 任务ID:" + id);
		Preconditions.checkNotNull(task, "任务对象为空，任务ID:" + id);
        Preconditions.checkArgument(task.getState() != State.RUNNING && task.getState() != State.INIT, "当前任务正在运行不可删除,状态:" + task.getState());
	    taskMap.remove(id);
	}
	
	public void delTasksByState(State state) {
		log.info("任务集删除开始， 任务集状态:" + state);
		taskMap.entrySet().stream().forEach(entry -> {
			Task task = entry.getValue();
			if(task.getState() == state) {
				task.getDescriptions().clear();
				task.getExtraInfo().clear();
				log.info("任务删除开始， 任务ID:" + entry.getKey());
//				Preconditions.checkNotNull(task, "任务对象为空，任务ID:" + id);
				taskMap.remove(entry.getKey());
			}
		});
	}
	
	public long countByState(State state) {
		return taskMap.values().stream().filter(task -> task.getState() == state).count();
	}
}