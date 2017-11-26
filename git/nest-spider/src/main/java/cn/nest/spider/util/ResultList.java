package cn.nest.spider.util;

import java.util.Collection;

public class ResultList<T> extends ResultBundle<T> {

	//结果
	private Collection<T> res;
	
	public ResultList(Collection<T> res, String keyword, long time) {
		this.res = res;
        this.param = keyword;
        this.time = time;
        this.count = res.size();
        this.success = true;
	}
	
	public ResultList(String keyword, long time, boolean success, String errorMessage) {
		this.param = keyword;
		this.time = time;
		this.success = success;
		this.errorMessage = errorMessage;
		this.count = 0;
	}

	public Collection<T> getRes() {
		return res;
	}

	public void setRes(Collection<T> res) {
		this.res = res;
	}

	@Override
	public String toString() {
		return "    {\"res\":\"" + res + "\"}  ";
	}
	
	
}
