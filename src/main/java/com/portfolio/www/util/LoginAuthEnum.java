package com.portfolio.www.util;

import lombok.Getter;

@Getter
public enum LoginAuthEnum {
	BOARD("/board"),
	DEVELOP("/develop"),
	SUPPORT("/support"),
	SALES("/sales"),
	PROD("/prod"),
	QA("/qa");
	
	private String url;

	LoginAuthEnum(String url) {
		this.url = url;
	} 

}
