package com.portfolio.www.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.portfolio.www.service.BoardService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class BoardController {
	private final BoardService boardService;
	
	@GetMapping("/listPage.do") //게시판 페이지 요청
	public String listPage() {
		//페이징 정보가 필요하다.
		//현재 몇 페이지인지
		return "forum/notice/list";
	}
	
	@GetMapping("/writePage.do") //글쓰기 페이지 요청
	public String writePage() {
		
		return "forum/notice/write";
	}
	@GetMapping("/readPage.do") //개별 게시글 읽기 요청
	public String readPage() {
		
		return "forum/notice/read";
	}
}
