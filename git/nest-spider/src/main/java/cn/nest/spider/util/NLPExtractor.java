package cn.nest.spider.util;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author web
 *
 */
public interface NLPExtractor {

	//抽取命名实体
	Map<String, Set<String>> extractNameEntity(String content);
	
	//抽取摘要
	List<String> extractSummary(String content);
	
	//抽取关键词
	List<String> extractKeyWords(String content);
}
