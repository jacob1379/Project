package com.example.demo.dao;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.dto.BoardDto;
import com.example.demo.entity.Board;

@Mapper
public interface BoardDao {
	// 글 저장
	public Integer save(Board board);
	
	// 글 목록(페이징)
	public List<BoardDto.ListDto> findAll(Map<String, Object> map);
	
	// 글 읽기
	public Optional<BoardDto.ReadDto> find(Integer bno);
	
	// 글 변경
	public Integer update(Board board);
	
	// 글 삭제
	public Integer delete(Integer bno);
	
	// 글 개수
	public Integer count(String writer);
	
	// 글 변경,삭제 전 글쓴이 확인 -> 글쓴이가 없다면 글이 없음
	public Optional<String> findWriter(Integer bno);
	
	// 좋아요 개수
	public Integer findGoodCount(Integer bno);
	
	// 싫어요 개수
	public Integer findBadCount(Integer bno);
}	
