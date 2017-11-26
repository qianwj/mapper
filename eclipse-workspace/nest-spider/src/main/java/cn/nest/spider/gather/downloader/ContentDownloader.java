package cn.nest.spider.gather.downloader;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.nest.spider.util.StaticValue;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.downloader.HttpClientGenerator;
import us.codecraft.webmagic.utils.UrlUtils;

@Component
public class ContentDownloader extends HttpClientDownloader {

	private static final Logger LOG = LogManager.getLogger(ContentDownloader.class);
	
	private final Map<String, CloseableHttpClient> clients = new HashMap<>();
	
	private HttpClientGenerator generator = new HttpClientGenerator();
	
	@Autowired
	private StaticValue value;
	
	protected String getContent(String charset, HttpResponse response) throws IOException {
		if(charset == null) {
			long contentLength = response.getEntity().getContentLength();
			if(response.getFirstHeader("Content-Type") != null 
					&& !response.getFirstHeader("Content-Type").getValue().toLowerCase().contains("text/html")) 
				throw new IllegalArgumentException("此链接为非html内容，不下载，内容类型：" + response.getFirstHeader("Content-Type"));
			else if(contentLength>value.getMaxDownloadLength())
				throw new IllegalArgumentException("网页内容长度超过最大限制，要求最大长度：" + value.getMaxDownloadLength() + "，实际长度：" + contentLength);
			byte[] contentBytes = IOUtils.toByteArray(response.getEntity().getContent());
			String htmlCharset = UrlUtils.getCharset(response.getEntity().getContentType().getValue());
			if (htmlCharset != null) {
                return new String(contentBytes, htmlCharset);
            } else {
                LOG.warn("自动探测字符集失败, 使用 {} 作为字符集。请在Site.setCharset()指定字符集", Charset.defaultCharset());
                return new String(contentBytes);
            }
		} else 
			return IOUtils.toString(response.getEntity().getContent(), charset);
	}

	@Override
	protected Page handleResponse(Request request, String charset, HttpResponse httpResponse, Task task)
			throws IOException {
		Page page = null;
		try {
			page = super.handleResponse(request, charset, httpResponse, task);
		} catch(IllegalArgumentException e) {
			LOG.warn("响应处理异常， url:{}, 由于 {}", request.getUrl(), e.getLocalizedMessage());
			request.putExtra("EXCEPTION", e);
			onError(request);
			throw e;
		}
		return page;
	}

	@Override
	public Page download(Request request, Task task) {
		Site site = task.getSite();
		String charset = null;
		if(site!=null) 
			charset = site.getCharset();
		LOG.info("Downloading page {} ...", request.getUrl());
		CloseableHttpResponse response = null;
		int statusCode = 0;
		try {
			response = getHttpClient(site).execute(new HttpGet(request.getUrl()));
			statusCode = response.getStatusLine().getStatusCode();
			request.putExtra("STATUS_CODE", statusCode);
			if(statusCode == 200) {
				Page page = handleResponse(request, charset, response, task);
				LOG.info("Page {} downloaded, done.", request.getUrl());
	            onSuccess(request);
	            return page;
			} else {
				LOG.warn("get page {} error, status code {}", request.getUrl(), statusCode);
                return null;
			}
		} catch (IOException e) {
			LOG.error("下载响应异常， 由于 " + e.getLocalizedMessage());
			onError(request);
			return null;
		} finally {
			request.putExtra("STATUS_CODE", statusCode);
			try {
                if (response != null) {
                    //ensure the connection is released back to pool
                	EntityUtils.consume(response.getEntity());
                }
            } catch (IOException e) {
                LOG.warn("close response fail", e);
            }
		}
	}
	
	private CloseableHttpClient getHttpClient(Site site) {
		if(site == null)
			return generator.getClient(site);
		String domain = site.getDomain();
		CloseableHttpClient client = clients.get(domain);
		if(client == null) 
			synchronized(this) {
				client = clients.get(domain);
				if(client == null) {
					client = generator.getClient(site);
					clients.put(domain, client);
				}
			}
		return client;
	}
}
