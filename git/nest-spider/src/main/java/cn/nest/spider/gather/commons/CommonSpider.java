package cn.nest.spider.gather.commons;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.bag.SynchronizedSortedBag;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Preconditions;

import cn.nest.spider.dao.commons.SpiderInfoDAO;
import cn.nest.spider.dao.commons.WebpageDAO;
import cn.nest.spider.dao.pipeline.WebpagePipeline;
import cn.nest.spider.entity.async.Task;
import cn.nest.spider.entity.async.TaskManager;
import cn.nest.spider.entity.commons.SpiderInfo;
import cn.nest.spider.entity.commons.Webpage;
import cn.nest.spider.entity.util.State;
import cn.nest.spider.gather.async.AsyncGather;
import cn.nest.spider.gather.downloader.CasperjsDownloader;
import cn.nest.spider.gather.downloader.ContentDownloader;
import cn.nest.spider.util.NLPExtractor;
import cn.nest.spider.util.StaticValue;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.Spider.Status;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.pipeline.ResultItemsCollectorPipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.PlainText;
import us.codecraft.webmagic.utils.UrlUtils;

public class CommonSpider extends AsyncGather {

	private static final Logger LOG = LogManager.getLogger(CommonSpider.class);
	
	private static final String LINK_KEY = "LINK_LIST";
	
	private static final String DYNAMIC_FIELD = "dynamic_fields";
	
	private static final String SPIDER_INFO = "spiderInfo";
	
	private static List<String> ignoredUrls;
	
	private StaticValue sValue;
	
	private static List<Pair<String, SimpleDateFormat>> datePattern = new LinkedList<>();
	
	private static Map<String, NestSpider> spiderMap = new HashMap<>();
	
	@Resource(name = "hanNLPExtractor")
	private NLPExtractor keywordsExtractor;
	
	@Resource(name = "hanNLPExtractor")
    private NLPExtractor summaryExtractor;
    
	@Resource(name = "hanNLPExtractor")
    private NLPExtractor namedEntitiesExtractor;
    
