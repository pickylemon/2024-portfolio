package com.portfolio.www.forum.notice.controller.rest;

import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.portfolio.www.dto.BoardCommentDto;
import com.portfolio.www.service.BoardCommentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/forum/notice")
public class RestCommentController {
	private final BoardCommentService commentService;
	

	/**
	 * 댓글을 저장하고 갱신된 댓글리스트를 반환한다.
	 * msg와 List를 함께 반환하기 위해 한번 객체로 한번 감싸서 반환
	 * 근데 addComment에서 댓글 저장과 댓글리스트 가져오기를 모두 하는게 맞나?
	 * 메서드를 분리해야하나?
	 * 
	 * 일단은 나눈 버전으로 구현
	 * @param commentDto
	 * @param session
	 * @return
	 */
	@PostMapping("/reply.do")
	public ResponseEntity<String> addComment(@RequestBody BoardCommentDto commentDto, HttpSession session) {
		int memberSeq = (int)session.getAttribute("memberSeq");
		commentDto.setMemberSeq(memberSeq);
		log.info("commentDto={}",commentDto);
		
		int code = commentService.addComment(commentDto);
		
		if(code == 1) {
			return ResponseEntity.ok().body("COMMENT_SAVE_OK");
		} else {
			return ResponseEntity.badRequest().body("COMMENT_SAVE_FAIL");
		}

	}
}
