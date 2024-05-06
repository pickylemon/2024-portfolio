package com.portfolio.www.forum.notice.controller.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.portfolio.www.dto.BoardVoteDto;
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
	 */
	//이 요청 역시 먼저 로그인 Filter에 의해 걸러져야 한다.
	@GetMapping("/forum/notice/{boardTypeSeq}/{boardSeq}/vote.do")
	public ResponseEntity<VoteResponse> vote(
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
		
		//이미 vote테이블에 값이 존재하는지?
		BoardVoteDto dto = service.getVote(boardSeq, boardTypeSeq, memberSeq);
		int code;
		
		if(ObjectUtils.isEmpty(dto)) { //투표 결과가 없으면 추가한다.
			service.addVote(boardSeq, boardTypeSeq, memberSeq, isLike, ip);
			code = 0;
		} else if (dto.getIsLike().equals(isLike)) { //이전 투표결과와 같으면 취소한다.
			service.deleteVote(boardSeq, boardTypeSeq, memberSeq, isLike);
			code = 1;
		} else {
			service.updateVote(boardSeq, boardTypeSeq, memberSeq, isLike, ip); 
			//이전 투표결과와 다르면 투표결과를 바꾼다.
			code = 2;
		}
		
		log.info("code={}", code);
		
		return new ResponseEntity<VoteResponse>(new VoteResponse(code, thumb), HttpStatus.OK);
//		return new VoteResponse(code, thumb);
	}
	
}
