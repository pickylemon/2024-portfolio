package com.portfolio.www.entity;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Board {
	private Integer boardSeq;
	private Integer boardTypeSeq;
	private Integer memberSeq;
	private String isLike;
	private String ip;
	private String regDtm;
	
	
	public Board(Integer boardSeq, Integer boardTypeSeq, Integer memberSeq, String isLike, String ip) {
		this.boardSeq = boardSeq;
		this.boardTypeSeq = boardTypeSeq;
		this.memberSeq = memberSeq;
		this.isLike = isLike;
		this.ip = ip;
		this.regDtm = LocalDateTime.now().toString();
	}


	public void setIsLike(String isLike) {
		this.isLike = isLike;
	}


	public void setIp(String ip) {
		this.ip = ip;
	}


	public void setRegDtm(String regDtm) {
		this.regDtm = regDtm;
	}
	
}
