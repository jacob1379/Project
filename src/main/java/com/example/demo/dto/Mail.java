package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder
@AllArgsConstructor
@Accessors(chain=true) //setter로 각각 setMethod를 여러 줄로 생성 할 필요없이 chain형태로 이어서 원하는 setMethod를 생성할 수 있다.(빌더랑 비슷)
public class Mail {
	private String from;
	private String to;
	private String subject;
	private String text;
}
