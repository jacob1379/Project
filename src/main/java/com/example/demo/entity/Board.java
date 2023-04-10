package com.example.demo.entity;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class Board {
	private Integer bno;
	private String title;
	private String content;
	private String writer;
	private LocalDateTime writeTime;
	private Integer readCount;
	private Integer goodCount;
	private Integer badCount;
	private Integer commentCount;
	
	// 필드를 변경하는 애매한 의미를 가진 setter대신 명확한 역할을 가진 메소드를 사용
	public Board addWriter(String loginId) {
		this.writer = loginId;
		return this;
	}
}
