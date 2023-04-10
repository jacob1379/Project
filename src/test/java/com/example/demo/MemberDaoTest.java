package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.dao.MemberDao;

@SpringBootTest
public class MemberDaoTest {
	@Autowired
	MemberDao dao;
}
