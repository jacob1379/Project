package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.service.MemberService;

import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpSession;

@Controller
public class ViewController {
	
	@Autowired
	private MemberService service;
	
	@PermitAll
	@GetMapping({"/", "/board/list"})
	public ModelAndView list(HttpSession session) {
		ModelAndView mav = new ModelAndView("/board/list");
		if(session.getAttribute("msg")!=null) {
			mav.addObject("msg", session.getAttribute("msg"));
			session.removeAttribute("msg");
		}
		return mav;
	}
	
	@GetMapping("/board/read")
	public void read() {
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/board/write")
	public void write() {
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/board/update")
	public void update() {	
	}
	
	@PreAuthorize("isAnonymous()")
	@GetMapping("/member/login")
	public ModelAndView login(HttpSession session) {
		ModelAndView mav = new ModelAndView("/member/login");
		if(session.getAttribute("msg")!=null) {
			mav.addObject("msg", session.getAttribute("msg"));
			session.removeAttribute("msg");
		}
		return mav;
	}
	
	@PreAuthorize("isAnonymous()")
	@GetMapping("/member/join")
	public void join() {
	}
	
	@PreAuthorize("isAnonymous()")
	@GetMapping("/member/check/join")
	public String checkJoin(String checkcode) {
		service.checkJoin(checkcode);
		return "/member/login";
	}
	
	@PreAuthorize("isAnonymous()")
	@GetMapping("/member/find")
	public void find() {
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/member/read")
	public void memberRead() {		
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/member/change_password")
	public ModelAndView changePassword(HttpSession session) {
		ModelAndView mav = new ModelAndView("/member/change_password");
		if(session.getAttribute("msg")!=null) {
			mav.addObject("msg", session.getAttribute("msg"));
			session.removeAttribute("msg");
		}
		return mav;
	}
}
