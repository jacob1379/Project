package com.example.demo.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dao.MemberDao;
import com.example.demo.dto.MemberDto;
import com.example.demo.entity.Member;
import com.example.demo.exception.JobFailException;
import com.example.demo.exception.MemberNotFoundException;
import com.example.demo.util.MailUtil;

@Service
public class MemberService {
	
	@Autowired
	private MemberDao memberDao;
	
	@Autowired private PasswordEncoder passwordEncoder;
	 
	@Autowired
	private MailUtil mailUtil;
	
	// 아이디 중복 확인
	public Boolean idCheck(MemberDto.Idcheck dto) {
		return !memberDao.existsById(dto.getUsername());
	}
	// 이메일 중복 확인
	public Boolean emailCheck(MemberDto.EmailCheck dto) {
		return !memberDao.existsByEmail(dto.getEmail());
	}
	// 회원가입
	public Member join(MemberDto.Join dto) {
		Member member = dto.toEntity();
		String checkcode = RandomStringUtils.randomAlphanumeric(20);
		String encodedPassword = passwordEncoder.encode(member.getPassword());
		
		member.addJoinInfo(checkcode, encodedPassword);
		memberDao.save(member);
		mailUtil.sendJoinCheckMail("admin@domain.com", member.getEmail(), checkcode);
		return member;
	}
	// 회원가입 확인
	public void checkJoin(String checkcode) {
		Member member = memberDao.findByCheckcode(checkcode).orElseThrow(MemberNotFoundException::new);
		memberDao.update(Member.builder().username(member.getUsername()).enabled(true).checkcode("").build());
	}
	// 아이디 찾기
	public void findId(MemberDto.FindId dto) {
		Member member = memberDao.findByEmail(dto.getEmail()).orElseThrow(MemberNotFoundException::new);
		mailUtil.sendFindIdMail("admin@domain.com", dto.getEmail(), member.getUsername());
	}
	// 비밀번호 리셋(임시비밀번호 발급)
	public void resetPassword(MemberDto.ResetPassword dto) {
		memberDao.findById(dto.getUsername()).orElseThrow(MemberNotFoundException::new);
		String password = RandomStringUtils.randomAlphanumeric(20);
		String encodedPassword = passwordEncoder.encode(password);
		memberDao.update(Member.builder().username(dto.getUsername()).password(encodedPassword).build());
		mailUtil.sendResetPasswordMail("admin@domain.com", dto.getEmail(), password);
	}
	// 비밀번호 변경
	public Integer changePassword(MemberDto.ChangePassword dto, String loginId) {
		String encodedPassword = memberDao.findById(loginId).orElseThrow(MemberNotFoundException::new).getPassword();
		if(passwordEncoder.matches(dto.getPassword(), encodedPassword)==false)
			throw new JobFailException("비밀번호를 변경할 수 없습니다");
		String newEncodedPassword = passwordEncoder.encode(dto.getNewPassword());
		return memberDao.update(Member.builder().username(loginId).password(newEncodedPassword).build());
	}
	// 내 정보 조회
	public MemberDto.Read read(String loginId) {
		Member member = memberDao.findById(loginId).orElseThrow(MemberNotFoundException::new);
		MemberDto.Read dto = member.toRead();
		return dto;
	}
	// 내 정보 변경
	public Integer update(MemberDto.Update dto, String loginId) {
		Member member = memberDao.findById(loginId).orElseThrow(MemberNotFoundException::new);
		return memberDao.update(Member.builder().username(loginId).email(dto.getEmail()).build());
	}
	
	// 회원 탈퇴
	public Integer delete(String loginId) {
		if(memberDao.existsById(loginId)==false)
			throw new MemberNotFoundException();
		return memberDao.delete(loginId);
	}
}
