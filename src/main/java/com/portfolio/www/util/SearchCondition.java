package com.portfolio.www.util;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SearchCondition {
	//제목과 회원 아이디로 검색
	private String title;
	private String memberId;
}
