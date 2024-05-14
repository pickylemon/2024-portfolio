package com.portfolio.www.util;

import java.io.File;

import lombok.Getter;

/**
 * 파일 다운로드시, 
 * abstractPath를 기준으로 한 fileName이 아닌
 * 업로드 시의 원본 이름을 그대로 쓰고 싶어서 만든 클래스
 */
@Getter
public class CustomFile extends File {
	
	private File file;
	private String orgFileNm;
	
	public CustomFile(String abstractPath, String orgFileNm) {
		super(abstractPath);
		this.orgFileNm = orgFileNm;
	}
}
