package com.portfolio.www.dto;

import java.io.Serializable;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(of = {"title", "content"})
@AllArgsConstructor
public class BoardSaveDto implements Serializable {
	//제목, 내용, 파일
	@NotEmpty
	private String title;
	@NotEmpty
	private String content;
//	@NotEmpty
	private int boardTypeSeq;
//	@NotEmpty
	private int regMemberSeq;
	
	
	
}
