package cn.nest.spider.util;

import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component("resultBuilder")
public class ResultBundleBuilder {

	private static final Logger LOG = LogManager.getLogger(ResultBundleBuilder.class);
	
	public <T> ResultBundle<T> bundle(String keyword, Supplier<T> sup) {
		ResultBundle<T> result;
		long start = System.currentTimeMillis();
		try {
			T t = sup.get();
			result = new ResultBundle<>(t, keyword, System.currentTimeMillis() - start);
		} catch(Exception e) {
			result = new ResultBundle<>(keyword, System.currentTimeMillis() - start, false, e.getClass().getName() + ":" + e.getLocalizedMessage());
			LOG.error(result.errorMessage);
			e.printStackTrace();
		}
		return result;
	}
	
	public <T> ResultList<T> bundleList(String keyword, Supplier<? extends Collection<T>> sup) {
		ResultList<T> result;
		long start = System.currentTimeMillis();
		try {
			Collection<T> t = sup.get();
			result = new ResultList<>(t, keyword, System.currentTimeMillis() - start);
		} catch(Exception e) {
			result = new ResultList<>(keyword, System.currentTimeMillis() - start, false, e.getClass().getName() + ":" + e.getLocalizedMessage());
			LOG.error(result.errorMessage);
			e.printStackTrace();
		}
		return result;
	}
}
