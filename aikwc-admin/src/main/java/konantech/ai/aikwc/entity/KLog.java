package konantech.ai.aikwc.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Data
@Table(name = "kwc_log")
public class KLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int pk;
	@Column
	String type;
	@Column
	String agency;
	@Column
	String logCont;
	@Column
	String comment;
	@Column
	LocalDateTime createDate;
	@Column
	String readyn;
	@Column
	String delyn;
	@Column
	String param1;
	@Column
	String param2;
	@Column
	String param3;
	
	public KLog() {}
	@Builder
	public KLog(String type, String agency, String logCont, String comment, LocalDateTime createDate, String readyn, String delyn,
			String param1, String param2, String param3) {
		super();
		this.type = type;
		this.agency = agency;
		this.logCont = logCont;
		this.comment = comment;
		this.createDate = createDate;
		this.readyn = readyn;
		this.delyn = delyn;
		this.param1 = param1;
		this.param2 = param2;
		this.param3 = param3;
	}

	@PrePersist
	public void create() {
		this.createDate = LocalDateTime.now();
		this.readyn = "N";
		this.delyn = "N";
	}

}
