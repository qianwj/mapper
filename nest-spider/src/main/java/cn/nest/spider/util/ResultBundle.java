package cn.nest.spider.util;

public class ResultBundle<T> {

	//请求参数
	protected String param;
	
	//返回的结果数
	protected int count;
	
	//调用耗时
	protected long time;
	
	//调用是否成功
	protected boolean success;
	
	//调用不成功返回的错误信息
	protected String errorMessage;
	
	//本次调用的追踪ID
	protected int traceId;
	
	//返回的结果
	private T result;
	
	public ResultBundle() {}
	
	public ResultBundle(T result, String param, long time) {
        this.result = result;
        this.param = param;
        this.time = time;
        this.count = 1;
        this.success = true;
    }

    public ResultBundle(String param, long time, boolean success, String errorMessage) {
        result = null;
        this.success = success;
        this.errorMessage = errorMessage;
        this.param = param;
        this.time = time;
        this.count = 0;
    }

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public int getTraceId() {
		return traceId;
	}

	public void setTraceId(int traceId) {
		this.traceId = traceId;
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "ResultBundle [param=" + param + ", count=" + count + ", time=" + time + ", success=" + success
				+ ", errorMessage=" + errorMessage + ", traceId=" + traceId + ", result=" + result + "]";
	}
}
