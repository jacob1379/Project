package com.example.demo.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.dao.BoardDao;
import com.example.demo.dao.CommentDao;
import com.example.demo.dao.MemberBoardDao;
import com.example.demo.dto.BoardDto;
import com.example.demo.dto.BoardDto.Page;
import com.example.demo.dto.MemberBoardDto;
import com.example.demo.entity.Board;
import com.example.demo.exception.BoardNotFoundException;
import com.example.demo.exception.JobFailException;

@Service
public class BoardService {
	@Autowired
	private BoardDao dao;
	@Autowired
	private MemberBoardDao memberBoardDao;
	@Autowired
	private CommentDao commentDao;
	@Value("${zproject_f.pagesize}") //설정파일(.properties, .yml)에 설정한 내용을 주입시켜주는 어노테이션
	private Integer pagesize;
	
	// 쓰기
	public Board write(BoardDto.WriteDto dto, String loginId) {
		Board board = dto.toEntity().addWriter(loginId);
		dao.save(board);
		return board;
	}
	// 읽기
	public BoardDto.ReadDto read(Integer bno, String loginId) {
		BoardDto.ReadDto dto = dao.find(bno).orElseThrow(()->new BoardNotFoundException());
		if(loginId!=null && dto.getWriter().equals(loginId)==false) {
			dao.update(Board.builder().bno(bno).readCount(1).build());
			dto.setReadCount(dto.getReadCount()+1);
		}
		dto.setComments(commentDao.findByBno(bno));
		return dto;
	}
	
	// 목록
	public BoardDto.Page list(Integer pageno, String writer) {
		Integer totalCount = dao.count(writer);
		Integer countOfPage = (totalCount-1)/pagesize + 1;
		if(pageno>countOfPage) {
			pageno = countOfPage;
		}else if(pageno<0) {
			pageno = -pageno;
		}else if(pageno==0) {
			pageno = 1;
		}
		Integer start = (pageno-1)*pagesize+1;
		Integer end = start+pagesize-1;
		
		Map<String, Object> map = new HashMap<>();
		map.put("start", start);
		map.put("end", end);
		map.put("writer", writer);
		return new Page(pageno, pagesize, totalCount, dao.findAll(map));	
	}
	
	// 변경
	public Integer update(BoardDto.UpdateDto dto, String loginId) {
		String writer = dao.findWriter(dto.getBno()).orElseThrow(() -> new BoardNotFoundException());
		if(writer.equals(loginId)==false)
			throw new JobFailException("변경 권한이 없습니다.");
		return dao.update(dto.toEntity());
	}
	
	// 삭제
	public Integer delete(Integer bno, String loginId) {
		String writer = dao.findWriter(bno).orElseThrow(() -> new BoardNotFoundException());
		if(writer.equals(loginId)==false)
			throw new JobFailException("삭제 권한이 없습니다.");
		return dao.delete(bno);
	}
	
	// 좋아요,싫어요
	public Integer goodOrBad(MemberBoardDto dto, String loginId) {
		String writer = dao.findWriter(dto.getBno()).orElseThrow(()->new BoardNotFoundException());
		if(writer.equals(loginId))
			throw new JobFailException("좋아요/싫어요 권한이 없습니다.");
		Map<String, Object> map = new HashMap<>();
		map.put("bno", dto.getBno());
		map.put("username", loginId);
		if(memberBoardDao.existsById(map)==true) {
			if(dto.getIsGood()==true) {
				return dao.findGoodCount(dto.getBno());
			} else {
				return dao.findBadCount(dto.getBno());
				}
		} else {
			memberBoardDao.save(map);
			if(dto.getIsGood()==true) {
				dao.update(Board.builder().bno(dto.getBno()).goodCount(1).build());
				return dao.findGoodCount(dto.getBno());
			} else {
				dao.update(Board.builder().bno(dto.getBno()).badCount(1).build());
				return dao.findBadCount(dto.getBno());
			}
		}
	}
}
