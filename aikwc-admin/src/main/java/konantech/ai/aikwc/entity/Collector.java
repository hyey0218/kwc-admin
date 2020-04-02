package konantech.ai.aikwc.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
//@MappedSuperclass
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name="v_collector")
@Entity
public class Collector {
	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	int pk;
	
	@Column
	String code;
	@Column
	String name;
	@Column
	LocalDateTime ctrtStart;
	@Column
	LocalDateTime ctrtEnd;
	@Column
	String useyn;
	
	@Column
	String className;
	
	@Column
	String status;
	
	@Column
	String hashed;
	
	@Column
	String site;
	
	public String getPackageClassName() {
		return "konantech.ai.aikwc.entity.collectors" + getClassName();
	}
}
