package com.example.demo.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.dto.MemberDto;
import com.example.demo.dto.RestResponse;
import com.example.demo.entity.Member;
import com.example.demo.service.MemberService;

import jakarta.validation.Valid;

@Controller
public class MemberController {
	@Autowired
	private MemberService service;
	
	// 아이디 중복 확인
	@GetMapping(path="/member/check/username", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RestResponse> idCheck(@Valid MemberDto.Idcheck dto) {
		if(service.idCheck(dto))
			return ResponseEntity.ok(new RestResponse("OK", "사용 가능한 아이디입니다", null));
		return ResponseEntity.status(HttpStatus.CONFLICT).body(new RestResponse("Fail", "사용중인 아이디입니다", null));
	}
	// 이메일 중복 확인
	@GetMapping(path="/member/check/email", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RestResponse> emailCheck(@Valid MemberDto.EmailCheck dto) {
		if(service.emailCheck(dto))
			return ResponseEntity.ok(new RestResponse("OK", "사용 가능한 이메일입니다", null));
		return ResponseEntity.status(HttpStatus.CONFLICT).body(new RestResponse("Fail", "사용중인 이메일입니다", null));
	}
	// 회원가입
	@PostMapping(path="/member/new", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RestResponse> join(@Valid MemberDto.Join dto) {
		Member member = service.join(dto);
		return ResponseEntity.ok(new RestResponse("OK", member, "/member/login"));
	}
	// 아이디 찾기
	@GetMapping(path="/member/find/username", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RestResponse> findId(@Valid MemberDto.FindId dto) {
		service.findId(dto);
		return ResponseEntity.ok(new RestResponse("OK", "아이디를 이메일로 보냈습니다", null));
	}
	// 비밀번호 리셋
	@PatchMapping(path="/member/find/password", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RestResponse> resetPassword(@Valid MemberDto.ResetPassword dto) {
		service.resetPassword(dto);
		return ResponseEntity.ok(new RestResponse("OK", "임시 비밀번호를 이메일로 보냈습니다", null));
	}
	// 비밀번호 변경
	@PatchMapping(path="/member/password", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RestResponse> changePassword(@Valid MemberDto.ChangePassword dto, Principal principal) {
		service.changePassword(dto, principal.getName());
		return ResponseEntity.ok(new RestResponse("OK", "비밀번호를 변경하였습니다", null));
	}
	// 내 정보 보기
	@PreAuthorize("isAuthenticated()")
	@GetMapping(path="/member", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RestResponse> read(Principal principal) {
		return ResponseEntity.ok(new RestResponse("OK", service.read(principal.getName()), null));
	}
	// 내 정보 변경
	@PreAuthorize("isAuthenticated()")
	@PostMapping(path="/member", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RestResponse> update(@Valid MemberDto.Update dto, Principal principal) {
		service.update(dto, principal.getName());
		return ResponseEntity.ok(new RestResponse("OK", "회원 정보를 변경했습니다", "/member/read"));
	}
	// 회원 탈퇴
	@PreAuthorize("isAuthenticated()")
	@DeleteMapping(path="/member/delete", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RestResponse> delete(Principal principal) {
		service.delete(principal.getName());
		return ResponseEntity.ok(new RestResponse("OK", "회원 정보를 삭제했습니다", "/board/list"));
	}
}
