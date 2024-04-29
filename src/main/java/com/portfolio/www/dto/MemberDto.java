package com.portfolio.www.dto;

import lombok.Data;

@Data
public class MemberDto {
	private int memberSeq;
	private String memberId;
	private String passwd;
	private String memberNm;
	private String email;
	private String authYn;
	private String pwdChngDtm;
	private String joinDtm;
}
