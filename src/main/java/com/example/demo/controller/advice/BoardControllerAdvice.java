package com.example.demo.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.dto.RestResponse;
import com.example.demo.exception.BoardNotFoundException;
import com.example.demo.exception.JobFailException;

@RestControllerAdvice
public class BoardControllerAdvice {
	
	@ExceptionHandler(BoardNotFoundException.class)
	public ResponseEntity<RestResponse> boardNotFoundExceptionHandler() {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(new RestResponse("Fail", "게시물을 찾을 수 없습니다.", "/board/list"));
	}
	@ExceptionHandler(JobFailException.class)
	public ResponseEntity<RestResponse> jobFailExceptionHandler(JobFailException e) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(new RestResponse("Fail", e.getMessage(), null));
	}
	
}
