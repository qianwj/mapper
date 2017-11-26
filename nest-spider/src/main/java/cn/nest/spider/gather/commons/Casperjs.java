package cn.nest.spider.gather.commons;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

import cn.nest.spider.entity.commons.Request;
import cn.nest.spider.util.HttpClientUtil;
import cn.nest.spider.util.StaticValue;

@Component
public class Casperjs {

	private static final Gson GSON = new Gson();
	
	@Autowired
	private HttpClientUtil util;
	
	@Autowired
	private StaticValue value;
	
	public String gatherHtml(Request request) throws IOException {
		return gatherHtml(request, value.getAjaxDownloader() + "html");
	}
	
	private String gatherHtml(Request request, String url) throws IOException {
		Preconditions.checkArgument(request.getUrl().startsWith("http"), "url必须以http开头， 当前url:%s", request.getUrl());
		Fetch fetch = new Fetch().setUrl(request.getUrl());
		String json = util.post(url, GSON.toJson(fetch));
		json = new String(json.getBytes("iso8859-1"), "utf-8");
		return new JsonParser().parse(json).getAsJsonObject().get("content").getAsString();
	}
	
	public class Fetch {
		
		private String proxy = "";
		
		private int jsViewPortWidth = 1024;
		
		private int jsViewPortHeight = 1024;
		
		private String jsRunAt;
		
		private String jsScript;
		
		private boolean loadImage = false;
		
		private int timeout = 5;
		
		private String url;
		
		private String method = "get";
		
		private String data = "";
		
		private Map<String, String> headers;

		public String getProxy() {
			return proxy;
		}

		public Fetch setProxy(String proxy) {
			this.proxy = proxy;
			return this;
		}

		public int getJsViewPortWidth() {
			return jsViewPortWidth;
		}

		public Fetch setJsViewPortWidth(int jsViewPortWidth) {
			this.jsViewPortWidth = jsViewPortWidth;
			return this;
		}

		public int getJsViewPortHeight() {
			return jsViewPortHeight;
		}

		public Fetch setJsViewPortHeight(int jsViewPortHeight) {
			this.jsViewPortHeight = jsViewPortHeight;
			return this;
		}

		public String getJsRunAt() {
			return jsRunAt;
		}

		public Fetch setJsRunAt(String jsRunAt) {
			this.jsRunAt = jsRunAt;
			return this;
		}

		public String getJsScript() {
			return jsScript;
		}

		public Fetch setJsScript(String jsScript) {
			this.jsScript = jsScript;
			return this;
		}

		public boolean isLoadImage() {
			return loadImage;
		}

		public Fetch setLoadImage(boolean loadImage) {
			this.loadImage = loadImage;
			return this;
		}

		public int getTimeout() {
			return timeout;
		}

		public Fetch setTimeout(int timeout) {
			this.timeout = timeout;
			return this;
		}

		public String getUrl() {
			return url;
		}

		public Fetch setUrl(String url) {
			this.url = url;
			return this;
		}

		public String getMethod() {
			return method;
		}

		public Fetch setMethod(String method) {
			this.method = method;
			return this;
		}

		public String getData() {
			return data;
		}

		public Fetch setData(String data) {
			this.data = data;
			return this;
		}

		public Map<String, String> getHeaders() {
			return headers;
		}

		public Fetch setHeaders(Map<String, String> headers) {
			this.headers = headers;
			return this;
		}
		
		
	}
}
