package cn.nest.spider.dao.pipeline;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;

import cn.nest.spider.util.StaticValue;
import redis.clients.jedis.Jedis;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

public class WebpageRedisPipeline implements Pipeline {
	
	private static Jedis jedis;
	
	private static Gson gson = new Gson();
	
	private final boolean needRedis;
	
	private final String redisPublishChannelName;
	
	private static final Logger LOG = LogManager.getLogger(WebpageRedisPipeline.class);
	
	@Autowired
	WebpageRedisPipeline(StaticValue value) {
		this.needRedis = value.isNeedRedis();
		this.redisPublishChannelName =  value.getWebpageRedisPublishChannelName();
		if(this.needRedis) {
			LOG.info("redis client 开始初始化 host:" + value.getRedisHost() + ", port:" + value.getRedisPort());
			jedis = new Jedis(value.getRedisHost(), value.getRedisPort());
			LOG.info("redis client 初始化成功");
		} else 
			LOG.warn("redis client 未初始化");
	}

	@Override
	public void process(ResultItems resultItems, Task task) {
        if(!needRedis)
        	return;
        long recivedRedisCount = jedis.publish(redisPublishChannelName, gson.toJson(resultItems.getAll()));
	}

}
