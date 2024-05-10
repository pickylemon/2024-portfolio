package com.portfolio.www.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(of = {"title", "content"})
public class BoardSaveDto {
	//제목, 내용, 파일
	@NotEmpty
	private String title;
	@NotEmpty
	private String content;
	@NotEmpty
	private int boardTypeSeq;
	@NotEmpty
	private int regMemberSeq;
	private UploadFile file;


}
