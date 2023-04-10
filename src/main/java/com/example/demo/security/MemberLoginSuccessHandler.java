package com.example.demo.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import com.example.demo.dao.MemberDao;
import com.example.demo.entity.Member;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class MemberLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler{
	
	@Autowired
	private MemberDao memberDao;
	
	// 사용자가 가려던 목적지를 저장하는 객체
	private RequestCache cache = new HttpSessionRequestCache();
	// 리다이렉트 해주는 객체
	private RedirectStrategy rs = new DefaultRedirectStrategy();
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException, ServletException {
		
		// 로그인 성공하면 실패 횟수 초기화, 로그인 횟수 증가
		memberDao.update(Member.builder().username(authentication.getName()).loginFailCount(0).build());
		
		// 임시 비밀번호로 로그인하면 비밀번호 변경으로 이동
//		if(request.getParameter("password").length()>=20) {
//			HttpSession session = request.getSession();
//			session.setAttribute("msg", "임시 비밀번호 로그인");
//			rs.sendRedirect(request, response, "/member/change_password");
//		}
		
		SavedRequest req = cache.getRequest(request, response);
		if(req!=null)
			rs.sendRedirect(request, response, req.getRedirectUrl());
		else
			rs.sendRedirect(request, response, "/");
	}
}
