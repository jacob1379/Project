package com.example.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.example.demo.dto.Mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Component //개발자가 직접 작성한 Class를 Bean으로 등록하기 위한 Annotation
public class MailUtil {
	@Autowired
	private JavaMailSender javaMailSender;
	
	// 메일 설정
	private void sendMail(Mail mail) {
		MimeMessage message = javaMailSender.createMimeMessage(); //멀티파트 데이터를 처리할 수 있고, 메일의 내용을 설정
		MimeMessageHelper helper;
		
		try {
			helper = new MimeMessageHelper(message, false, "utf-8"); // false는 멀티파트 메세지를 사용하지 않겠다는 의미
			helper.setFrom(mail.getFrom());
			// 빈에 아이디 설정한 것은 단순히 smtp 인증을 받기 위해 사용함 따라서 보낸이(setFrom())는 반드시 필요
			// 보낸이와 메일주소를 받는이가 볼 때 모두 표기되길 원하면 아래 코드를 사용
			// helper.setFrom("보내는이 이름 <보내는이 아이디@도메인주소>");
			helper.setTo(mail.getTo());
			helper.setSubject(mail.getSubject());
			helper.setText(mail.getText(), true); // true는 html을 사용하겠다는 의미
			javaMailSender.send(message);
		} catch(MessagingException e) {
			e.printStackTrace();
		}
	}
	// 아이디 찾는 메일
	public void sendFindIdMail(String from, String to, String username) {
		Mail mail = Mail.builder().from(from).to(to).subject("아이디 확인 메일").build();
		String message = new StringBuffer("<p>아이디를 찾았습니다.</p>").append("<p>당신의 아이디 : ").append(username).append("</p>").toString();
		// StringBuffer 문자열을 추가하거나 변경 할 때 주로 사용하는 자료형, 마지막에 toSting()메소드를 통해 String자료형으로 변환
		sendMail(mail.setText(message));
	}
	// 임시 비밀번호 발급 메일
	public void sendResetPasswordMail(String from, String to, String password) {
		Mail mail = Mail.builder().from(from).to(to).subject("임시 비밀번호 확인 메일").build();
		String message = new StringBuffer("<p>임시 비밀번호가 발급되었습니다.</p>").append("<p>임시 비밀번호 : ").append(password).append("</p>").toString();
		sendMail(mail.setText(message));
	}
	// 회원가입 확인 메일
	public void sendJoinCheckMail(String from, String to, String checkcode) {
		Mail mail = Mail.builder().from(from).to(to).subject("가입 확인 메일").build();
		StringBuffer buf = new StringBuffer("<p>회원가입을 위한 안내 메일입니다.</p>");
		buf.append("<p>가입 확인을 위한 링크를 안내해 드립니다.</p>");
		buf.append("<p>가입 확인 : ");
		buf.append("<a href='http://localhost8087/member/check/join?checkcode=");
		buf.append(checkcode);
		buf.append("'>여기를 클릭해주세요</a></p>");
		sendMail(mail.setText(buf.toString()));
	}
}