    @SuppressWarnings("unchecked")
	private final PageConsumer spiderInfoPageConsumer = (page, info, task) -> {
	    LOG.info("Page consumer starting...");
    	try {
    		long start = System.currentTimeMillis();
    		final boolean startPage = info.getStartUrl().contains(page.getUrl().get());
    		//List<String> attachmentList = new LinkedList<>();
    		String urlReg = info.getUrlReg();
    		//判断是否只抽取入口页
    		if(!info.isGatherFirstPage() || (info.isGatherFirstPage() && startPage)) {
    			List<String> links = null;
    			//probleam here, needs to fix the regex
    			System.out.println(info.getUrlReg());
    			if(StringUtils.isNotBlank(urlReg)) {
    				System.out.println(page.getHtml().links().regex(urlReg).all().toString());
    				links = page.getHtml().links().regex(urlReg).all().stream().map(str -> {
    					int indexOfSharp = str.indexOf("#");
    					return str.substring(0, indexOfSharp == -1? str.length():indexOfSharp);
    				}).collect(Collectors.toList());
    				System.out.println(links.toString());
    			} else {//如果该域名下正则为空，则抽取全部链接并使用黑名单进行过滤
    				links = page.getHtml().links().regex("https?://" + info.getDomain().replace(".", "\\." ) + "/.*")
    						.all().stream().map(str -> {
    							int indexOfSharp = str.indexOf("#");
    	    					return str.substring(0, indexOfSharp == -1? str.length():indexOfSharp);
    						}).filter(url -> {
    							for(String suffix:ignoredUrls) {
    								if(url.toLowerCase().endsWith(suffix)) 
    									return false;
    							}
    							return true;
    						}).collect(Collectors.toList());
    			}
    			
    			for(Element iframe:page.getHtml().getDocument().getElementsByTag("iframe")) {
    				final String src = iframe.attr("src");
    				if(StringUtils.isNotBlank(urlReg) && src.matches(urlReg)) 
    					links.add(src);
    				else if(StringUtils.isBlank(urlReg) && UrlUtils.getDomain(src).equals(info.getDomain()))
    					links.add(src);
    			}
    			
    			if(links != null && links.size() > 0) {
    				page.addTargetRequests(links);
    				if(sValue.isSpiderDebug()) {
    					List<String> urls;
    					if((urls = (List<String>)task.getExtraInfoByKey(LINK_KEY))!=null) 
    						urls.addAll(links);
    					else
    						task.addExtraInfo(LINK_KEY, links);
    				}
    			}
    		}
    		
    		
    		//抽取之后去掉startpage页面
    		if(startPage)
    			page.setSkip(true);
    		//LOG.info("Start page has been skipped, start to put field in page");
    		
    		
    		page.putField("url", page.getUrl().get());
    		page.putField("domain", info.getDomain());
            page.putField("spiderInfoId", info.getId());
            page.putField("gatherTime", new Date());
            page.putField("spiderInfo", info);
            page.putField("spiderUUID", task.getTaskId());
            if(info.isSaveCapture()) 
            	page.putField("rawHtml", page.getHtml().get());
            
            //LOG.info("static fields extractor: starting...");
            if(info.getStaticFields() != null && info.getStaticFields().size()>0) {
            	Map<String, String> fields = new HashMap<>();
            	for(SpiderInfo.StaticField field:info.getStaticFields()) 
            		fields.put(field.getName(), field.getValue());
            	page.putField("staticField", fields);
            }
            
            //抽取动态字段
            //LOG.info("dynamic fields extractor: starting...");
            Map<String, Object> dynamicFields = new HashMap<>();
            for (SpiderInfo.FieldConfig conf : info.getDynamicFields()) {
                String fieldName = conf.getName();
                String fieldData = null;
                if (!StringUtils.isBlank(conf.getXpath())) {//提取
                    fieldData = page.getHtml().xpath(conf.getXpath()).get();
                } else if (!StringUtils.isBlank(conf.getRegex())) {
                    fieldData = page.getHtml().regex(conf.getRegex()).get();
                }
                dynamicFields.put(fieldName, fieldData);
                if (conf.isNeed() && StringUtils.isBlank(fieldData)) {
                    page.setSkip(true);
                    return;
                }
            }
            page.putField(DYNAMIC_FIELD, dynamicFields);
            
            //LOG.info("content extractor: starting...");
            String content;
            StringBuffer buffer = new StringBuffer();
            //优先使用XPath
            if(StringUtils.isNotBlank(info.getContentXPath())) {
            	buffer.delete(0, buffer.length());
            	page.getHtml().xpath(info.getContentXPath()).all().forEach(buffer::append);
            	content = buffer.toString();
            } else if(StringUtils.isNotBlank(info.getContentReg())) {
            	buffer.delete(0, buffer.length());
            	page.getHtml().regex(info.getContentReg()).all().forEach(buffer::append);
            	content = buffer.toString();
            } else {//如果都没有就使用智能提取
            	Document clone = page.getHtml().getDocument().clone();
            	clone.getElementsByTag("p").append("***");
            	clone.getElementsByTag("br").append("***");
            	clone.getElementsByTag("script").remove();
            	clone.getElementsByAttributeValueContaining("style", "display:none").remove();
            	content = new Html(clone).smartContent().get();
            }
            //去掉所有脚本和样式，标签，恢复页面中的文本原状
            content.replaceAll("<script([\\s\\S]*?)</script>", "");
            content.replaceAll("<style([\\s\\S]*?)</style>", "");
            content = content.replace("</p>", "***");
            content = content.replace("<BR>", "***");
            content = content.replaceAll("<([\\s\\S]*?)>", "");
            content = content.replace("***", "<br/>");
            content = content.replace("\n", "<br/>");
            content = content.replaceAll("(\\<br/\\>\\s*){2,}", "<br/> ");
            content = content.replaceAll("(&nbsp;\\s*)+", " ");
            page.putField("content", content);
            //如果content为空，跳过它
            if(info.isNeedContent() && StringUtils.isBlank(content)) {
            	page.setSkip(true);
            	return;
            }
            
            //LOG.info("title extractor: starting...");
            //抽取标题
            String title = null;
            if (!StringUtils.isBlank(info.getTitleXPath())) //提取网页标题
                 title = page.getHtml().xpath(info.getTitleXPath()).get();
            else if (!StringUtils.isBlank(info.getTitleReg())) 
                 title = page.getHtml().regex(info.getTitleReg()).get();
            else //如果不写默认是title
                 title = page.getHtml().getDocument().title();
            page.putField("title", title);
            if (info.isNeedTitle() && StringUtils.isBlank(title)) {
                page.setSkip(true);
                return;
            }
            
            //抽取分类
            //LOG.info("category extractor: starting...");
            String category = null;
            if(StringUtils.isNotBlank(info.getCategoryXPath()))
            	category = page.getHtml().xpath(info.getCategoryXPath()).get();
            else if(StringUtils.isNotBlank(info.getCategoryReg()))
            	category = page.getHtml().regex(info.getCategoryReg()).get();
            if(StringUtils.isBlank(category))
            	category = info.getDefaultCategory();
            page.putField("category", category);
            
            //抽取发布时间
            //LOG.info("publish time extractor: starting...");
            String publishTime = null;
            if (!StringUtils.isBlank(info.getPublishTimeXPath())) //文章发布时间规则
                publishTime = page.getHtml().xpath(info.getPublishTimeXPath()).get();
            else if (!StringUtils.isBlank(info.getPublishTimeReg())) 
                publishTime = page.getHtml().regex(info.getPublishTimeReg()).get();
            
            SimpleDateFormat sdf = null;
            if(!StringUtils.isBlank(info.getPublishTimeFormat()))
            	if(StringUtils.isNotBlank(info.getLang()))
            		sdf = new SimpleDateFormat(info.getPublishTimeFormat(), new Locale(info.getLang(), info.getCountry()));
                else
            	    sdf = new SimpleDateFormat(info.getPublishTimeFormat());
            else if(StringUtils.isBlank(publishTime) && info.isAutoDetectPublishDate())
            	for(Pair<String, SimpleDateFormat> formatEntry:datePattern) {
            		publishTime = page.getHtml().regex(formatEntry.getKey(), 0).get();
            		if(StringUtils.isNotBlank(publishTime)) {
            			sdf = formatEntry.getValue();
            			break;
            		}
            	}
            
            //解析时间开始
            Date publishDate = null;
            if(StringUtils.isNotBlank(publishTime) && sdf != null) {
            	try {
            		publishDate = sdf.parse(publishTime);
            		if(!sdf.toPattern().contains("yyyy")) {
            			Calendar cal = Calendar.getInstance();
            			 cal.setTime(publishDate);
            			 cal.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
            			 publishDate = cal.getTime();
            		}
            		page.putField("publishTime", publishDate);
            	} catch(ParseException e) {
            		LOG.debug("解析文章时间出错， source:" + publishTime + ", format:" + sdf.toPattern());
            		task.setDescription("解析文章时间出错， url:%s, source:%s, format:%s", page.getUrl().get(), publishTime, sdf.toPattern());
            	    if(info.isNeedPublishTime()) {
            	    	page.setSkip(true);
            	    	return;
            	    }
            	}
            } else if(info.isNeedPublishTime()) {
            	page.setSkip(true);
            	return;
            }
            
            //自然语言处理开始
            //LOG.info("nlp extractor: starting...");
            if(info.isDoNLP()) {
            	String contentWithoutTag = content.replaceAll("<br/>", "");
            	try {
                    //抽取关键词,10个词
                    page.putField("keywords", keywordsExtractor.extractKeyWords(contentWithoutTag));
                    //抽取摘要,5句话
                    page.putField("summary", summaryExtractor.extractSummary(contentWithoutTag));
                    //抽取命名实体
                    page.putField("namedEntity", namedEntitiesExtractor.extractNameEntity(contentWithoutTag));
                } catch (Exception e) {
                    e.printStackTrace();
                    LOG.error("对网页进行NLP处理失败, 由于 " + e.getLocalizedMessage());
                    task.setDescription("对网页进行NLP处理失败,%s", e.getLocalizedMessage());
                }
            }
            
            //页面处理时长
            page.putField("processTime", System.currentTimeMillis() - start);
            
            LOG.info("page consumer: done.");
    	} catch(Exception ex) {
    		LOG.error("网页处理出错");
    		task.setDescription("网页处理出错， %s", ex.toString());
    	}
    };
    
