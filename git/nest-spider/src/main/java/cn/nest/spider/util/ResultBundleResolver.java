package cn.nest.spider.util;

import java.text.SimpleDateFormat;
import java.lang.reflect.Type;
import java.lang.reflect.ParameterizedType;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class ResultBundleResolver {

	private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy MM dd HH:mm:ss", Locale.CHINESE);
	
	private static final Gson GSON = new GsonBuilder()
			.registerTypeAdapter(Date.class, (JsonDeserializer<Date>)(json, typeOfT, context) -> {
				try {
					return FORMAT.parse(json.getAsJsonPrimitive().getAsString().replaceAll("\"", ""));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				return null;
	        }).create();
	
	private static final Logger LOG = LogManager.getLogger(ResultBundleResolver.class);
	
	private void check(ResultBundle result) {
		if(result == null)
			LOG.error("结果为空，请检查参数");
		else if(result.isSuccess() == false)
			LOG.error("调用出错，由于 " + result.getErrorMessage() + ";" + "追踪ID:" + result.getTraceId());
	}
	
	public <T> ResultBundle<T> bundle(String json) {
		ResultBundle<T> result = null;
		try {
			Type objType = new TypeToken<ResultBundle<T>>() {}.getType();
			result = GSON.fromJson(json, objType);
		} catch(JsonSyntaxException e) {
			LOG.error("无法解析的返回值，由于 " + e.getLocalizedMessage());
		}
		check(result);
		return result;
	}
	
	public <T> ResultBundle<T> bundle(String json, Type classOfT) {
		ResultBundle<T> result = null;
		try {
			Type objType = new ParameterizedType() {

				@Override
				public Type[] getActualTypeArguments() {
					return new Type[] {classOfT};
				}

				@Override
				public Type getRawType() {
					return ResultBundle.class;
				}

				@Override
				public Type getOwnerType() {
					return null;
				}
				
			};
			result = GSON.fromJson(json, objType);
		} catch(JsonSyntaxException e) {
			LOG.error("无法解析的返回值信息， 由于 " + e.getLocalizedMessage());
			e.printStackTrace();
		}
		check(result);
		return result;
	}
	
	public <T> ResultList<T> listBundle(String json) {
		ResultList<T> result = null;
		try {
			Type objType = new TypeToken<ResultList<T>>() {}.getType();
			result = GSON.fromJson(json, objType);
		} catch(JsonSyntaxException e) {
			LOG.error("无法解析的返回值信息， 由于 " + e.getLocalizedMessage());
			e.printStackTrace();
		}
		check(result);
		return result;
	}
	
	public <T> ResultBundle<T> listBundle(String json, Class<T> classOfT) {
		ResultList<T> result = null;
		try {
			Type objType = new ParameterizedType() {

				@Override
				public Type[] getActualTypeArguments() {
					return new Type[] {classOfT};
				}

				@Override
				public Type getRawType() {
					return ResultList.class;
				}

				@Override
				public Type getOwnerType() {
					return null;
				}
				
			};
			result = GSON.fromJson(json, objType);
		} catch(JsonSyntaxException e) {
			LOG.error("无法解析的信息， 由于 " + e.getLocalizedMessage());
			e.printStackTrace();
		}
		check(result);
		return result;
	}
}
