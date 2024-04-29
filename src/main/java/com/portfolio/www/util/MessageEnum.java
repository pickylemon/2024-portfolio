package com.portfolio.www.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MessageEnum {
	SUCCESS("0000", "성공"),
	USER_NOT_FOUND("9000", "존재하지 않는 회원입니다."),
	WRONG_PASSWORD("9001", "비밀번호가 틀렸습니다"),
	ALREADY_EXISTS("9002", "이미 존재하는 아이디입니다.");

	
	private final String code;
	private final String message;

}
