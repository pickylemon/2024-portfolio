package com.portfolio.www.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BoardCommentDto {
	private int commentSeq;
	private int lvl;
	private String content;
	private int boardSeq;
	private int boardTypeSeq;
	private int memberSeq;
	private Integer parentCommentSeq;
	private String regDtm;
	private String updateDtm;
	private String deleteDtm;
}
