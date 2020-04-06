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
	@PrePersist
	public void create() {
		this.createDate = LocalDateTime.now();
		this.readyn = "N";
		this.delyn = "N";
	}

}
