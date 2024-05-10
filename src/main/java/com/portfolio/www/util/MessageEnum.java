package com.portfolio.www.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MessageEnum {
	SUCCESS("0000", "성공"),
	USER_NOT_FOUND("9000", "존재하지 않는 회원입니다."),
	WRONG_PASSWORD("9001", "비밀번호가 틀렸습니다"),
	ALREADY_EXISTS("9002", "이미 존재하는 아이디입니다."),
	POST_SUCCESS("1", "게시글이 성공적으로 등록되었습니다"),
	POST_FAIL("-1", "게시글 작성에 실패했습니다."),
	MODIFY_SUCCESS("1", "게시물 수정에 성공하였습니다"),
	MODIFY_FAIL("-1", "게시물 수정에 실패했습니다");

	
	private final String code;
	private final String message;

}
