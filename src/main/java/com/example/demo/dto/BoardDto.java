package com.example.demo.dto;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import com.example.demo.entity.Board;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access=AccessLevel.PACKAGE)
public class BoardDto {
	
	@Data
	@Builder
	public static class WriteDto {
		@NotEmpty(message="제목을 필수 입력입니다.")
		private String title;
		@NotEmpty(message="제목은 필수 입력입니다.")
		private String content;
		
		public Board toEntity() {
			return Board.builder().title(title).content(content).build();
		}
	}
	
	@Data
	public static class ListDto {
		private Integer bno;
		private String title;
		private String writer;
		@JsonFormat(pattern="yyyy-MM-dd")
		private LocalDateTime writeTime;
		private Integer readCount;
		private Integer commentCount;
	}
	
	@Data
	@AllArgsConstructor
	public static class Page {
		private Integer pageno;
		private Integer pagesize;
		private Integer totalCount;
		private Collection<ListDto> boardList;
	}
	
	@Data
	public static class ReadDto {
		private Integer bno;
		private String title;
		private String content;
		private String writer;
		@JsonFormat(pattern="yyyy-MM-dd")//yyyy-MM-dd hh:mm:ss
		private LocalDateTime writeTime;
		private Integer readCount;
		private Integer goodCount;
		private Integer badCount;
		private Integer commentCount;
		private List<CommentDto.Read> comments;
	}
	
	@Data
	@Builder
	public static class UpdateDto {
		@NotNull(message="글 번호는 필수 입력입니다.")
		private Integer bno;
		@NotEmpty(message="글 제목은 필수 입력입니다.")
		private String title;
		@NotEmpty(message="글 내용은 필수 입력입니다.")
		private String content;
		
		public Board toEntity() {
			return Board.builder().bno(bno).title(title).content(content).build();
		}
		
	}
	
}
