package com.example.demo;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.MemberDto;
import com.example.demo.service.MemberService;

@SpringBootTest
public class MemberServiceTest {
	
	@Autowired
	private MemberService service;
	
	//@Transactional
	@Test
	public void joinTest() {
		MemberDto.Join dto = MemberDto.Join.builder().username("FINDJOB1").password("1q2w3e4r!").irum("조준형")
				.birthday(LocalDate.now()).email("hompajo27@naver.com").build();
		service.join(dto);
	}
	
	//@Test
	public void readTest() {
		MemberDto.Read dto = service.read("SUMMER22");
		System.out.println(dto);
	}
}
