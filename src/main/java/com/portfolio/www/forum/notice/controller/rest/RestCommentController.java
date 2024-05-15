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
	 * 메서드를 분리해야하나? -> 일단은 댓글 등록 메서드에서는 등록만 담당하기로.
	 * 근데 post메서드이기때문에 get으로 보내는게 맞을것 같다.
	 * 새로고침하면 댓글이 계속 등록되는데 이건 의도하는 동작이 아님.
	 * js에서 성공 콜백함수 내에서 댓글 등록 성공시 다시 해당 page로 get요청보냄
	 * 
	 * 일단은 나눈 버전으로 구현
	 * @param commentDto
	 * @param session
	 * @return
	 */
	@PostMapping("/reply.do")
	public ResponseEntity<CommentResponse> addComment(@RequestBody BoardCommentDto commentDto, HttpSession session) {
		int memberSeq = (int)session.getAttribute("memberSeq");
		commentDto.setMemberSeq(memberSeq);
		log.info("commentDto={}",commentDto);
		
		int code = commentService.addComment(commentDto);
		
		if(code == 1) {
			return ResponseEntity.ok().body(new CommentResponse(code, "댓글이 성공적으로 등록되었습니다."));
		} else {
			return ResponseEntity.badRequest().body(new CommentResponse(code, "댓글 등록에 실패했습니다."));
		}

	}
}
