package com.example.demo.security;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class MemberAccessDeniedHandler implements AccessDeniedHandler{
	
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		
		if("XMLHttpRequest".equals(request.getHeader("x-requested-with"))) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			response.setContentType("text/plain;charset=utf-8");
			response.getWriter().print("권한없음 - 작업을 수행할 권한이 없습니다");
		} else {
			HttpSession session = request.getSession();
			session.setAttribute("msg", "작업을 수행할 권한이 없습니다");
			response.sendRedirect("/");
		}
	}
}
