package com.portfolio.www.util;

import java.io.File;

import lombok.Getter;

@Getter
public class CustomFile extends File {
	
	private File file;
	private String orgFileNm;
	
	public CustomFile(String abstractPath, String orgFileNm) {
		super(abstractPath);
		this.orgFileNm = orgFileNm;
	}


}
