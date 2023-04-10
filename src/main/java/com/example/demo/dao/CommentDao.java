package com.example.demo.dao;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

import com.example.demo.dto.CommentDto;
import com.example.demo.entity.Comment;

@Mapper
public interface CommentDao {
	
	// 댓글 쓰기
	@SelectKey(statement="select comments_seq.nextval from dual", resultType=Integer.class, keyProperty="cno", before=true)
	@Insert("insert into comments(cno,content,writer,bno) values(#{cno},#{content},#{writer},#{bno})")
	public Integer save(Comment comment);
	
	// 댓글 출력
	@Select("select cno,content,writer,writeTime from comments where bno=#{bno}")
	public List<CommentDto.Read> findByBno(Integer bno);
	
	// 글쓴이 확인
	@Select("select writer from comments where cno=#{cno} and rownum<=1")
	public Optional<String> findWriterById(Integer cno);
	
	// 글번호로 댓글 삭제
	@Delete("delete from comments where bno=#{bno}")
	public Integer deleteByBno(Integer bno);
	
	// 댓글번호로 댓글 삭제
	@Delete("delete from comments where cno=#{cno}")
	public Integer deleteByCno(Integer cno);
}
