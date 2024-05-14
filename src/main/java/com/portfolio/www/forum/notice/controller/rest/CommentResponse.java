package com.portfolio.www.forum.notice.controller.rest;

import java.util.List;

import com.portfolio.www.dto.BoardCommentDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentResponse {
	private String msg;
	private List<BoardCommentDto> commentList;
}
