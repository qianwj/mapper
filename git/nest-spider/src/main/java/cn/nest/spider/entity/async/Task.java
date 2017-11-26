package cn.nest.spider.entity.async;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.builder.EqualsBuilder;

import cn.nest.spider.entity.util.State;

public class Task implements Cloneable {
	
	private String taskId;
    
	private String name;
    
	private Map<Date, String> descriptions = new LinkedHashMap<>();
    
	private State state;
    
	private long time;
    
	private int count;
    
	private List<String> callbackURL =  new ArrayList<>();
    
	private String callbackPara;
    
	private long period;
    
	private TimeUnit timeUnit;
    
	private Map<Object, Object> extraInfo = new HashMap<>();
	
	public Task(String name, String taskId, long time) {
		this.name = name;
		this.taskId = taskId;
		this.state = State.INIT;
		this.time = time;
	}
	
	public String getTaskId() {
		return taskId;
	}
	
	public Task setTaskId(String taskId) {
		this.taskId = taskId;
		return this;
	}
	
	public String getName() {
		return name;
	}
	
	public Task setName(String name) {
		this.name = name;
		return this;
	}
	
	public Map<Date, String> getDescriptions() {
		return descriptions;
	}
	
	public Task setDescription(Map<Date, String> descriptions) {
		this.descriptions = descriptions;
		return this;
	}
	
	public void setDescription(String description, Object...param) {
		final String str = (param != null?String.format(description, param):description);
		Date date = new Date();
		descriptions.keySet().stream().forEach(now -> {
			if(Math.abs(now.getTime() - date.getTime()) < 2000) {
				descriptions.put(now, descriptions.get(now) + "</br>" + str);
				return;
			}
		});
		descriptions.put(date, str);
	}
	
	public State getState() {
		return state;
	}
	
	public Task setState(State state) {
		this.state = state;
		return this;
	}
	
	public long getTime() {
		return time;
	}
	
	public Task setTime(long time) {
		this.time = time;
		return this;
	}
	
	public int getCount() {
		return count;
	}
	
	public Task setCount(int count) {
		this.count = count;
		return this;
	}
	
	public void increaseCount() {
		this.count++;
	}
	
	public List<String> getCallbackURL() {
		return callbackURL;
	}
	
	public Task setCallbackURL(List<String> callbackURL) {
		this.callbackURL = callbackURL;
		return this;
	}
	
	public String getCallbackPara() {
		return callbackPara;
	}
	
	public Task setCallbackPara(String callbackPara) {
		this.callbackPara = callbackPara;
		return this;
	}
	
	public long getPeriod() {
		return period;
	}
	
	public Task setPeriod(long period) {
		this.period = period;
		return this;
	}
	
	public TimeUnit getTimeUnit() {
		return timeUnit;
	}
	
	public Task setTimeUnit(TimeUnit timeUnit) {
		this.timeUnit = timeUnit;
		return this;
	}
	
	public Map<Object, Object> getExtraInfo() {
		return extraInfo;
	}
	
	public Object getExtraInfoByKey(Object key) {
        return extraInfo.get(key);
    }
	
	public void addExtraInfo(Object key, Object value) {
        extraInfo.put(key, value);
    }
	
	public Task setExtraInfo(Map<Object, Object> extraInfo) {
		this.extraInfo = extraInfo;
		return this;
	}
	
	public void addCallbackUrl(String callbackUrl) {
		this.callbackURL.add(callbackUrl);
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null || getClass() != obj.getClass()) return false;
		
		Task t = (Task)obj;
		
		return new EqualsBuilder()
				.append(this.getTaskId(), t.getTaskId()).append(this.getName(), t.getName()).isEquals();
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	@Override
	public String toString() {
		return "Task{\"taskId\":\"" + taskId + "\",\"name\":\"" + name + "\",\"descriptions\":\"" + descriptions
				+ "\",\"state\":\"" + state + "\",\"time\":\"" + time + "\",\"count\":\"" + count
				+ "\",\"callbackURL\":\"" + callbackURL + "\",\"callbackPara\":\"" + callbackPara + "\",\"period\":\""
				+ period + "\",\"timeUnit\":\"" + timeUnit + "\",\"extraInfo\":\"" + extraInfo + "\"}  ";
	}
    
}
