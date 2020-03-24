package konantech.ai.aikwc.common.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.DefaultValueProcessor;


public class CommonUtil {

	
	public static Object parseToJson(Object object){
		if(object instanceof List || object instanceof Object[]){
			return JSONArray.fromObject(object, jsonConfig());
		}else{
			if(object == null) return null;
			return JSONObject.fromObject(object, jsonConfig());
		}
	}
	
	@SuppressWarnings("rawtypes")
	private static JsonConfig jsonConfig() {
		JsonConfig config = new JsonConfig();
		config.registerDefaultValueProcessor(String.class, new DefaultValueProcessor() {
			@Override
			public Object getDefaultValue(Class arg0) {return "";}
		});
		config.registerDefaultValueProcessor(Date.class, new DefaultValueProcessor() {
			@Override
			public Object getDefaultValue(Class arg0) {return "";}
		});
		config.registerDefaultValueProcessor(Integer.class, new DefaultValueProcessor() {
			@Override
			public Object getDefaultValue(Class arg0) {return "";}
		});
		
		return config;
	}
	public static LocalDateTime stringToLocalDateTime(String str, String pattern, boolean timeContains) {
		LocalDateTime dateTime;
		if(timeContains)
			dateTime = stringToLocalDateTime(str,pattern);
		else
			dateTime = stringToLocalDay(str,pattern);
		return dateTime;
	}
	
	public static LocalDateTime stringToLocalDateTime(String str, String pattern) {
		pattern = StringUtils.defaultIfEmpty(pattern, "yyyy-MM-dd HH:mm:ss");
        LocalDateTime timeToMins = LocalDateTime.parse(str, DateTimeFormatter.ofPattern(pattern).withLocale(Locale.KOREA));
        return timeToMins;
    }
	public static LocalDateTime stringToLocalDay(String str, String pattern) {
		pattern = StringUtils.defaultIfEmpty(pattern, "yyyy-MM-dd");
        LocalDateTime timeToMins = LocalDate.parse(str, DateTimeFormatter.ofPattern(pattern).withLocale(Locale.KOREA)).atStartOfDay();
        return timeToMins;
    }
	
	public static String getUriParamValue(String uri, String paramName) {
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(uri);
		MultiValueMap<String, String> map = uriComponentsBuilder.build().getQueryParams();
		return StringUtils.defaultIfEmpty(map.getFirst(paramName), "");
	}
	
	
}