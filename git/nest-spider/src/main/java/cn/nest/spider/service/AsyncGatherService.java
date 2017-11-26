package cn.nest.spider.service;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.nest.spider.entity.async.Task;
import cn.nest.spider.entity.util.State;
import cn.nest.spider.gather.async.AsyncGather;
import cn.nest.spider.util.ResultBundle;
import cn.nest.spider.util.ResultBundleBuilder;
import cn.nest.spider.util.ResultList;
import cn.nest.spider.util.Supplier;

@Service("asyncGatherService")
public class AsyncGatherService {

	@Autowired
	protected AsyncGather gather;
	
	@Autowired
	protected ResultBundleBuilder builder;
	
	public AsyncGatherService() {};
	
	public AsyncGatherService(AsyncGather gather) {
		this.gather = gather;
	}
	
	public ResultList<Task> findAll(boolean extra) {
		Supplier<List<Task>> sup = () -> new LinkedList<>(gather.getAllTask(extra));
		return builder.bundleList(null, sup);
	}
	
	public ResultList<Task> findTasksByState(State state, boolean extra) {
		Supplier<List<Task>> sup = () -> new LinkedList<>(gather.getTasksByState(state, extra));
		return builder.bundleList(state.name(), sup);
	}
	
	public ResultBundle<Task> findTasksByTime(Long start, Long end, boolean extra) {
		Supplier<List<Task>> sup = () -> new LinkedList<>(gather.getTasksByTime(start, end, extra));
		return builder.bundleList("start:" + start + ", end:" + end, sup);
	}
	
	public ResultBundle<Task> findTask(String id, boolean extra) {
		Supplier<Task> sup = () -> gather.findTask(id, extra);
		return builder.bundle(null, sup);
	}
	
	public ResultBundle<Integer> getTaskCount(String id) {
		Supplier<Integer> sup = () -> gather.getTaskCount(id);
		return builder.bundle(null, sup);
	}
	
	public ResultBundle<Integer> getConnectionPort() {
		Supplier<Integer> sup = () -> gather.getConnectionPort();
		return builder.bundle(null, sup);
	}
	
	public ResultBundle<Long> countByState(State state) {
		Supplier<Long> sup = () -> gather.countByState(state);
		return builder.bundle(state.name(), sup);
	}
	
	public ResultBundle<String> delTask(String id) {
		Supplier<String> sup = () -> {
			gather.delTask(id);
			return "OK!";
		};
		return builder.bundle(null, sup);
	}
}
