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
@Entity
@Data
@Table(name = "kwc_task")
public class KTask {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int pk;
	@Column
	String agency;
	@Column
	String grp;
	@Column
	String collector;
	@Column
	String cType;
	@Column
	String start;
	@Column
	String end;
	@Column
	String cycleCron;
	@Column
	String status;
	@Column
	String useyn;
	@Column
	String taskNo;
	
	

}
