package konantech.ai.aikwc.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// 나중에 필요시 DB 사용
//@Entity
//@Table(name = "user")
public class User {

//	@Id
	String id;
	
//	@Column(length = 512)
	String password;
	
	public User() {
		
	}

	@Builder
	public User(String id, String password) {
		this.id = id;
		this.password = password;
	}

	
}
