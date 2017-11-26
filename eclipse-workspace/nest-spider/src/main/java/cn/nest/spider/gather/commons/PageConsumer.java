package cn.nest.spider.gather.commons;

import cn.nest.spider.entity.async.Task;
import cn.nest.spider.entity.commons.SpiderInfo;
import us.codecraft.webmagic.Page;

@FunctionalInterface
public interface PageConsumer {

	void accept(Page page, SpiderInfo info, Task task);
}
