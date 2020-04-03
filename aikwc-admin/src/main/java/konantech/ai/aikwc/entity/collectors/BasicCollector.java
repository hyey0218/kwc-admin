package konantech.ai.aikwc.entity.collectors;

import javax.persistence.AssociationOverride;
import javax.persistence.AttributeOverride;
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

import konantech.ai.aikwc.entity.ECollector;
import konantech.ai.aikwc.entity.Site;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;



@Table(name = "kwc_collector")
@Getter
@Setter
@Entity
@AttributeOverride(name = "pk", column = @Column(name = "pk"))
public class BasicCollector extends Collector {
	
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
	String contId;
	
	
	@Transient
	String channel;

	
	public BasicCollector() {}
	@Builder
	public BasicCollector(String startUrl, String pageUrl, String startPage, String endPage, String titleLink,
			String title, String content, String writer, String wdatePattern, String writeDate, String contId,
			String channel) {
		super();
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
		this.contId = contId;
		this.channel = channel;
	}
	

	public String getPackageClassName() {
		return "konantech.ai.aikwc.entity.collectors" + getClassName();
	}
}
