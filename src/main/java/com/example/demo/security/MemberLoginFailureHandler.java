package com.example.demo.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.example.demo.dao.MemberDao;
import com.example.demo.entity.Member;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class MemberLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
	
	@Autowired
	private MemberDao dao;
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		
		String username = request.getParameter("username");
		Member member = dao.findById(username).orElse(null);
		HttpSession session = request.getSession();
		
		if(exception instanceof BadCredentialsException) {	// 비밀번호 틀렸을 때
			if(member!=null) {
				Integer loginFailCount = member.getLoginFailCount();
				if(loginFailCount<5) {
					dao.update(Member.builder().username(username).loginFailCount(loginFailCount+1).build());
					session.setAttribute("msg", (loginFailCount+1) + "회 로그인에 실패했습니다.");
				}
			} else
				session.setAttribute("msg", "로그인에 실패했습니다");
		} else if(exception instanceof DisabledException) {
			session.setAttribute("msg", "블록된 계정입니다. 관리자에게 문의하세요");
		}
		
		new DefaultRedirectStrategy().sendRedirect(request, response, "/member/login");
	}
}
