package konantech.ai.aikwc.entity.collectors;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.persistence.Transient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import konantech.ai.aikwc.common.utils.CommonUtil;
import konantech.ai.aikwc.entity.Collector;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class BasicCollector{
	String pk;
	String hashed;
	String startUrl;
	String pageUrl;
	String startPage;
	String endPage;
	String titleLink;
	String title;
	String content;
	String writer;
	String wdatePattern;
	String writeDate;
	String contId;
	
	
//	public BasicCollector getJsonToObject() {
//		return (BasicCollector) CommonUtil.stringToJsonClass(this.detail, BasicCollector.class);
//	}
}
