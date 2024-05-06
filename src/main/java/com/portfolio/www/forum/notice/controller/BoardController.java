package com.portfolio.www.forum.notice.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.portfolio.www.dto.BoardDto;
import com.portfolio.www.dto.BoardVoteDto;
import com.portfolio.www.service.BoardService;
import com.portfolio.www.util.PageHandler;
import com.portfolio.www.util.SearchCondition;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
@Slf4j
public class BoardController {
	private final BoardService boardService;
	
	@GetMapping("/listPage.do") //게시판 페이지(한 페이지) 요청
	public String listPage(Integer page, Integer size, Model model, HttpServletRequest request) {	
//		log.info("\n params={} \n", params);
		//현재 페이지(currPage)와 페이지 크기(pageSize)정보가 넘어오지 않았을 경우의 방어코딩
//		if(!params.containsKey("page")) {
//			params.put("page", "1"); //가장 첫 페이지
//		}
//		if(!params.containsKey("size")) {
//			params.put("size", "10"); //pageSize = 10
//		}
		if(ObjectUtils.isEmpty(page)) {
			page = 1;
		}
		if(ObjectUtils.isEmpty(size)) {
			size = 10;
		}
		
//		Integer page = Integer.parseInt(params.get("page"));
//		Integer size = Integer.parseInt(params.get("size"));
		PageHandler ph = new PageHandler(page, size);
		SearchCondition sc = new SearchCondition(); //나중에 검색조건 추가하면 생성자 수정필요
		
		List<BoardDto> list = boardService.getList(ph, sc);
		
		log.info("\n pageHandler={} \n",ph);
		model.addAttribute("ph", ph);
		model.addAttribute("sc", sc);
		model.addAttribute("list", list);
		
		return "forum/notice/list";
	}
	
	@GetMapping("/writePage.do") //글쓰기 페이지 요청
	public String writePage() {
		
		return "forum/notice/write";
	}
	
	@GetMapping("/readPage.do") //개별 게시글 읽기 요청
	public String readPage(Integer boardSeq, Integer boardTypeSeq, HttpSession session, Model model) {
		int memberSeq = (int)session.getAttribute("memberSeq");
		String isLike = "";
		
		BoardDto boardDto = boardService.getPost(boardSeq);
		BoardVoteDto dto = boardService.getVote(boardSeq, boardTypeSeq, memberSeq);
		if(!ObjectUtils.isEmpty(dto)) {
			isLike = dto.getIsLike();
		}
		model.addAttribute("boardDto", boardDto);
		model.addAttribute("isLike", isLike);
		
		return "forum/notice/read";
	}
	
	
	
}
