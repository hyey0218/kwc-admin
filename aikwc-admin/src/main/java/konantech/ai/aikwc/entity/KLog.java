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
public class Klog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int pk;
	@Column
	String type;
	@Column
	String comment;
	@Column
	LocalDateTime date;
	
	public Klog() {}
	@Builder
	public Klog(String type, String comment, LocalDateTime date) {
		super();
		this.type = type;
		this.comment = comment;
		this.date = date;
	}
	
	
	@PrePersist
	public void createDate() {
		this.date = LocalDateTime.now();
	}

}
