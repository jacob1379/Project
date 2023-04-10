package com.example.demo.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MemberBoardDao {
	@Select("select count(*) from member_board where bno=#{bno} and username=#{username} and rownum<=1")
	public Boolean existsById(Map<String, Object> map);
	
	@Insert("insert into member_board values(#{bno}, #{username})")
	public Integer save(Map<String, Object> map);
}
