package konantech.ai.aikwc.common.config;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import konantech.ai.aikwc.service.UserService;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Resource
	private UserService UserService;
	
	@Autowired
	AuthFailureHandler authFilureHandler;
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/static/**");
	}

	@Override
 	protected void configure(HttpSecurity http) throws Exception {
		
		// 로그인 설정
		http.authorizeRequests()
		.antMatchers("/login").permitAll()
		.antMatchers("/**").hasRole("ADMIN")
		.and().formLogin().loginPage("/login").defaultSuccessUrl("/main")
		.failureHandler(authFilureHandler)
		.usernameParameter("id").passwordParameter("password");
		
		
		// 403 페이지, 로그아웃
//		http.exceptionHandling();//.accessDeniedPage("/loginFail");
		http.logout().logoutUrl("/logout").invalidateHttpSession(true);
		
	
		//h2 console 사용을위해
		http.authorizeRequests().antMatchers("/h2/**").permitAll()
		.and().csrf().disable()
		.headers().frameOptions().disable();
		
		
//		security.authorizeRequests()
//		.antMatchers("/**").permitAll();
 	}

	
 	@Override
 	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
 		//  인증 -> AuthenticationManager : 생성은 Builder
 		// (loadUserByUsername() 을 구현한)UserDetailService 를 통해 필요한 정보를 가져옴
 		
// 		auth.authenticationProvider(authProvider); // provider를 이용한 로그인
 		auth.userDetailsService(UserService); // 
 	}

	
}
