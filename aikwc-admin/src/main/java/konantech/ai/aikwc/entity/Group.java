package konantech.ai.aikwc.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
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

@Getter
@Setter
@Entity
@Data
@Table(name = "kwc_group")
public class Group {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int pk;
	
	@Column
	String agency;
	@Column
	String code;
	@Column
	String name;
	
	@Transient
	String agencyName;
	
	public Group() {}
	@Builder
	public Group(String agency, String code, String name) {
		super();
		this.agency = agency;
		this.code = code;
		this.name = name;
	}

}
