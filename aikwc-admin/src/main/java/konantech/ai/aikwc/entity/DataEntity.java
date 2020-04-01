package konantech.ai.aikwc.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataEntity {
	
	
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	int idx;
	String channel;
	String site_name;
	String board_name;
	String uniqkey;
	String url;
	String title;
	String doc;
	String write_id;
	LocalDateTime write_time;
	LocalDateTime crawled_time;
	LocalDateTime update_time;
	String pseudo;
}
