package com.portfolio.www.dto;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Alias("MemberDto")
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
