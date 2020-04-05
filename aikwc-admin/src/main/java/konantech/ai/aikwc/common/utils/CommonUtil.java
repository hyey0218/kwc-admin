package konantech.ai.aikwc.common.utils;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
	
	public static String getCurrentTimeStr(String pattern) {
		pattern = StringUtils.defaultIfEmpty(pattern, "yyyy-MM-dd HH:mm:ss");
		String timeStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern).withLocale(Locale.KOREA));
		return timeStr;
	}
	
	public static String getUriParamValue(String uri, String paramName) {
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(uri);
		MultiValueMap<String, String> map = uriComponentsBuilder.build().getQueryParams();
		return StringUtils.defaultIfEmpty(map.getFirst(paramName), "");
	}
	
	/**
	 * @param text
	 * @return MD5 인코딩
	 */
	public static String getEncMd5(String text) {
		
		MessageDigest md;
		String encText = "";
		try {
			md = MessageDigest.getInstance("MD5");
	        byte[] bytes = text.getBytes(Charset.forName("UTF-8"));
	        md.update(bytes);
	        encText = Base64.getEncoder().encodeToString(md.digest());
		} catch (NoSuchAlgorithmException e) {
			encText = text;
			e.printStackTrace();
		}
		return encText;
	}
	
	

	public static Map stringToJsonMap(String json) {
		return stringToObject(json, HashMap.class);
	}

	public static Object stringToJsonClass(String json, Class clazz) {
		return stringToObject(json, clazz);
	}

	public static <T> T stringToObject(String jsonString, Class<T> valueType) {
		try {
			if(jsonString != null)
				return new ObjectMapper().readValue(jsonString, valueType);
			else
				return valueType.newInstance();
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String jsonToString(Object jsonObject) {
		return objectToString(jsonObject);
	}

	public static String objectToString(Object json) {
		ObjectMapper om = new ObjectMapper();
		try {
			return om.writeValueAsString(json);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

}
