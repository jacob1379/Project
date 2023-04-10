package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.example.demo.security.MemberAccessDeniedHandler;
import com.example.demo.security.MemberLoginFailureHandler;
import com.example.demo.security.MemberLoginSuccessHandler;

import jakarta.servlet.DispatcherType;

@Configuration
@EnableMethodSecurity
// 해당 클래스에서 1개 이상의 빈을 생성하고 있음을 명시함. 빈 어노테이션을 사용하는 클래스는 반드시 @Configuration과 함께 사용
public class SecurityConfig {
	
	@Autowired
	private MemberLoginSuccessHandler memberLoginSuccessHandler;
	@Autowired
	private MemberLoginFailureHandler memberLoginFailureHandler;
	@Autowired
	private MemberAccessDeniedHandler accessDeniedHandler;
	
	@Bean
	protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		http.csrf().disable().cors().disable()
        .authorizeHttpRequests(request -> request
        	.dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
        	.requestMatchers("/", "/img/**","/member/join","/member/new","/member/login","/css/**","/board/list","/board/all",
        			"/board/read","/board","/js/**","/member/find/**","/member/check/**").permitAll()
                .anyRequest().authenticated()	// 어떠한 요청이라도 인증필요
        )
        .formLogin(login -> login	// form 방식 로그인 사용
        		.loginPage("/member/login")		// 커스텀 로그인 페이지 지정
        		.loginProcessingUrl("/member/login")	// submit받을 URL
        		.usernameParameter("username")
        		.passwordParameter("password")
        		.successHandler(memberLoginSuccessHandler)
        		.failureHandler(memberLoginFailureHandler)
                .permitAll()	// 대시보드 이동이 막히면 안되므로 얘는 허용
        )
        .logout().logoutUrl("/member/logout").logoutSuccessUrl("/").invalidateHttpSession(true);	// 로그아웃은 기본설정으로 (/logout으로 인증해제)
		
		http.exceptionHandling().accessDeniedHandler(accessDeniedHandler);
		
		return http.build();
	}
}
