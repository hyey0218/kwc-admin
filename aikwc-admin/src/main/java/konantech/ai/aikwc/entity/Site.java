package konantech.ai.aikwc.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Data
@Table(name = "kwc_site")
public class Site {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int pk;
	
	@Column
	String grp;
	
	@Column
	String code;
	@Column
	String name;

	public Site() {}
	@Builder
	public Site(String group, String code, String name) {
		super();
		this.grp = group;
		this.code = code;
		this.name = name;
	}
	
//	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
//	@JoinColumn(name = "site")
//	List<Collector> collector;
	
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "grp", referencedColumnName = "pk",insertable = false, updatable = false)
	Group group;
	
}
