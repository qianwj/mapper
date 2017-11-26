package cn.nest.spider.dao.es;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.types.TypesExistsRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import cn.nest.spider.util.StaticValue;

@Component("esClient")
@Scope("prototype")
public class ESClient {

	private static final String COMMON_INDEX_CONFIG = "common_index.json";
	
	private static final String COMMON_INDEX_NAME = "commons";
	
	private static final String WEBPAGE_TYPE_NAME = "webpage";
	
	private static final String SPIDER_INFO_TYPE_NAME = "spiderinfo";
	
	private static final String SPIDER_INFO_INDEX_NAME = "spiderinfo";
	
	private static final Logger LOG = LogManager.getLogger(ESClient.class);
	
	private Client client;
	
	@Resource(name = "staticValue")
	private StaticValue sValue;
	
	
	public Client getClient() {
		if(!sValue.isNeedES()) {
			LOG.info("配置文件已声明不需要ES, 如需要ES, 请更改配置文件");
			return null;
		}
		if(client!=null)
			return client;
		LOG.info("正在初始化客户端， Host:{}，Port:{}", sValue.getEsHost(), sValue.getEsPort());
		Settings settings = Settings.builder()
		        .put("cluster.name", sValue.getEsClusterName()).build();
		try {
			client = new PreBuiltTransportClient(settings)
			        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(sValue.getEsHost()), sValue.getEsPort()));
		    final ClusterHealthResponse healthResponse = client.admin().cluster()
		    		.prepareHealth().setTimeout(TimeValue.timeValueMinutes(1)).get();
		    if(healthResponse.isTimedOut()) 
		    	LOG.info("ES客户端初始化失败,响应超时");
		    else
		    	LOG.info("ES客户端初始化成功");
		} catch(IOException e) {
			LOG.fatal("ES客户端构建失败，由于 " + e.getLocalizedMessage());
		}
		return client;
	}
	
	public boolean checkCommonIndex() {
		return checkIndex(COMMON_INDEX_NAME, COMMON_INDEX_CONFIG);
	}
	
	public boolean checkWebpageType() {
		return checkType(COMMON_INDEX_NAME, WEBPAGE_TYPE_NAME, "webpage.json");
	}
	
	public boolean checkSpiderInfoIndex() {
		return checkIndex(SPIDER_INFO_INDEX_NAME, SPIDER_INFO_TYPE_NAME);
	}
	
	public boolean checkSpiderInfoType() {
		return checkType(SPIDER_INFO_INDEX_NAME, SPIDER_INFO_TYPE_NAME, "spiderinfo.json");
	}
	
	public boolean checkIndex(String index, String mapping) {
		if(client == null) 
			return false;
		if(!client.admin().indices().exists(new IndicesExistsRequest(index)).actionGet().isExists()) {
			File mappingFile;
			try {
				mappingFile = new File(this.getClass().getClassLoader().getResource(mapping).getFile());
			} catch(Exception e) {
				LOG.fatal("查找" + index + "index mapping配置文件出错， 由于 " + e.getLocalizedMessage());
				return false;
			}
			LOG.debug(index + "index MappingFile:" + mappingFile.getPath());
            LOG.info(index + " index 不存在,正在准备创建index");
            CreateIndexResponse response = null;
            try {
            	response = client.admin().indices().prepareCreate(index).setSettings(FileUtils.readFileToString(mappingFile), XContentType.JSON).execute().actionGet();
            } catch(IOException e) {
            	LOG.error("创建" + index + "index失败， 由于 " + e.getLocalizedMessage());
            	return false;
            }
            if(response.isAcknowledged())
            	LOG.info("创建" + index + "成功");
            else {
            	LOG.error("创建" + index + "失败");
            	return false;
            }
		} else
			LOG.info(index + "index已存在");
		return true;
	}
	
	public boolean checkType(String index, String type, String mapping) {
		if(client == null) 
			return false;
		if(!client.admin().indices().typesExists(new TypesExistsRequest(new String[] {index}, type)).actionGet().isExists()) {
			LOG.info(type + "type不存在，准备创建type");
			File mappingFile;
			try {
				mappingFile = new File(this.getClass().getClassLoader().getResource(mapping).getFile());
			} catch(Exception e) {
				LOG.fatal("查找ES mapping配置文件出错， 由于 " + e.getLocalizedMessage());
				return false;
			}
			LOG.debug(type + " mapping file:" + mappingFile.getPath());
			PutMappingRequest request = null;
			PutMappingResponse response = null;
			try {
				request = Requests.putMappingRequest(index).type(type)
						.source(FileUtils.readFileToString(mappingFile), XContentType.JSON);
			} catch(IOException e) {
				LOG.error(type + "type创建失败， 由于 " + e.getLocalizedMessage());
			}
			response = client.admin().indices().putMapping(request).actionGet();
			if(response.isAcknowledged())
				LOG.info(type + "type创建成功");
			else {
				LOG.info(type + "type创建失败");
				return false;
			}
		} else
			LOG.info(type + "type已存在");
		return true;
	}

	public StaticValue getsValue() {
		return sValue;
	}

	public ESClient setsValue(StaticValue sValue) {
		this.sValue = sValue;
		return this;
	}
}
