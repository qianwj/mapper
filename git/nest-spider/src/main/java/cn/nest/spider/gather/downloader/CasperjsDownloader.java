package cn.nest.spider.gather.downloader;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.nest.spider.gather.commons.Casperjs;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
//import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.AbstractDownloader;
import us.codecraft.webmagic.selector.PlainText;

@Component
public class CasperjsDownloader extends AbstractDownloader {
	
    @Autowired
    private Casperjs casperjs;

	@Override
	public Page download(Request request, Task task) {
		String html = null;
		try {
			html = casperjs.gatherHtml(new cn.nest.spider.entity.commons.Request(request.getUrl(), true));
		} catch(IOException e) {
			request.putExtra("EXCEPTION", e);
            onError(request);
            return null;
		}
		Page page = new Page().setRawText(html);
		page.setRequest(request);
		page.setUrl(new PlainText(request.getUrl()));
		onSuccess(request);
		return page;
	}

	@Override
	public void setThread(int threadNum) {
		
	}

	
}
