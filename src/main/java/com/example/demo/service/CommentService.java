package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dao.BoardDao;
import com.example.demo.dao.CommentDao;
import com.example.demo.dto.CommentDto;
import com.example.demo.entity.Board;
import com.example.demo.exception.CommentNotFoundException;
import com.example.demo.exception.JobFailException;

@Service
public class CommentService {
	
	@Autowired
	private CommentDao commentDao;
	@Autowired
	private BoardDao boardDao;
	
	@Transactional
	public List<CommentDto.Read> write(CommentDto.Write dto, String loginId) {
		commentDao.save(dto.toEntity().addWriter(loginId));
		boardDao.update(Board.builder().bno(dto.getBno()).commentCount(1).build());
		return commentDao.findByBno(dto.getBno());
	}
	
	@Transactional
	public List<CommentDto.Read> delete(CommentDto.Delete dto, String loginId) {
		String writer = commentDao.findWriterById(dto.getCno()).orElseThrow(CommentNotFoundException::new);
		if(!writer.equals(loginId))
			throw new JobFailException("삭제할 수 없습니다");
		commentDao.deleteByCno(dto.getCno());
		boardDao.update(Board.builder().bno(dto.getBno()).commentCount(0).build());
		return commentDao.findByBno(dto.getBno());
	}
}
