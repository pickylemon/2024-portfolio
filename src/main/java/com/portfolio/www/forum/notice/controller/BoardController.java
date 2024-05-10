package com.portfolio.www.forum.notice.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
//@RequestMapping("/board")
@RequestMapping("/forum/notice")
@Slf4j
public class BoardController {
	private final BoardService boardService;
	
	/**
	 * 게시판 목록 페이지 요청
	 * @param page
	 * @param size
	 * @param model
	 * @param request
	 * @return
	 */
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
	

	/**
	 * 개별 게시물 읽기 요청
	 * @param boardSeq
	 * @param boardTypeSeq
	 * @param session
	 * @param model
	 * @return
	 */
	@GetMapping("/readPage.do") 
	public String readPage(Integer boardSeq, Integer boardTypeSeq, HttpSession session, Model model) {
		log.info("session.getAttribute()={}",session.getAttribute("597875B21C949A70938E6250A7D7E122")) ;
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
	
	/**
	 * 게시판 수정 페이지 요청
	 * @param boardSeq
	 * @param model
	 * @return
	 */
	@GetMapping("/{boardTypeSeq}/{boardSeq}/modifyPage.do")
	public String modifyPage(@PathVariable("boardSeq") Integer boardSeq, Model model) {
		log.info("boardSeq={}", boardSeq);
		BoardDto boardDto = boardService.getPost(boardSeq);
		model.addAttribute("boardDto", boardDto);
		model.addAttribute("page", "modify");
		
		return "forum/notice/write";
	}
	
	/**
	 * 글쓰기 페이지 요청
	 * @return
	 */
	@GetMapping("/writePage.do") 
	public String writePage(Model model) {
		model.addAttribute("page", "write");
		return "forum/notice/write";
	}
	
}
