package com.example.demo.entity;

import java.time.LocalDate;

import com.example.demo.dto.MemberDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member {
	private String username;
	private String password;
	private String irum;
	private String email;
	private LocalDate birthday;
	private LocalDate joinday;
	private String role;
	private Integer loginFailCount;
	private String checkcode;
	private Boolean enabled;
	
	public MemberDto.Read toRead() {
		return MemberDto.Read.builder().irum(irum).username(username).birthday(birthday).joinday(joinday).email(email).build();
	}
	
	public MemberDto.User toDto() {
		return MemberDto.User.builder().username(username).password(password).role(role).enabled(enabled).build();
	}
	
	public void addJoinInfo(String checkcode, String encodedPassword) {
		this.checkcode = checkcode;
		this.password = encodedPassword;
	}
}
