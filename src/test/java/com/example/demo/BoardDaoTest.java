package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.dao.BoardDao;
import com.example.demo.entity.Board;

@SpringBootTest
public class BoardDaoTest {
	@Autowired
	private BoardDao dao;
	
	//@Test
	public void diTest() {
		assertNotNull(dao);
	}
	
	//@Test
	public void saveTest() {
		Board board = Board.builder().title("aaaa").content("bbbb").writer("cccc").build();
		assertEquals(1, dao.save(board));
	}
	
	//@Test
	public void findAllTest() {
		Map<String, Object> map = new HashMap<>();
		map.put("writer", null);
		map.put("start", 1);
		map.put("end", 3);
		assertEquals(3, dao.findAll(map).size());
		
		map.put("writer", "summer");
		map.put("start", 1);
		map.put("end", 3);
		assertEquals(0, dao.findAll(map).size());
	}
	
	//@Test
	public void findTest() {
		assertEquals(true, dao.find(1).isPresent());
		assertEquals(true, dao.find(11).isEmpty());
	}
	
	//@Test
	public void updateTest() {
		assertEquals(1, dao.update(Board.builder().bno(1).title("변경").content("하지용").build()));
		assertEquals(1, dao.update(Board.builder().bno(1).readCount(2).build()));
		assertEquals(1, dao.update(Board.builder().bno(1).goodCount(2).build()));
		assertEquals(1, dao.update(Board.builder().bno(1).badCount(2).build()));
	}
	
	//@Test
	public void deleteTest() {
		assertEquals(1, dao.delete(3));
		assertEquals(0, dao.delete(4));
	}
	
	//@Test
	public void countTest() {
		assertEquals(2, dao.count("cccc"));
		assertEquals(0, dao.count("spring"));
		assertEquals(2, dao.count(null));
	}
	
	@Test
	public void findWriterTest() {
		Assertions.assertThrows(NoSuchElementException.class,
				()->dao.findWriter(3).get());
		assertEquals("cccc", dao.findWriter(1).get());
	}
}
