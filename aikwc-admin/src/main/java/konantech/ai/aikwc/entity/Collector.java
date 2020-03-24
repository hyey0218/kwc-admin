package konantech.ai.aikwc.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;



@Entity
@Data
@Table(name = "kwc_collector")
@Getter
@Setter
public class Collector {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int pk;
	
	@Column
	String site;
	@Column
	String code;
	@Column
	String name;

	@Column
	String startUrl;
	@Column
	String pageUrl;
	@Column
	String startPage;
	@Column
	String endPage;
	@Column
	String titleLink;
	@Column
	String title;
	@Column(nullable = true)
	String content;
	@Column
	String writer;
	@Column(name="wdate_pattern")
	String wdatePattern;
	@Column
	String writeDate;
	@Column
	String useyn;
	@Column
	String status;
	@Column
	String contId;
	
//	@Transient
	String gCode;
//	@Transient
	String gName;
//	@Transient
	String sCode;
//	@Transient
	String sName;

	
	public Collector() {}
	@Builder
	public Collector(String site, String code, String name, String startUrl, String pageUrl, String startPage,
			String endPage, String titleLink, String title, String content, String writer, String wdatePattern, 
			String writeDate, String useyn, String status, String contId) {
		super();
		this.site = site;
		this.code = code;
		this.name = name;
		this.startUrl = startUrl;
		this.pageUrl = pageUrl;
		this.startPage = startPage;
		this.endPage = endPage;
		this.titleLink = titleLink;
		this.title = title;
		this.content = content;
		this.writer = writer;
		this.wdatePattern = wdatePattern;
		this.writeDate = writeDate;
		this.useyn = useyn;
		this.status = status;
		this.contId = contId;
	}
	
	@Builder
	public Collector(String startPage, String endPage, String titleLink, String title, String content, String writer,
			String wdatePattern, String writeDate, String contId) {
		super();
		this.startPage = startPage;
		this.endPage = endPage;
		this.titleLink = titleLink;
		this.title = title;
		this.content = content;
		this.writer = writer;
		this.wdatePattern = wdatePattern;
		this.writeDate = writeDate;
		this.contId = contId;
	}
	@Builder
	public Collector(int pk, String status) {
		this.pk = pk;
		this.status = status;
	}

	@ManyToOne(optional = false)//fetch = FetchType.LAZY
	@JoinColumn(name = "site", referencedColumnName = "pk",insertable = false, updatable = false)
	Site toSite;


	
}
