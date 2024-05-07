package com.portfolio.www.forum.notice.controller.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.portfolio.www.service.BoardService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
public class RestNoticeController {
	private final BoardService service;
	
	/**
	 * 1. param으로 전달받은 boardSeq, boardTypeSeq, isLike(복합 PK)로
	 * 	  DB에서 이전 투표 결과가 있는지 조회한다.
	 * 2. DB에 이전 투표 결과가 없다 -> 테이블에 insert
	 * 3. DB에 이전 투표 결과가 있다.
	 * 	3-1. 이전 투표 결과와 param으로 전달된 isLike가 일치(ex. 좋아요를 두번 누름)
	 *      -> 이전 투표 결과를 삭제(토글처럼 작용)
	 *  3-2. 이전 투표 결과와 param으로 전달된 isLike가 불일치
	 *      -> 이전 투표 결과의 isLike를 param으로 받은 isLike로 update    
	 *      
	 *  비즈니스 로직인 것 같아서 Service로 옮김
	 */
	//이 요청 역시 먼저 로그인 Filter에 의해 걸러져야 한다.
	@GetMapping("/forum/notice/{boardTypeSeq}/{boardSeq}/vote.do")
//	public ResponseEntity<VoteResponse> vote(
	public VoteResponse vote(
			@PathVariable("boardSeq") int boardSeq, 
			@PathVariable("boardTypeSeq") int boardTypeSeq,
			@RequestParam("isLike") String isLike,
			@RequestParam("thumb") boolean thumb,
			HttpServletRequest request) {
		
		log.info("\n boardSeq={}, boardTypeSeq={}, isLike={}, thumb={} \n", boardSeq, boardTypeSeq, isLike, thumb);
		//세션으로부터 memberSeq 얻어오기
		HttpSession session = request.getSession();
		int memberSeq = (int)session.getAttribute("memberSeq");
		
		//ip 주소 얻기
		String ip = request.getRemoteAddr();
		
		//좋아요(싫어요) 요청에 대한 코드만 반환받는다.
		//구체적인 처리는 서비스계층에게 위임
		int code = service.vote(boardSeq, boardTypeSeq, memberSeq, isLike, ip);
	
		log.info("code={}", code);
		
//		return new ResponseEntity<VoteResponse>(new VoteResponse(code, thumb), HttpStatus.OK);
		return new VoteResponse(code, thumb);
	}
	
}
