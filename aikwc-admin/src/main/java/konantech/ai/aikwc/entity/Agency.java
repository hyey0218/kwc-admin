package konantech.ai.aikwc.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Table(name = "kwc_agency")
public class Agency {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int pk;
	
	@Column
	String code;
	
	@Column
	String name;
	
	@Column
	String useyn;
	
	
	public Agency() {
	}

	@Builder
	public Agency(String code, String name, String useyn) {
		super();
		this.code = code;
		this.name = name;
		this.useyn = useyn;
	}
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "agency")
	List<Group> group = new ArrayList<>();
	
}