    @Resource(name = "webpageDAO")
    private WebpageDAO dao;
    
    @Resource(name = "spiderInfoDAO")
    private SpiderInfoDAO infoDao;
    
    @Autowired
    private WebpagePipeline pipeline;
    
    @Autowired
    private CasperjsDownloader jsDownloader;
    
    @Autowired
    private ContentDownloader contentDownloader;
    
    @Autowired
    private List<Pipeline> pipelines;
    
    private int progress;
	
	static {
		try {
			ignoredUrls = FileUtils.readLines(new File(
					CommonSpider.class.getClassLoader().getResource("ignore_urls.txt").getFile()));
			LOG.info("加载爬虫忽略名单成功， 忽略名单：" + ignoredUrls.toString());
			try {
				String[] dataPatterns = FileUtils.readFileToString(new File(
						CommonSpider.class.getClassLoader().getResource("date_pattern.txt").getFile())
				).replace("\r", "").split("=====\r?\n");
				String[] dates = dataPatterns[0].split("\n");
				String[] times = dataPatterns[1].split("\n");
				for(String date:dates) {
					String[] entry = date.split("##");
					String dateReg = entry[0];
					String dateFormat = entry[1];
					LOG.debug("正在编译日期正则 " + dateReg + ", format:" + dateFormat);
					datePattern.add(Pair.of(dateReg, new SimpleDateFormat(dateFormat)));
					for(String time:times) {
						String[] timeEntry = time.split("##");
						String timeReg = timeEntry[0];
						String timeFormat = timeEntry[1];
						LOG.debug("正在编译日期正则 " + dateReg + "" + timeReg + ", format:" + dateReg + "" + timeFormat);
						datePattern.add(Pair.of(dateReg + " " + timeReg, new SimpleDateFormat(dateFormat + " " + timeFormat)));
						LOG.debug("正在编译日期正则 " + dateReg + timeReg + ", format:" + dateReg + timeFormat);
						datePattern.add(Pair.of(dateReg + timeReg, new SimpleDateFormat(dateFormat + timeFormat)));
					}
				}
				datePattern.sort((p1, p2) -> p2.getLeft().length() - p1.getLeft().length());
				LOG.info("日期匹配加载完成");
			} catch(IOException e) {
				LOG.error("日期匹配加载失败， 由于 " + e.getLocalizedMessage());
			}
		} catch(IOException e) {
			LOG.error("加载忽略名单失败， 由于 " + e.getLocalizedMessage());
		}
	}
	
