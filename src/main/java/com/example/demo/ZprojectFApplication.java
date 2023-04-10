package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication//(exclude = {SecurityAutoConfiguration.class})
// @ComponentScan이 내부에 설정되어있음
public class ZprojectFApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZprojectFApplication.class, args);
	}
	
	@Bean // 개발자가 직접 제어가 불가능한 외부 라이브러리 등을 빈으로 등록할 때
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
