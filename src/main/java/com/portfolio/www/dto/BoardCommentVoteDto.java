package com.portfolio.www.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BoardCommentVoteDto {
	private int commentSeq;
	private int memberSeq;
	private String isLike;
	private String regDtm;
	private String ip;
	
	
	public BoardCommentVoteDto(int commentSeq, int memberSeq, String isLike, String ip) {
		this.commentSeq = commentSeq;
		this.memberSeq = memberSeq;
		this.isLike = isLike;
		this.ip = ip;
	}
}
