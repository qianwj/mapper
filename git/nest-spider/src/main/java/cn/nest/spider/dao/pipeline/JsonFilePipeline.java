package cn.nest.spider.dao.pipeline;


import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import cn.nest.spider.entity.commons.Webpage;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

@Component
@Scope("prototype")
public class JsonFilePipeline implements Pipeline {
	
	private static final Logger LOG = LogManager.getLogger(JsonFilePipeline.class);
	
	private static final Gson GSON = new Gson();

	@Override
	public void process(ResultItems items, Task task) {
		Webpage page = WebpagePipeline.convertToWebpage(items);
		try {
			FileUtils.writeStringToFile(new File("nest_spider_data/" + page.getSpiderUUID() + ".json"), GSON.toJson(page) + "\n", true);
		} catch(IOException e) {
			LOG.error("序列化网页信息出错， 由于" + e.getLocalizedMessage());
		}
	}

}
