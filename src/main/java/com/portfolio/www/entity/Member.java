package com.portfolio.www.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Member {
	private Integer memberSeq;
	private String memberId;
	private String passwd;
	private String email;
	private String autyYn;
	private String pwdChngDtm;
	private String joinDtm;

}
