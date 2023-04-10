package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.dto.BoardDto;
import com.example.demo.entity.Board;
import com.example.demo.exception.BoardNotFoundException;
import com.example.demo.exception.JobFailException;
import com.example.demo.service.BoardService;

@SpringBootTest
public class BoardServiceTest {
	@Autowired
	private BoardService service;
	
	@Test
	public void writeTest() {
		for(int i=0; i<100; i++) {
			BoardDto.WriteDto dto = BoardDto.WriteDto.builder().title("페이징 테스트").content("테스트중인 더미 데이터입니다.").build();
			Board board = service.write(dto, "FINDJOB1");
			assertNotNull(board);
		}
	}
	
	//@Test
	public void readTest() {
		// 글을 읽었는데 없다
		Assertions.assertThrows(BoardNotFoundException.class, () -> service.read(10, "cccc"));
		// 글을 읽었는데 있다
		assertEquals(0, service.read(2, null).getReadCount());
		// 글쓴이어서 조회수가 변경되지 않는다
		assertEquals(0, service.read(2, "cccc").getReadCount());
		// 글쓴이가 아니라 조회수가 변경된다
		assertEquals(1, service.read(2, "bbbb").getReadCount());
	}
	
	//@Test
	public void listTest() {
		assertEquals(10, service.list(1, "spring").getPagesize());
	}
	
	//@Test
	public void updateTest() {
		BoardDto.UpdateDto d1 = BoardDto.UpdateDto.builder().bno(3).title("ab").content("hello").build();
		BoardDto.UpdateDto d2 = BoardDto.UpdateDto.builder().bno(2).title("ab").content("hello").build();
		Assertions.assertThrows(BoardNotFoundException.class, () -> service.update(d1, "cccc"));
		Assertions.assertThrows(JobFailException.class, () -> service.update(d2, "aaaa"));
		assertEquals(1, service.update(d2, "cccc"));
	}
	
	//@Test
	public void deleteTest() {
		Assertions.assertThrows(BoardNotFoundException.class, () -> service.delete(3, "spring"));
		Assertions.assertThrows(JobFailException.class, () -> service.delete(4, "summer"));
		assertEquals(1, service.delete(4, "spring"));
	}
}
