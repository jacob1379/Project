package com.example.demo.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.BoardDto;
import com.example.demo.dto.MemberBoardDto;
import com.example.demo.dto.RestResponse;
import com.example.demo.entity.Board;
import com.example.demo.service.BoardService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Validated
@RestController
public class BoardController {
	@Autowired
	private BoardService service;
	
	// 작성
	@PreAuthorize("isAuthenticated()")
	@PostMapping(value="/board/new", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RestResponse> write(@ModelAttribute @Valid BoardDto.WriteDto dto, BindingResult bindingResult, Principal principal) {
		Board board = service.write(dto, principal.getName());
		return ResponseEntity.ok(new RestResponse("OK", board, "/board/read?bno="+board.getBno()));
	}
	
	// 목록
	@GetMapping(value="/board/all", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RestResponse> list(@RequestParam(defaultValue="1") Integer pageno, String writer) {
		return ResponseEntity.ok(new RestResponse("OK", service.list(pageno, writer), null));
	}
	
	// 읽기 
	@GetMapping(value="/board", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RestResponse> read(@RequestParam @NotNull(message="글 번호는 필수입력입니다.") Integer bno, Principal principal){
		String loginId = principal==null? null : principal.getName();
		BoardDto.ReadDto dto = service.read(bno, loginId);
		return ResponseEntity.ok(new RestResponse("OK", dto, null));
	}
	
	// 변경
	@PreAuthorize("isAuthenticated()")
	@PutMapping(value="/board/modify", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RestResponse> update(@ModelAttribute @Valid BoardDto.UpdateDto dto, BindingResult bindingResult, Principal principal) {
		service.update(dto, principal.getName());
		return ResponseEntity.ok(new RestResponse("OK", dto.getBno()+"번 글을 변경했습니다.", "/board/read?bno="+dto.getBno()));
	}
	
	// 삭제
	@PreAuthorize("isAuthenticated()")
	@DeleteMapping(value="/board/delete", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RestResponse> delete(@RequestParam @NotNull(message="글 번호는 필수입력입니다.") Integer bno, Principal principal) {
		service.delete(bno, principal.getName());
		return ResponseEntity.ok(new RestResponse("OK", bno+"번 글을 삭제하였습니다.", "/board/list"));
	}
	
	// 좋아요 싫어요
	@PreAuthorize("isAuthenticated()")
	@PatchMapping(value="/board/gob", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RestResponse> goodOrBad(@ModelAttribute @Valid MemberBoardDto dto, BindingResult bindingResult, Principal principal) {
		Integer result = service.goodOrBad(dto, principal.getName());
		return ResponseEntity.ok(new RestResponse("OK", result, "/board/read?bno="+dto.getBno()));
	}
	
}
