package com.example.demo.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.dao.MemberDao;
import com.example.demo.entity.Member;
import com.example.demo.security.Account;

@Service
public class MyUserDetailsService implements UserDetailsService{
	
	@Autowired
	private MemberDao memberDao;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Optional<Member> memberOptional = memberDao.findById(username);
		
		Member member = memberOptional.orElseThrow(()-> new UsernameNotFoundException("사용자를 찾을 수 없습니다"));
		
		Account account = new Account(member.toDto());
		
		return account;
	}
}
