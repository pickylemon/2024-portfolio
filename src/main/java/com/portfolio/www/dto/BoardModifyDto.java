package com.portfolio.www.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class BoardModifyDto {
	//제목, 내용, 파일
	@NotEmpty
	private int boardSeq;
	@NotEmpty
	private String title;
	@NotEmpty
	private String content;
	@NotEmpty
	private int boardTypeSeq;
	@NotEmpty
	private int updateMemberSeq;
	private UploadFile file;

}