	@Autowired
	public CommonSpider(TaskManager manager, StaticValue sValue) {
		this.manager = manager;
		this.sValue = sValue;
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				delAll();
				LOG.debug("定时删除全部完成的普通网页抓取任务");
			}
		}, sValue.getDeleteTaskDelay() * 3600000, sValue.getDeleteTaskPeriod() * 3600000);
		LOG.debug("定时删除普通网页抓取任务记录线程已启动,延时:"+ sValue.getDeleteTaskDelay() + "小时,每" + sValue.getDeleteTaskPeriod() + "小时删除一次");
	}
	
	public List<Webpage> testSpiderInfo(SpiderInfo info) {
		final ResultItemsCollectorPipeline ricp = new ResultItemsCollectorPipeline();
		final String uuid = UUID.randomUUID().toString();
		Task task = manager.initTask(uuid, info.getDomain(), info.getCallbackUrl(), "spiderInfoId=" + info.getId() + "&spiderUUID=" + uuid);
		task.addExtraInfo("spiderInfo", info);
		QueueScheduler scheduler = new QueueScheduler();
		NestSpider spider = makeSpider(info, task);
		spider.addPipeline(ricp).setScheduler(scheduler);
		if(info.isAjaxSite() && StringUtils.isNotBlank(sValue.getAjaxDownloader()))
			spider.setDownloader(jsDownloader);
		else
			spider.setDownloader(contentDownloader);
		spider.startUrls(info.getStartUrl());
		spiderMap.put(uuid, spider);
		manager.findTask(uuid).setState(State.RUNNING);
		spider.run();
		List<Webpage> list = new LinkedList<>();
		ricp.getCollected().forEach(items -> {
			Webpage page = WebpagePipeline.convertToWebpage(items);
			if(page != null)
				list.add(page);
		});
		if(list.size() > 0) 
			return list;
		else
			return new ArrayList<>();
	}
	
	public void delete(String uuid) {
		Preconditions.checkArgument(spiderMap.containsKey(uuid) || spiderMap.get(uuid) != null, "找不到uuid为%s的爬虫", uuid);
		Preconditions.checkArgument(manager.findTask(uuid).getState() == State.STOP, "爬虫%s尚未停止， 不能删除任务", uuid);
		delTask(uuid);
		spiderMap.remove(uuid);
	}
	
	/**
	 * 删除页面后返回该任务的uuid
	 * @param domain
	 * @return uuid
	 */
	public String delByDomain(String domain) {
		final String uuid = UUID.randomUUID().toString();
		Task task = manager.initTask(uuid, "Delete by domain:" + domain, new ArrayList<>(), null);
		Thread thread = new Thread(() -> {
			task.setState(State.RUNNING);
			try {
				dao.delByDomain(domain, task);
			} catch(Exception e) {
				task.setDescription("删除任务时发生异常， 由于:%s", e.getLocalizedMessage());
			} finally {
				task.setState(State.STOP);
			}
			
		}, "delete-webpage-thread-" + domain) ;
		thread.start();
		return uuid;
	}
	
	public void delAll() {
		List<String> removedSpider = spiderMap.entrySet().stream()
				.filter(entry -> entry.getValue().getStatus() == Status.Stopped)
				.map(Map.Entry::getKey).collect(Collectors.toList());
		for(String uuid:removedSpider) {
			try {
				delTask(uuid);
				spiderMap.remove(uuid);
			} catch(Exception e) {
				LOG.error("删除任务出错， 由于 " + e.getLocalizedMessage());
			}
		}
		manager.delTasksByState(State.STOP);
	}
	
	public String updateBySpiderInfoId(String spiderInfoId, SpiderInfo info, List<String> callbackUrls) {
		Preconditions.checkArgument(StringUtils.isNotBlank(info.getId()), "网站模板必须包含id");
		SpiderInfo inDataBase = infoDao.findById(spiderInfoId);
		Preconditions.checkArgument(inDataBase!=null, "网页模板必须存在于模板库中");
		final String uuid = UUID.randomUUID().toString();
		Task task = manager.initTask(uuid, "Update by spiderinfo id:" + spiderInfoId, callbackUrls, "spiderInfo ID:" + spiderInfoId);
		Thread thread = new Thread(() -> {
			task.setState(State.RUNNING);
			try {
				Pair<String, List<Webpage>> pair = 
						dao.startScroll(QueryBuilders.matchQuery("spiderInfoId", spiderInfoId).operator(Operator.AND), 50);
				int scrollPage = 0;
				List<Webpage> list = pair.getRight();
				while(list.size() > 0) {
					List<Webpage> webpageList = new ArrayList<>();
					for(Webpage seed:list) {
						try {
							Page page = new Page();
							page.setRequest(new Request(seed.getUrl()));
							page.setRawText(seed.getRawHTML());
							page.setUrl(new PlainText(seed.getUrl()));
							spiderInfoPageConsumer.accept(page, info, task);
							webpageList.add(WebpagePipeline.convertToWebpage(page.getResultItems()));
						} catch(Exception e) {
							LOG.error("应用模板时发生异常， 模板ID:" + spiderInfoId + ", 由于 " + e.getLocalizedMessage());
						} finally {
							task.increaseCount();
						}
					}
					//更新数据
					boolean hasFail = dao.update(webpageList);
					task.setDescription("已更新%s页数据， 错误:%s", ++scrollPage, hasFail);
					list = dao.ScrollAllWebpage(pair.getLeft());
				}
			} catch(Exception e) {
				task.setDescription("根据spiderinfoID更新数据时发生异常%s", e.toString() + e.getLocalizedMessage());
			} finally {
				task.setState(State.STOP);
				manager.stopTask(task);
			}
		});
		thread.start();
		return uuid;
	}
	
	public NestSpider getSpider(String uuid) {
		Preconditions.checkArgument(spiderMap.containsKey(uuid), "找不到uuid为%s的爬虫", uuid);
		return spiderMap.get(uuid);
	}
	
	/**
	 * 列出所有爬虫的运行时信息
	 * @param extra
	 * @return
	 */
	public Map<String, Map<Object, Object>> listAllSpiders(boolean extra) {
		Map<String, Map<Object, Object>> result = new HashMap<>();
		spiderMap.entrySet().forEach(entry -> {
			result.put(entry.getKey(), makeSpiderRuntimeInfo(entry.getValue(), extra));
		});
		return result;
	}
	
	/**
	 * 启动爬虫
	 * @param info
	 * @return
	 */
	public String start(SpiderInfo info) {
		boolean running = manager.findTask(task -> {
			Object spiderInfoObj = task.getExtraInfoByKey(SPIDER_INFO);
			if(spiderInfoObj != null && spiderInfoObj instanceof SpiderInfo) {
				SpiderInfo spiderInfo = (SpiderInfo) spiderInfoObj;
				return task.getState() == State.RUNNING && spiderInfo.getId().equals(info.getId());
			}
			return false;
		});
		Preconditions.checkArgument(!running, "此任务已提交， 请勿重复提交， 模板编号：" + info.getId());
		final String uuid = UUID.randomUUID().toString();
		Task task = manager.initTask(uuid, info.getDomain(), info.getCallbackUrl(), "spiderInfoId=" + info.getId() + "&spiderUUID=" + uuid);
		task.addExtraInfo(SPIDER_INFO, info);
		QueueScheduler scheduler = new QueueScheduler() {
			@Override
			public void pushWhenNoDuplicate(Request request, us.codecraft.webmagic.Task task) {
				int left = getLeftRequestsCount(task);
				if(left <= sValue.getLimitOfDownloadQueue())
					super.pushWhenNoDuplicate(request, task);
			}
			
		};
		if(sValue.isNeedES()) 
			scheduler.setDuplicateRemover(pipeline);
		NestSpider spider = makeSpider(info, task);
		spider.setScheduler(scheduler);
		//if(pipelines != null && pipelines.size() > 0) 
		pipelines.forEach(spider::addPipeline);
		info.getStartUrl().forEach(url -> scheduler.pushWhenNoDuplicate(new Request(url), spider));
		spiderMap.put(uuid, spider);
		spider.start();
		manager.findTask(uuid).setState(State.RUNNING);
		return uuid;
	}
	
	public void stop(String uuid) {
		Preconditions.checkArgument(spiderMap.containsKey(uuid), "找不到uuid为%s的爬虫", uuid);
		spiderMap.get(uuid).close();
		spiderMap.get(uuid).stop();
		manager.findTask(uuid).setState(State.STOP);
	}
	
	public  Map<Object, Object> getSpiderRuntimeInfo(String uuid, boolean extra) {
		return makeSpiderRuntimeInfo(spiderMap.get(uuid), extra);
	}
	
	/**
     * 获取忽略url黑名单
     *
     * @return
     */
    public List<String> getIgnoredUrls() {
        return ignoredUrls;
    }

    /**
     * 添加忽略url黑名单
     *
     * @param postfix
     */
    public void addIgnoredUrl(String postfix) {
        Preconditions.checkArgument(!ignoredUrls.contains(postfix), "已包含这个url后缀请勿重复添加");
        ignoredUrls.add(postfix);
    }
	
	public NLPExtractor getKeywordsExtractor() {
        return keywordsExtractor;
    }

    public void setKeywordsExtractor(NLPExtractor keywordsExtractor) {
        this.keywordsExtractor = keywordsExtractor;
    }

    public NLPExtractor getSummaryExtractor() {
        return summaryExtractor;
    }

    public void setSummaryExtractor(NLPExtractor summaryExtractor) {
        this.summaryExtractor = summaryExtractor;
    }

    public NLPExtractor getNamedEntitiesExtractor() {
        return namedEntitiesExtractor;
    }

    public CommonSpider setNamedEntitiesExtractor(NLPExtractor namedEntitiesExtractor) {
        this.namedEntitiesExtractor = namedEntitiesExtractor;
        return this;
    }

    public ContentDownloader getContentDownloader() {
        return contentDownloader;
    }

    public CommonSpider setContentDownloader(ContentDownloader contentDownloader) {
        this.contentDownloader = contentDownloader;
        return this;
    }

    public List<Pipeline> getPipelines() {
        return pipelines;
    }

    public CommonSpider setPipelines(List<Pipeline> pipelines) {
        this.pipelines = pipelines;
        return this;
    }

    public CasperjsDownloader getJsDownloader() {
        return jsDownloader;
    }

    public CommonSpider setJsDownloader(CasperjsDownloader jsDownloader) {
        this.jsDownloader = jsDownloader;
        return this;
    }
		
	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	private Map<Object, Object> makeSpiderRuntimeInfo(NestSpider spider, boolean extra) {
		Map<Object, Object> infoMap = new HashMap<>();
		infoMap.put("Page Count", spider.getPageCount());
	    infoMap.put("StartTime", spider.getStartTime());
        infoMap.put("ThreadAlive", spider.getThreadAlive());
        infoMap.put("Status", spider.getStatus());
        infoMap.put("SpiderInfo", spider.getSpiderInfo());
        if(extra)
        	infoMap.put("Links", findTask(spider.getUUID(), true).getExtraInfoByKey(LINK_KEY));
        return infoMap;
	}
	
	private NestSpider makeSpider(SpiderInfo info, Task task) {
		NestSpider spider = new NestSpider(new NestPageProcessor(info, task), info);
		spider.thread(info.getThread()).setUUID(task.getTaskId());
		if(info.isAjaxSite() && StringUtils.isNotBlank(sValue.getAjaxDownloader()))
			spider.setDownloader(jsDownloader);
		else
			spider.setDownloader(contentDownloader);
		return spider;
	}
	
	private class NestSpider extends Spider {
		
		private final SpiderInfo SPIDER_INFO;
		
		public NestSpider(PageProcessor pageProcessor, SpiderInfo info) {
			super(pageProcessor);
			this.SPIDER_INFO = info;
		}

		@Override
		protected void onSuccess(Request request) {
			super.onSuccess(request);
			Task task = manager.findTask(this.getUUID());
			boolean reachMax = false;
			boolean exceedRatio = false;
			if(
					(
							//已抓取数量大于最大抓取数量，退出
							(reachMax = SPIDER_INFO.getMaxPageGather() > 0 && task.getCount() >= SPIDER_INFO.getMaxPageGather())
					         ||
					         //如果抓取页面数超过最大抓取数量ratio倍的时候，也退出
					        (exceedRatio = (this.getPageCount() > SPIDER_INFO.getMaxPageGather()*sValue.getCrawlRatio() && SPIDER_INFO.getMaxPageGather() > 0))
			        )
					&& this.getStatus() == Status.Running) {
				LOG.info(
						"爬虫" + this.getUUID() + "已处理的页面数：" + this.getPageCount() 
						+ ", 有效页面：" + task.getCount() 
						+ ", 最大抓取数量：" + SPIDER_INFO.getMaxPageGather()
						+ ",达到最大？" + reachMax
						+ ", 超出比率：" + exceedRatio);
				task.setDescription(
						"爬虫ID%s已处理%s个页面,有效页面%s个,达到最大抓取页数%s,reachMax=%s,exceedRatio=%s,退出.", 
						this.getUUID(), this.getPageCount(), task.getCount(), 
						SPIDER_INFO.getMaxPageGather(), reachMax, exceedRatio);
				this.stop();
			}
		}

		@Override
		protected void onError(Request request) {
			super.onError(request);
			Task task = manager.findTask(this.getUUID());
			task.setDescription(" 处理网页%s时发生错误, %s", request.getUrl(), request.getExtras());
		}

		@Override
		public void close() {
			super.close();
			Task task = manager.findTask(this.getUUID());
			if(task != null) {
				pipeline.deleteUrls(task.getTaskId());
				manager.stopTask(task);
			}
		}

		@Override
		public boolean equals(Object obj) {
			if(this == obj) 
				return true;
			if(obj == null ||this.getClass()!=obj.getClass())
				return false;
			NestSpider spider = (NestSpider)obj;
			if(spider.getUUID() == this.uuid && this.uuid != null)
				return true;
			else
				return false;
		}

		@Override
		public int hashCode() {
			return new HashCodeBuilder().append(this.uuid).hashCode();
		}

		public SpiderInfo getSpiderInfo() {
			return SPIDER_INFO;
		}
	}	
	private class NestPageProcessor implements PageProcessor {
		
		private Site site;
		
		private SpiderInfo info;
		
		private Task task;
		
		public NestPageProcessor(SpiderInfo info, Task task) {
			this.site = Site.me().setDomain(info.getDomain()).setTimeOut(info.getTimeout())
					.setRetryTimes(info.getRetry()).setSleepTime(info.getSleep())
					.setCharset(StringUtils.isBlank(info.getCharset())? null:info.getCharset())
					.setUserAgent(info.getUserAgent());
			//设置抓取代理IP与接口
            if (StringUtils.isNotBlank(info.getProxyHost()) && info.getProxyPort() > 0) {
                contentDownloader.setProxyProvider(SimpleProxyProvider.from(new Proxy(info.getProxyHost(),info.getProxyPort())));
                //设置代理的认证
                if (StringUtils.isNotBlank(info.getProxyUsername()) && StringUtils.isNotBlank(info.getProxyPassword())) {
                    contentDownloader.setProxyProvider(SimpleProxyProvider.from(new Proxy(info.getProxyHost(), info.getProxyPort(), info.getProxyUsername(), info.getProxyPassword())));
                }
            }
            
            this.info = info;
            this.task = task;
		}

		@Override
		public void process(Page page) {
			spiderInfoPageConsumer.accept(page, info, task);
			progress++;
			if(!page.getResultItems().isSkip())
				task.increaseCount();
		}

		@Override
		public Site getSite() {
			return site;
		}
		
	}
 }
