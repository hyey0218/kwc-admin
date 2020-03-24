package konantech.ai.aikwc.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import konantech.ai.aikwc.service.UserService;


@Service("UserService")
public class UserServiceImpl implements UserService {

	@Value("${konan.admin.id}")
	String adminId;
	
	@Value("${konan.admin.password}")
	String adminPassword;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	

 	@Bean
 	public PasswordEncoder passwordEncoder() {
 		return new BCryptPasswordEncoder(); //password 암호화를 위한 빈 등록
 	}

	@Override
	public void selectUser() {
		// TODO Auto-generated method stub

	}

	@Override
	public UserDetails loadUserByUsername(String id) {
		// TODO Auto-generated method stub
		List<GrantedAuthority> authorities = new ArrayList<>();
		
		if(!id.equals(adminId))
			throw new UsernameNotFoundException(id);
		authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		String password = passwordEncoder.encode(adminPassword);
		
		return new User(adminId, password, authorities);
	}

	@Override
	public int joinUser() {
		// TODO Auto-generated method stub
		return 0;
	}

}
