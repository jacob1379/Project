package com.example.demo.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CommentDto;
import com.example.demo.dto.RestResponse;
import com.example.demo.service.CommentService;

import jakarta.validation.Valid;

@Validated
@RestController
public class CommentController {
	
	@Autowired
	private CommentService service;
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping(value="/comments/new", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RestResponse> write(@ModelAttribute @Valid CommentDto.Write dto, Principal principal) {
		List<CommentDto.Read> list = service.write(dto, principal.getName());
		return ResponseEntity.ok(new RestResponse("OK", list, null));
	}
	
	@PreAuthorize("isAuthenticated()")
	@DeleteMapping(value="/comments", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RestResponse> delete(@ModelAttribute @Valid CommentDto.Delete dto, Principal principal) {
		List<CommentDto.Read> list = service.delete(dto, principal.getName());
		return ResponseEntity.ok(new RestResponse("OK", list, "/board/read?bno="+dto.getBno()));
	}
}
