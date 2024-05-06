package com.portfolio.www.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardVoteDto {
	private int boardSeq;
	private int boardTypeSeq;
	private int memberSeq;
	private String ip;
	private String isLike;
	private String regDtm;
}
