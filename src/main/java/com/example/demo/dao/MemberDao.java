package com.example.demo.dao;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.entity.Member;

@Mapper
public interface MemberDao {
	// 아이디 중복 확인
	public Boolean existsById(String username);
	// 이메일 중복 확인
	public Boolean existsByEmail(String email);
	// 회원가입
	public Integer save(Member entity);
	// 회원(아이디) 찾기
	public Optional<Member> findByEmail(String email);
	// 회원(비밀번호) 찾기
	public Optional<Member> findById(String username);
	// 내 정보 변경
	public Integer update(Member member);
	// 계정 삭제
	public Integer delete(String username);
	// 체크코드 찾기
	public Optional<Member> findByCheckcode(String checkcode);
	
}
