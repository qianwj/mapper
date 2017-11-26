package cn.nest.spider.entity.commons;

import java.util.Map;
import java.util.function.Function;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;


public class Request {

	private String url;
	
    private String user_agent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36";
	
	private HttpRequestBase method = new HttpGet();
	
	private boolean followRedirect = true;
    
	private boolean ajax = false;
    
    private boolean needLogin = false;
    
    private LoginInfo loginInfo;
    
    private Function<LoginInfo, Map<String, String>> loginFunc;
    
    private Map<String, String> initHeaders;
    
    private Map<String, String> para;
    
    private int timeout = 5000;
    
    public Request() {}
    
    public Request(String url) {
    	this.url = url;
    }
    
    public Request(String url, boolean ajax) {
    	this.url = url;
    	this.ajax = ajax;
    }

	public String getUrl() {
		return url;
	}

	public Request setUrl(String url) {
		this.url = url;
		return this;
	}

	public String getUser_agent() {
		return user_agent;
	}

	public Request setUser_agent(String user_agent) {
		this.user_agent = user_agent;
		return this;
	}
	
	public HttpRequestBase getMethod() {
		return this.method;
	}
	
	public Request setMethod(HttpRequestBase method) {
		this.method = method;
		return this;
	}

	public boolean isFollowRedirect() {
		return followRedirect;
	}

	public Request setFollowRedirect(boolean followRedirect) {
		this.followRedirect = followRedirect;
		return this;
	}

	public boolean isAjax() {
		return ajax;
	}

	public Request setAjax(boolean ajax) {
		this.ajax = ajax;
		return this;
	}

	public boolean isNeedLogin() {
		return needLogin;
	}

	public Request setNeedLogin(boolean needLogin) {
		this.needLogin = needLogin;
		return this;
	}

	public LoginInfo getLoginInfo() {
		return loginInfo;
	}

	public Request setLoginInfo(LoginInfo loginInfo) {
		this.loginInfo = loginInfo;
		return this;
	}

	public Function<LoginInfo, Map<String, String>> getLoginFunc() {
		return loginFunc;
	}

	public Request setLoginFunc(Function<LoginInfo, Map<String, String>> loginFunc) {
		this.loginFunc = loginFunc;
		return this;
	}

	public Map<String, String> getInitHeaders() {
		return initHeaders;
	}

	public Request setInitHeaders(Map<String, String> initHeaders) {
		this.initHeaders = initHeaders;
		return this;
	}

	public Map<String, String> getPara() {
		return para;
	}

	public Request setPara(Map<String, String> para) {
		this.para = para;
		return this;
	}

	public int getTimeout() {
		return timeout;
	}

	public Request setTimeout(int timeout) {
		this.timeout = timeout;
		return this;
	}
    
    
}
