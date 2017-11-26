package cn.nest.spider.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Component("staticValue")
public class StaticValue {
	
	private String commonsIndex;
	
	private boolean spiderDebug;
	
	private int deleteTaskDelay;
	
	private int deleteTaskPeriod;//任务删除时间间隔，单位小时
	
	private long maxDownloadLength;
	
	private double crawlRatio;//页面抓取的最大比例
	
	private long limitOfDownloadQueue;//网页下载的队列的最大限制
	
	private boolean needES;
	
    private String esHost;
	
	private int esPort;
	
	private String esClusterName;
	
	private boolean needRedis;
	
	private int redisPort;
	
	private String redisHost;
	
	private String webpageRedisPublishChannelName;
	
	private String ajaxDownloader;
	
	private static final Logger LOG = LogManager.getLogger(StaticValue.class);
	
	public StaticValue() {
		LOG.debug("静态值正在初始化...");
		try {
			String json = FileUtils.readFileToString(new File(
					this.getClass().getClassLoader().getResource("staticvalue.json").getFile()));
			JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
			
			this.commonsIndex = obj.get("commonsIndex").getAsString();
			this.spiderDebug = obj.get("spiderDebug").getAsBoolean();
			this.deleteTaskDelay = obj.get("deleteTaskDelay").getAsInt();
			this.deleteTaskPeriod = obj.get("deleteTaskPeriod").getAsInt();
			this.crawlRatio = obj.get("crawlRatio").getAsDouble();
			this.maxDownloadLength = obj.get("maxDownloadLength").getAsLong();
			this.limitOfDownloadQueue = obj.get("limitOfDownloadQueue").getAsLong();
			this.needES = obj.get("needES").getAsBoolean();
			this.esPort = obj.get("esPort").getAsInt();
			this.esHost = obj.get("esHost").getAsString();
			this.esClusterName = obj.get("esClusterName").getAsString();
			this.needRedis = obj.get("needRedis").getAsBoolean();
			this.redisHost = obj.get("redisHost").getAsString();
			this.redisPort = obj.get("redisPort").getAsInt();
			this.webpageRedisPublishChannelName = obj.get("webpageRedisPublishChannelName").getAsString();
			this.ajaxDownloader = obj.get("ajaxDownloader").getAsString();
			LOG.debug("静态值初始化成功");
		} catch(IOException e) {
			LOG.fatal("静态值初始化失败，由于 " + e.getLocalizedMessage());
			e.printStackTrace();
		} 
	}

	public String getCommonsIndex() {
		return commonsIndex;
	}

	public StaticValue setCommonsIndex(String commonsIndex) {
		this.commonsIndex = commonsIndex;
		return this;
	}
	
	public boolean isSpiderDebug() {
		return spiderDebug;
	}

	public StaticValue setSpiderDebug(boolean spiderDebug) {
		this.spiderDebug = spiderDebug;
		return this;
	}

	public int getDeleteTaskDelay() {
		return deleteTaskDelay;
	}

	public StaticValue setDeleteTaskDelay(int deleteTaskDelay) {
		this.deleteTaskDelay = deleteTaskDelay;
		return this;
	}

	public int getDeleteTaskPeriod() {
		return deleteTaskPeriod;
	}

	public StaticValue setDeleteTaskPeriod(int deleteTaskPeriod) {
		this.deleteTaskPeriod = deleteTaskPeriod;
		return this;
	}

	public long getMaxDownloadLength() {
		return maxDownloadLength;
	}

	public StaticValue setMaxDownloadLength(long maxDownloadLength) {
		this.maxDownloadLength = maxDownloadLength;
		return this;
	}

	public double getCrawlRatio() {
		return crawlRatio;
	}

	public StaticValue setCrawlRatio(double crawlRatio) {
		this.crawlRatio = crawlRatio;
		return this;
	}

	public long getLimitOfDownloadQueue() {
		return limitOfDownloadQueue;
	}

	public StaticValue setLimitOfDownloadQueue(long limitOfDownloadQueue) {
		this.limitOfDownloadQueue = limitOfDownloadQueue;
		return this;
	}

	public boolean isNeedES() {
		return needES;
	}

	public StaticValue setNeedES(boolean needES) {
		this.needES = needES;
		return this;
	}
	
	public String getEsHost() {
		return esHost;
	}

	public StaticValue setEsHost(String esHost) {
		this.esHost = esHost;
		return this;
	}

	public int getEsPort() {
		return esPort;
	}

	public StaticValue setEsPort(int esPort) {
		this.esPort = esPort;
		return this;
	}

	public String getEsClusterName() {
		return esClusterName;
	}

	public StaticValue setEsClusterName(String esClusterName) {
		this.esClusterName = esClusterName;
		return this;
	}

	public boolean isNeedRedis() {
		return needRedis;
	}

	public StaticValue setNeedRedis(boolean needRedis) {
		this.needRedis = needRedis;
		return this;
	}

	public int getRedisPort() {
		return redisPort;
	}

	public StaticValue setRedisPort(int redisPort) {
		this.redisPort = redisPort;
		return this;
	}

	public String getRedisHost() {
		return redisHost;
	}

	public StaticValue setRedisHost(String redisHost) {
		this.redisHost = redisHost;
		return this;
	}

	public String getWebpageRedisPublishChannelName() {
		return webpageRedisPublishChannelName;
	}

	public StaticValue setWebpageRedisPublishChannelName(String webpageRedisPublishChannelName) {
		this.webpageRedisPublishChannelName = webpageRedisPublishChannelName;
		return this;
	}

	public String getAjaxDownloader() {
		return ajaxDownloader;
	}

	public StaticValue setAjaxDownloader(String ajaxDownloader) {
		this.ajaxDownloader = ajaxDownloader;
		return this;
	}

	@Override
	public String toString() {
		return "    {\"commonsIndex\":\"" + commonsIndex + "\",\"spiderDebug\":\"" + spiderDebug + "\",\"deleteTaskDelay\":\"" + deleteTaskDelay
				+ "\",\"deleteTaskPeriod\":\"" + deleteTaskPeriod + "\",\"maxDownloadLength\":\"" + maxDownloadLength
				+ "\",\"crawlRatio\":\"" + crawlRatio + "\",\"limitOfDownloadQueue\":\"" + limitOfDownloadQueue + "\",\"needES\":\"" + needES
				+ "\",\"esHost\":\"" + esHost + "\",\"esPort\":\"" + esPort + "\",\"esClusterName\":\"" + esClusterName
				+ "\",\"needRedis\":\"" + needRedis + "\",\"redisPort\":\"" + redisPort + "\",\"redisHost\":\""
				+ redisHost + "\",\"webpageRedisPublishChannelName\":\"" + webpageRedisPublishChannelName
				+ "\",\"ajaxDownloader\":\"" + ajaxDownloader + "\"}  ";
	}
}
