package com.portfolio.www.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailDto {
	private String from;
	private String receiver;
	private String text;
	private String subject;
}
