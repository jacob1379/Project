package com.example.demo.dto;

import java.time.LocalDate;

import com.example.demo.entity.Member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public class MemberDto {
	
	// 아이디 중복 검사
	@Data
	public static class Idcheck {
		@Pattern(regexp="^[A-Z0-9]{8,10}$", message="아이디는 대문자와 숫자 8~10자리입니다")
		private String username;
	}
	// 이메일 인증
	@Data
	public static class EmailCheck {
		@Email
		@NotEmpty(message="이메일은 필수 입력입니다")
		private String email;
	}
	//회원가입
	@Data
	@Builder
	public static class Join {
		
		@Pattern(regexp="^[A-Z0-9]{8,10}$", message="아이디는 대문자와 숫자 8~10자리입니다")
		@NotEmpty(message="아이디는 필수 입력입니다")
		private String username;
		
		@NotEmpty(message="비밀번호는 필수 입력입니다")
		private String password;
		
		@NotEmpty(message="이름은 필수 입력입니다")
		private String irum;
		
		@NotEmpty(message="이메일은 필수 입력입니다")
		private String email;
		
		@NotNull(message="생일은 필수 입력입니다")
		private LocalDate birthday;
		
		public Member toEntity() {
			return Member.builder().username(username).password(password).irum(irum).email(email).birthday(birthday).build();
		}		
	}
	// 아이디 찾기
	@Data
	public static class FindId {
		@NotEmpty(message="이메일은 필수 입력입니다")
		@Email(message="잘못된 이메일 형식입니다")
		private String email;
	}
	// 비밀번호 리셋
	@Data
	public static class ResetPassword {
		@NotEmpty(message="아이디는 필수 입력입니다")
		private String username;
		
		@Email
		@NotEmpty(message="이메일은 필수 입력입니다")
		private String email;
	}
	// 비밀번호 바꾸기
	@Data
	public static class ChangePassword {
		@NotNull(message="비밀번호는 필수 입력입니다")
		private String password;
		@NotNull(message="새 비밀번호는 필수 입력입니다")
		private String newPassword;
	}
	// 내 정보 보기
	@Data
	@Builder
	public static class Read {
		private String irum;
		private String username;
		private LocalDate birthday;
		private LocalDate joinday;
		private String email;
	}
	// 이메일 변경
	@Data
	public static class Update {
		private String email;
	}
	// 회원 객체
	@Data
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class User {
		private String username;
		private String password;
		private String role;
		private Boolean enabled;
	}	
}
