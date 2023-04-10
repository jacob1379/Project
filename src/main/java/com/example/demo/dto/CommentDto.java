package com.example.demo.dto;

import java.time.LocalDateTime;

import com.example.demo.entity.Comment;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public class CommentDto {
	
	@Data
	public static class Read {
		private Integer cno;
		private String content;
		private String writer;
		@JsonFormat(pattern="yyyy-MM-dd hh:mm:ss")
		private LocalDateTime writeTime;
	}
	
	@Data
	@Builder
	public static class Write {
		@NotNull
		private Integer bno;
		@NotEmpty
		private String content;
		public Comment toEntity() {
			return Comment.builder().bno(bno).content(content).build();
		}
	}
	
	@Data
	public static class Delete {
		@NotNull
		private Integer cno;
		@NotNull
		private Integer bno;
	}
}
