package com.portfolio.www.util;

import lombok.Data;

@Data
public class SearchCondition {
	//제목과 회원 아이디로 검색
	private final String title;
	private final String memberId;
}
