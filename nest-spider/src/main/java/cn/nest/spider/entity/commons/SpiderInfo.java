package cn.nest.spider.entity.commons;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SpiderInfo {
	/**
     * 使用多少抓取线程
     */
    private int thread = 1;
    /**
     * 失败的网页重试次数
     */
    private int retry = 2;
    /**
     * 抓取每个网页睡眠时间
     */
    private int sleep = 0;
    /**
     * 最大抓取网页数量,0代表不限制
     */
    private int maxPageGather = 10;
    /**
     * HTTP链接超时时间
     */
    private int timeout = 5000;
    /**
     * 网站权重
     */
    private int priority;
    /**
     * 是否只抓取首页
     */
    private boolean gatherFirstPage = false;
    /**
     * 抓取模板id
     */
    private String id;
    /**
     * 网站名称
     */
    private String siteName;
    /**
     * 域名
     */
    private String domain;
    /**
     * 起始链接
     */
    private List<String> startUrl;
    /**
     * 正文正则表达式
     */
    private String contentReg;
    /**
     * 正文Xpath
     */
    private String contentXPath;
    /**
     * 标题正则
     */
    private String titleReg;
    /**
     * 标题xpath
     */
    private String titleXPath;
    /**
     * 分类信息正则
     */
    private String categoryReg;
    /**
     * 分类信息XPath
     */
    private String categoryXPath;
    /**
     * 默认分类
     */
    private String defaultCategory;
    /**
     * url正则
     */
    private String urlReg;
    /**
     * 编码
     */
    private String charset;
    /**
     * 发布时间xpath
     */
    private String publishTimeXPath;
    /**
     * 发布时间正则
     */
    private String publishTimeReg;
    /**
     * 发布时间模板
     */
    private String publishTimeFormat;
    /**
     * 回调url
     */
    private List<String> callbackUrl;
    /**
     * 是否进行nlp处理
     */
    private boolean doNLP = true;
    /**
     * 网页必须有标题
     */
    private boolean needTitle = false;
    /**
     * 网页必须有正文
     */
    private boolean needContent = false;
    /**
     * 网页必须有发布时间
     */
    private boolean needPublishTime = false;
    /**
     * 动态字段列表
     */
    private List<FieldConfig> dynamicFields = new LinkedList<>();
    /**
     * 静态字段
     */
    private List<StaticField> staticFields = new ArrayList<>();
    /**
     * 语言,用于配置发布时间
     */
    private String lang;
    /**
     * 国家,用于配置发布时间
     */
    private String country;
    /**
     * User Agent
     */
    private String userAgent = "Mozilla/5.0 (Windows NT 5.2) AppleWebKit/534.30 (KHTML, like Gecko) Chrome/12.0.742.122 Safari/534.30";
    /**
     * 是否保存网页快照,默认保存
     */
    private boolean saveCapture = true;
    /**
     * 是否是ajax网站,如果是则使用casperjs下载器
     */
    private boolean ajaxSite = false;
    /**
     * 自动探测发布时间
     */
    private boolean autoDetectPublishDate = false;
    
    private String proxyHost;
    
    private int proxyPort;
    
    private String proxyUsername;
    
    private String proxyPassword;
   
    public int getThread() {
		return thread;
	}

	public SpiderInfo setThread(int thread) {
		this.thread = thread;
		return this;
	}

	public int getRetry() {
		return retry;
	}

	public SpiderInfo setRetry(int retry) {
		this.retry = retry;
		return this;
	}

	public int getSleep() {
		return sleep;
	}

	public SpiderInfo setSleep(int sleep) {
		this.sleep = sleep;
		return this;
	}

	public int getMaxPageGather() {
		return maxPageGather;
	}

	public SpiderInfo setMaxPageGather(int maxPageGather) {
		this.maxPageGather = maxPageGather;
		return this;
	}

	public int getTimeout() {
		return timeout;
	}

	public SpiderInfo setTimeout(int timeout) {
		this.timeout = timeout;
		return this;
	}

	public int getPriority() {
		return priority;
	}

	public SpiderInfo setPriority(int priority) {
		this.priority = priority;
		return this;
	}

	public boolean isGatherFirstPage() {
		return gatherFirstPage;
	}

	public SpiderInfo setGatherFirstPage(boolean gatherFirstPage) {
		this.gatherFirstPage = gatherFirstPage;
		return this;
	}

	public String getId() {
		return id;
	}

	public SpiderInfo setId(String id) {
		this.id = id;
		return this;
	}

	public String getSiteName() {
		return siteName;
	}

	public SpiderInfo setSiteName(String siteName) {
		this.siteName = siteName;
		return this;
	}

	public String getDomain() {
		return domain;
	}

	public SpiderInfo setDomain(String domain) {
		this.domain = domain;
		return this;
	}

	public List<String> getStartUrl() {
		return startUrl;
	}

	public SpiderInfo setStartUrl(List<String> startUrl) {
		this.startUrl = startUrl;
		return this;
	}

	public String getContentReg() {
		return contentReg;
	}

	public SpiderInfo setContentReg(String contentReg) {
		this.contentReg = contentReg;
		return this;
	}

	public String getContentXPath() {
		return contentXPath;
	}

	public SpiderInfo setContentXPath(String contentXPath) {
		this.contentXPath = contentXPath;
		return this;
	}

	public String getTitleReg() {
		return titleReg;
	}

	public SpiderInfo setTitleReg(String titleReg) {
		this.titleReg = titleReg;
		return this;
	}

	public String getTitleXPath() {
		return titleXPath;
	}

	public SpiderInfo setTitleXPath(String titleXPath) {
		this.titleXPath = titleXPath;
		return this;
	}

	public String getCategoryReg() {
		return categoryReg;
	}

	public SpiderInfo setCategoryReg(String categoryReg) {
		this.categoryReg = categoryReg;
		return this;
	}

	public String getCategoryXPath() {
		return categoryXPath;
	}

	public SpiderInfo setCategoryXPath(String categoryXPath) {
		this.categoryXPath = categoryXPath;
		return this;
	}

	public String getDefaultCategory() {
		return defaultCategory;
	}

	public SpiderInfo setDefaultCategory(String defaultCategory) {
		this.defaultCategory = defaultCategory;
		return this;
	}

	public String getUrlReg() {
		return urlReg;
	}

	public SpiderInfo setUrlReg(String urlReg) {
		this.urlReg = urlReg;
		return this;
	}

	public String getCharset() {
		return charset;
	}

	public SpiderInfo setCharset(String charset) {
		this.charset = charset;
		return this;
	}

	public String getPublishTimeXPath() {
		return publishTimeXPath;
	}

	public SpiderInfo setPublishTimeXPath(String publishTimeXPath) {
		this.publishTimeXPath = publishTimeXPath;
		return this;
	}

	public String getPublishTimeReg() {
		return publishTimeReg;
	}

	public SpiderInfo setPublishTimeReg(String publishTimeReg) {
		this.publishTimeReg = publishTimeReg;
		return this;
	}

	public String getPublishTimeFormat() {
		return publishTimeFormat;
	}

	public SpiderInfo setPublishTimeFormat(String publishTimeFormat) {
		this.publishTimeFormat = publishTimeFormat;
		return this;
	}

	public List<String> getCallbackUrl() {
		return callbackUrl;
	}

	public SpiderInfo setCallbackUrl(List<String> callbackUrl) {
		this.callbackUrl = callbackUrl;
		return this;
	}

	public boolean isDoNLP() {
		return doNLP;
	}

	public SpiderInfo setDoNLP(boolean doNLP) {
		this.doNLP = doNLP;
		return this;
	}

	public boolean isNeedTitle() {
		return needTitle;
	}

	public SpiderInfo setNeedTitle(boolean needTitle) {
		this.needTitle = needTitle;
		return this;
	}

	public boolean isNeedContent() {
		return needContent;
	}

	public SpiderInfo setNeedContent(boolean needContent) {
		this.needContent = needContent;
		return this;
	}

	public boolean isNeedPublishTime() {
		return needPublishTime;
	}

	public SpiderInfo setNeedPublishTime(boolean needPublishTime) {
		this.needPublishTime = needPublishTime;
		return this;
	}

	public List<FieldConfig> getDynamicFields() {
		return dynamicFields;
	}

	public SpiderInfo setDynamicFields(List<FieldConfig> dynamicFields) {
		this.dynamicFields = dynamicFields;
		return this;
	}

	public List<StaticField> getStaticFields() {
		return staticFields;
	}

	public SpiderInfo setStaticFields(List<StaticField> staticFields) {
		this.staticFields = staticFields;
		return this;
	}

	public String getLang() {
		return lang;
	}

	public SpiderInfo setLang(String lang) {
		this.lang = lang;
		return this;
	}

	public String getCountry() {
		return country;
	}

	public SpiderInfo setCountry(String country) {
		this.country = country;
		return this;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public SpiderInfo setUserAgent(String userAgent) {
		this.userAgent = userAgent;
		return this;
	}

	public boolean isSaveCapture() {
		return saveCapture;
	}

	public SpiderInfo setSaveCapture(boolean saveCapture) {
		this.saveCapture = saveCapture;
		return this;
	}

	public boolean isAjaxSite() {
		return ajaxSite;
	}

	public SpiderInfo setAjaxSite(boolean ajaxSite) {
		this.ajaxSite = ajaxSite;
		return this;
	}

	public boolean isAutoDetectPublishDate() {
		return autoDetectPublishDate;
	}

	public SpiderInfo setAutoDetectPublishDate(boolean autoDetectPublishDate) {
		this.autoDetectPublishDate = autoDetectPublishDate;
		return this;
	}

	public String getProxyHost() {
		return proxyHost;
	}

	public SpiderInfo setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
		return this;
	}

	public int getProxyPort() {
		return proxyPort;
	}

	public SpiderInfo setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
		return this;
	}

	public String getProxyUsername() {
		return proxyUsername;
	}

	public SpiderInfo setProxyUsername(String proxyUsername) {
		this.proxyUsername = proxyUsername;
		return this;
	}

	public String getProxyPassword() {
		return proxyPassword;
	}

	public SpiderInfo setProxyPassword(String proxyPassword) {
		this.proxyPassword = proxyPassword;
		return this;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (ajaxSite ? 1231 : 1237);
		result = prime * result + (autoDetectPublishDate ? 1231 : 1237);
		result = prime * result + ((callbackUrl == null) ? 0 : callbackUrl.hashCode());
		result = prime * result + ((categoryReg == null) ? 0 : categoryReg.hashCode());
		result = prime * result + ((categoryXPath == null) ? 0 : categoryXPath.hashCode());
		result = prime * result + ((charset == null) ? 0 : charset.hashCode());
		result = prime * result + ((contentReg == null) ? 0 : contentReg.hashCode());
		result = prime * result + ((contentXPath == null) ? 0 : contentXPath.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((defaultCategory == null) ? 0 : defaultCategory.hashCode());
		result = prime * result + (doNLP ? 1231 : 1237);
		result = prime * result + ((domain == null) ? 0 : domain.hashCode());
		result = prime * result + ((dynamicFields == null) ? 0 : dynamicFields.hashCode());
		result = prime * result + (gatherFirstPage ? 1231 : 1237);
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((lang == null) ? 0 : lang.hashCode());
		result = prime * result + maxPageGather;
		result = prime * result + (needContent ? 1231 : 1237);
		result = prime * result + (needPublishTime ? 1231 : 1237);
		result = prime * result + (needTitle ? 1231 : 1237);
		result = prime * result + priority;
		result = prime * result + ((proxyHost == null) ? 0 : proxyHost.hashCode());
		result = prime * result + ((proxyPassword == null) ? 0 : proxyPassword.hashCode());
		result = prime * result + proxyPort;
		result = prime * result + ((proxyUsername == null) ? 0 : proxyUsername.hashCode());
		result = prime * result + ((publishTimeFormat == null) ? 0 : publishTimeFormat.hashCode());
		result = prime * result + ((publishTimeReg == null) ? 0 : publishTimeReg.hashCode());
		result = prime * result + ((publishTimeXPath == null) ? 0 : publishTimeXPath.hashCode());
		result = prime * result + retry;
		result = prime * result + (saveCapture ? 1231 : 1237);
		result = prime * result + ((siteName == null) ? 0 : siteName.hashCode());
		result = prime * result + sleep;
		result = prime * result + ((startUrl == null) ? 0 : startUrl.hashCode());
		result = prime * result + ((staticFields == null) ? 0 : staticFields.hashCode());
		result = prime * result + thread;
		result = prime * result + timeout;
		result = prime * result + ((titleReg == null) ? 0 : titleReg.hashCode());
		result = prime * result + ((titleXPath == null) ? 0 : titleXPath.hashCode());
		result = prime * result + ((urlReg == null) ? 0 : urlReg.hashCode());
		result = prime * result + ((userAgent == null) ? 0 : userAgent.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SpiderInfo other = (SpiderInfo) obj;
		if (ajaxSite != other.ajaxSite)
			return false;
		if (autoDetectPublishDate != other.autoDetectPublishDate)
			return false;
		if (callbackUrl == null) {
			if (other.callbackUrl != null)
				return false;
		} else if (!callbackUrl.equals(other.callbackUrl))
			return false;
		if (categoryReg == null) {
			if (other.categoryReg != null)
				return false;
		} else if (!categoryReg.equals(other.categoryReg))
			return false;
		if (categoryXPath == null) {
			if (other.categoryXPath != null)
				return false;
		} else if (!categoryXPath.equals(other.categoryXPath))
			return false;
		if (charset == null) {
			if (other.charset != null)
				return false;
		} else if (!charset.equals(other.charset))
			return false;
		if (contentReg == null) {
			if (other.contentReg != null)
				return false;
		} else if (!contentReg.equals(other.contentReg))
			return false;
		if (contentXPath == null) {
			if (other.contentXPath != null)
				return false;
		} else if (!contentXPath.equals(other.contentXPath))
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (defaultCategory == null) {
			if (other.defaultCategory != null)
				return false;
		} else if (!defaultCategory.equals(other.defaultCategory))
			return false;
		if (doNLP != other.doNLP)
			return false;
		if (domain == null) {
			if (other.domain != null)
				return false;
		} else if (!domain.equals(other.domain))
			return false;
		if (dynamicFields == null) {
			if (other.dynamicFields != null)
				return false;
		} else if (!dynamicFields.equals(other.dynamicFields))
			return false;
		if (gatherFirstPage != other.gatherFirstPage)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (lang == null) {
			if (other.lang != null)
				return false;
		} else if (!lang.equals(other.lang))
			return false;
		if (maxPageGather != other.maxPageGather)
			return false;
		if (needContent != other.needContent)
			return false;
		if (needPublishTime != other.needPublishTime)
			return false;
		if (needTitle != other.needTitle)
			return false;
		if (priority != other.priority)
			return false;
		if (proxyHost == null) {
			if (other.proxyHost != null)
				return false;
		} else if (!proxyHost.equals(other.proxyHost))
			return false;
		if (proxyPassword == null) {
			if (other.proxyPassword != null)
				return false;
		} else if (!proxyPassword.equals(other.proxyPassword))
			return false;
		if (proxyPort != other.proxyPort)
			return false;
		if (proxyUsername == null) {
			if (other.proxyUsername != null)
				return false;
		} else if (!proxyUsername.equals(other.proxyUsername))
			return false;
		if (publishTimeFormat == null) {
			if (other.publishTimeFormat != null)
				return false;
		} else if (!publishTimeFormat.equals(other.publishTimeFormat))
			return false;
		if (publishTimeReg == null) {
			if (other.publishTimeReg != null)
				return false;
		} else if (!publishTimeReg.equals(other.publishTimeReg))
			return false;
		if (publishTimeXPath == null) {
			if (other.publishTimeXPath != null)
				return false;
		} else if (!publishTimeXPath.equals(other.publishTimeXPath))
			return false;
		if (retry != other.retry)
			return false;
		if (saveCapture != other.saveCapture)
			return false;
		if (siteName == null) {
			if (other.siteName != null)
				return false;
		} else if (!siteName.equals(other.siteName))
			return false;
		if (sleep != other.sleep)
			return false;
		if (startUrl == null) {
			if (other.startUrl != null)
				return false;
		} else if (!startUrl.equals(other.startUrl))
			return false;
		if (staticFields == null) {
			if (other.staticFields != null)
				return false;
		} else if (!staticFields.equals(other.staticFields))
			return false;
		if (thread != other.thread)
			return false;
		if (timeout != other.timeout)
			return false;
		if (titleReg == null) {
			if (other.titleReg != null)
				return false;
		} else if (!titleReg.equals(other.titleReg))
			return false;
		if (titleXPath == null) {
			if (other.titleXPath != null)
				return false;
		} else if (!titleXPath.equals(other.titleXPath))
			return false;
		if (urlReg == null) {
			if (other.urlReg != null)
				return false;
		} else if (!urlReg.equals(other.urlReg))
			return false;
		if (userAgent == null) {
			if (other.userAgent != null)
				return false;
		} else if (!userAgent.equals(other.userAgent))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "    {\"thread\":\"" + thread + "\",\"retry\":\"" + retry + "\",\"sleep\":\"" + sleep
				+ "\",\"maxPageGather\":\"" + maxPageGather + "\",\"timeout\":\"" + timeout + "\",\"priority\":\""
				+ priority + "\",\"gatherFirstPage\":\"" + gatherFirstPage + "\",\"id\":\"" + id + "\",\"siteName\":\""
				+ siteName + "\",\"domain\":\"" + domain + "\",\"startUrl\":\"" + startUrl + "\",\"contentReg\":\""
				+ contentReg + "\",\"contentXPath\":\"" + contentXPath + "\",\"titleReg\":\"" + titleReg
				+ "\",\"titleXPath\":\"" + titleXPath + "\",\"categoryReg\":\"" + categoryReg
				+ "\",\"categoryXPath\":\"" + categoryXPath + "\",\"defaultCategory\":\"" + defaultCategory
				+ "\",\"urlReg\":\"" + urlReg + "\",\"charset\":\"" + charset + "\",\"publishTimeXPath\":\""
				+ publishTimeXPath + "\",\"publishTimeReg\":\"" + publishTimeReg + "\",\"publishTimeFormat\":\""
				+ publishTimeFormat + "\",\"callbackURL\":\"" + callbackUrl + "\",\"doNLP\":\"" + doNLP
				+ "\",\"needTitle\":\"" + needTitle + "\",\"needContent\":\"" + needContent
				+ "\",\"needPublishTime\":\"" + needPublishTime + "\",\"dynamicFields\":\"" + dynamicFields
				+ "\",\"staticFields\":\"" + staticFields + "\",\"lang\":\"" + lang + "\",\"country\":\"" + country
				+ "\",\"userAgent\":\"" + userAgent + "\",\"saveCapture\":\"" + saveCapture + "\",\"ajaxSite\":\""
				+ ajaxSite + "\",\"autoDetectPublishDate\":\"" + autoDetectPublishDate + "\",\"proxyHost\":\""
				+ proxyHost + "\",\"proxyPort\":\"" + proxyPort + "\",\"proxyUsername\":\"" + proxyUsername
				+ "\",\"proxyPassword\":\"" + proxyPassword + "\"}  ";
	}



	public class FieldConfig {
    	private String regex;
    	
    	private String xpath;
    	
    	private String name;
    	
    	private boolean need = false;
    	
    	public FieldConfig() {}
    	
    	public FieldConfig(String regex, String xpath, String name, boolean need) {
    		this.regex = regex;
    		this.xpath = xpath;
    		this.name = name;
    		this.need = need;
    	}

		public String getRegex() {
			return regex;
		}

		public FieldConfig setRegex(String regex) {
			this.regex = regex;
			return this;
		}

		public String getXpath() {
			return xpath;
		}

		public FieldConfig setXpath(String xpath) {
			this.xpath = xpath;
			return this;
		}

		public String getName() {
			return name;
		}

		public FieldConfig setName(String name) {
			this.name = name;
			return this;
		}

		public boolean isNeed() {
			return need;
		}

		public FieldConfig setNeed(boolean need) {
			this.need = need;
			return this;
		}
    	
    	
    }
    
    public class StaticField {
    	private String name;
    	
    	private String value;
    	
    	public StaticField(String name, String value) {
    		this.name = name;
    		this.value = value;
    	}

		public String getName() {
			return name;
		}

		public StaticField setName(String name) {
			this.name = name;
			return this;
		}

		public String getValue() {
			return value;
		}

		public StaticField setValue(String value) {
			this.value = value;
			return this;
		}
    	
    }
}
