package konantech.ai.aikwc.entity;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Converter;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import konantech.ai.aikwc.common.utils.CommonUtil;
import konantech.ai.aikwc.entity.collectors.BasicCollector;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "kwc_collector")
public class Collector{
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	int pk;
	
	@Column
	String className;
	@Column
	String viewName;
	
	@Column
	String code;
	@Column
	String name;
	@Column
	String ctrtStart;
	@Column
	String ctrtEnd;
	@Column
	String useyn;
	@Column
	String status;
	@Column
	String site;
	@Column
	String param1;
	@Column
	String param2;
	
	@Column
	String detail;
	
	@Transient
	String channel;
	
	@ManyToOne(optional = false)//fetch = FetchType.LAZY
	@JoinColumn(name = "site", referencedColumnName = "pk",insertable = false, updatable = false)
	Site toSite;
	
	public String getPackageClassName() {
		return "konantech.ai.aikwc.entity.collectors."+getClassName();
	}
	
}