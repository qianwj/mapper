package cn.nest.spider.entity.commons;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.annotations.SerializedName;

public class Webpage {

	/**
     * 附件id列表
     */
    public List<String> attachmentList;
    /**
     * 图片ID列表
     */
    public List<String> imageList;
    /**
     * 正文
     */
    private String content;
    /**
     * 标题
     */
    private String title;
    /**
     * 链接
     */
    private String url;
    /**
     * 域名
     */
    private String domain;
    /**
     * 爬虫id,可以认为是taskid
     */
    private String spiderUUID;
    /**
     * 模板id
     */
    @SerializedName("spiderInfoId")
    private String spiderInfoId;
    /**
     * 分类
     */
    private String category;
    /**
     * 网页快照
     */
    private String rawHTML;
    /**
     * 关键词
     */
    private List<String> keywords;
    /**
     * 摘要
     */
    private List<String> summary;
    /**
     * 抓取时间
     */
    @SerializedName("gatherTime")
    private Date gathertime;
    /**
     * 网页id,es自动分配的
     */
    private String id;
    /**
     * 文章的发布时间
     */
    private Date publishTime;
    /**
     * 命名实体
     */
    private Map<String, Set<String>> namedEntity;
    /**
     * 动态字段
     */
    private Map<String, Object> dynamicFields;
    /**
     * 静态字段
     */
    private Map<String, Object> staticFields;
    /**
     * 本网页处理时长
     */
    private long processTime;
    
	public List<String> getAttachmentList() {
		return attachmentList;
	}
	public Webpage setAttachmentList(List<String> attachmentList) {
		this.attachmentList = attachmentList;
		return this;
	}
	public List<String> getImageList() {
		return imageList;
	}
	public Webpage setImageList(List<String> imageList) {
		this.imageList = imageList;
		return this;
	}
	public String getContent() {
		return content;
	}
	public Webpage setContent(String content) {
		this.content = content;
		return this;
	}
	public String getTitle() {
		return title;
	}
	public Webpage setTitle(String title) {
		this.title = title;
		return this;
	}
	public String getUrl() {
		return url;
	}
	public Webpage setUrl(String url) {
		this.url = url;
		return this;
	}
	public String getDomain() {
		return domain;
	}
	public Webpage setDomain(String domain) {
		this.domain = domain;
		return this;
	}
	public String getSpiderUUID() {
		return spiderUUID;
	}
	public Webpage setSpiderUUID(String spiderUUID) {
		this.spiderUUID = spiderUUID;
		return this;
	}
	public String getSpiderInfoId() {
		return spiderInfoId;
	}
	public Webpage setSpiderInfoId(String spiderInfoId) {
		this.spiderInfoId = spiderInfoId;
		return this;
	}
	public String getCategory() {
		return category;
	}
	public Webpage setCategory(String category) {
		this.category = category;
		return this;
	}
	public String getRawHTML() {
		return rawHTML;
	}
	public Webpage setRawHTML(String rawHTML) {
		this.rawHTML = rawHTML;
		return this;
	}
	public List<String> getKeywords() {
		return keywords;
	}
	public Webpage setKeywords(List<String> keywords) {
		this.keywords = keywords;
		return this;
	}
	public List<String> getSummary() {
		return summary;
	}
	public Webpage setSummary(List<String> summary) {
		this.summary = summary;
		return this;
	}
	public Date getGathertime() {
		return gathertime;
	}
	public Webpage setGathertime(Date gathertime) {
		this.gathertime = gathertime;
		return this;
	}
	public String getId() {
		return id;
	}
	public Webpage setId(String id) {
		this.id = id;
		return this;
	}
	public Date getPublishTime() {
		return publishTime;
	}
	public Webpage setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
		return this;
	}
	public Map<String, Set<String>> getNamedEntity() {
		return namedEntity;
	}
	public Webpage setNamedEntity(Map<String, Set<String>> namedEntity) {
		this.namedEntity = namedEntity;
		return this;
	}
	public Map<String, Object> getDynamicFields() {
		return dynamicFields;
	}
	public Webpage setDynamicFields(Map<String, Object> dynamicFields) {
		this.dynamicFields = dynamicFields;
		return this;
	}
	public Map<String, Object> getStaticFields() {
		return staticFields;
	}
	public Webpage setStaticFields(Map<String, Object> staticFields) {
		this.staticFields = staticFields;
		return this;
	}
	public long getProcessTime() {
		return processTime;
	}
	public Webpage setProcessTime(long processTime) {
		this.processTime = processTime;
		return this;
	}
    
    
}
