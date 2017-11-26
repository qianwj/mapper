package cn.nest.spider.util.nlp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;

import cn.nest.spider.util.NLPExtractor;

@Component("hanNLPExtractor")
@Scope("prototype")
public class HanlpExtractor implements NLPExtractor {
	
	private static final Segment SEGMENT = HanLP.newSegment()
			.enableOrganizationRecognize(true).enablePlaceRecognize(true);
	
	private static final Logger LOG = LogManager.getLogger(HanlpExtractor.class);

	public Map<String, Set<String>> extractNameEntity(String content) {
		//对文本进行分词
		List<Term> list = SEGMENT.seg(content);
		
		//识别人名
		Set<String> nrList = list.stream().filter(term -> term.nature.startsWith("nr"))
				.map(term -> term.word).collect(Collectors.toSet());
		//识别地名
		Set<String> nsList = list.stream().filter(term -> term.nature.startsWith("ns"))
				.map(term -> term.word).collect(Collectors.toSet());
		//识别机构名
	    Set<String> ntList = list.stream().filter(term -> term.nature.startsWith("nt"))
	    		.map(term -> term.word).collect(Collectors.toSet());
	    Map<String, Set<String>> namedEntity = new HashMap<>();
	    namedEntity.put("nr", nrList);
	    namedEntity.put("ns", nsList);
	    namedEntity.put("nt", ntList);
		return namedEntity;
	}

	public List<String> extractSummary(String content) {
		return HanLP.extractSummary(content, 7);
	}

	public List<String> extractKeyWords(String content) {
		return HanLP.extractKeyword(content, 10);
	}

}
