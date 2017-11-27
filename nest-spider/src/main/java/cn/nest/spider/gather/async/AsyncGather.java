package cn.nest.spider.gather.async;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.nest.spider.entity.async.Task;
import cn.nest.spider.entity.async.TaskManager;
import cn.nest.spider.entity.util.State;


public class AsyncGather {

	@Autowired
	protected TaskManager manager;
	
	private int connectionPort;
		
	public AsyncGather() {}
	
	public Collection<Task> getAllTask(boolean extra) {
		return manager.getAllTask(extra);
	}
	
	public Collection<Task> getTasksByState(State state, boolean extra) {
		return manager.getAllTask(extra).stream()
				.filter(task -> task.getState() == state).collect(Collectors.toList());
	}
	
	public Collection<Task> getTasksByTime(long start, long end, boolean extra) {
		return manager.getAllTask(extra).stream()
				.filter(task -> task.getTime() >= start && task.getTime() <= end).collect(Collectors.toList());
	}
	
	public Task findTask(String id, boolean extra) {
		return manager.findTask(id, extra);
	}
	
	public int getTaskCount(String id) {
		return manager.getTaskCount(id);
	}
	
	public void delTask(String id) {
		manager.delTask(id);
	}
	
	public long countByState(State state) {
		return manager.countByState(state);
	}
	
	public int getConnectionPort() {
		return this.connectionPort;
	}
}
