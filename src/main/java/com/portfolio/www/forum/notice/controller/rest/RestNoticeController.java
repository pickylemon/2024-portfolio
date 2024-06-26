package com.portfolio.www.forum.notice.controller.rest;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.portfolio.www.dto.BoardSaveDto;
import com.portfolio.www.service.BoardService;
import com.portfolio.www.util.MessageEnum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/forum/notice")
@Slf4j
@RequiredArgsConstructor
public class RestNoticeController {
	private final BoardService boardService;
	
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
	@GetMapping("/{boardTypeSeq}/{boardSeq}/vote.do")
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
		int code = boardService.vote(boardSeq, boardTypeSeq, memberSeq, isLike, ip);
	
		log.info("code={}", code);
		
//		return new ResponseEntity<VoteResponse>(new VoteResponse(code, thumb), HttpStatus.OK);
		return new VoteResponse(code, thumb);
	}
	
	/**
	 * 게시물 저장 요청 
	 * @return
	 */
	@PostMapping("/writePage.do")
	public BoardResponse save(@RequestPart(value = "boardSaveDto") BoardSaveDto boardSaveDto, 
								@RequestPart(value = "files", required = false) MultipartFile[] files, 
								HttpSession session, HttpServletRequest request) {
		//1. TODO dto에 대한 validation (추후 처리) 
		//2. BoardService호출 
		//로그인이 전제되어야지만 게시글 작성이 가능하다.
		//즉, 로그인 상태라면 세션에 memberSeq가 항상있는데도 null체크를 해야할까?
		
		log.info("content-type = {}", request.getHeader("content-type"));
		log.info("content-type = {}", request.getContentType());
		
		
		int memberSeq = (int)session.getAttribute("memberSeq");
		boardSaveDto.setBoardTypeSeq(1); //지금은 하드코딩인데, 원래는 어디로부터 가져와야 하지?
		log.info("memberSeq={}",memberSeq);
		log.info("boardSaveDto={}",boardSaveDto);
		log.info("files={}", Arrays.toString(files));

		int code = boardService.savePost(boardSaveDto, files);

		
		//4. 게시글 업로드 결과에 따라 
		//4-1. 게시글 업로드 성공하면 게시판 1페이지로 요청 (PRG 패턴 적용)
		//4-2. 게시글 업로드 실패하면 다시 게시글 작성 페이지로 이동(+작성 내용을 모델에 담아서)
		if (code == 1) {
			return new BoardResponse(MessageEnum.POST_SUCCESS.getCode(), MessageEnum.POST_SUCCESS.getMessage());
//			return new ResponseEntity<SaveResponse>(
//					new SaveResponse(
//							MessageEnum.POST_SUCCESS.getCode(), MessageEnum.POST_SUCCESS.getMessage())
//					,HttpStatus.OK);

		} else {
			return new BoardResponse(
					MessageEnum.POST_FAIL.getCode(), MessageEnum.POST_FAIL.getMessage());
//			return new ResponseEntity<SaveResponse>(
//					new SaveResponse(
//							MessageEnum.POST_FAIL.getCode(), MessageEnum.POST_FAIL.getMessage())
//					, HttpStatus.BAD_REQUEST);
		}
		
	}
	
	
	/**
	 * 게시글 수정
	 * @param boardModifyDto
	 * @return
	 */
//	@PatchMapping("/{boardTypeSeq}/{boardSeq}/modifyPage.do")
//	public BoardResponse modfiy(@RequestBody BoardModifyDto boardModifyDto) {
//		//게시글 수정
//		log.info("\n boardModifyDto={} \n", boardModifyDto);
//		//게시글 작성자와 수정자의 memberSeq가 다르면 수정 불가능하게 막기
//		int code = boardService.modify(boardModifyDto);
//		if (code == 1) {
//			return new BoardResponse(MessageEnum.MODIFY_SUCCESS.getCode(), MessageEnum.MODIFY_SUCCESS.getMessage());
//		} else {
//			return new BoardResponse(
//					MessageEnum.MODIFY_FAIL.getCode(), MessageEnum.MODIFY_FAIL.getMessage());
//		}
//	}
	
	
	/**
	 * 게시물 삭제(게시글과 저장된 파일, 파일 정보 모두 삭제)
	 * @param boardSeq
	 * @return
	 */
	@DeleteMapping("/{boardTypeSeq}/{boardSeq}/deletePage.do")
	public BoardResponse delete(
			@PathVariable("boardSeq") Integer boardSeq, 
			@PathVariable("boardTypeSeq") Integer boardTypeSeq) {
		//게시글 삭제
		log.info("\n boardSeq={} \n", boardSeq);
		int code = boardService.delete(boardSeq, boardTypeSeq);
		
		if (code == 1) {
			return new BoardResponse(MessageEnum.DEL_SUCCESS.getCode(), MessageEnum.DEL_SUCCESS.getMessage());
		} else {
			return new BoardResponse(
					MessageEnum.DEL_FAIL.getCode(), MessageEnum.DEL_FAIL.getMessage());
		}
	}
	
	/**
	 * 게시물 수정 페이지 - 개별 파일 삭제 요청
	 * @param attachSeq
	 * @return
	 */
	@DeleteMapping("/{attachSeq}/deleteFile.do")
	public ResponseEntity<String> deleteFile(@PathVariable("attachSeq") Integer attachSeq) {
		int code = boardService.deleteFile(attachSeq);
		if(code == 1) {
			return ResponseEntity.ok("FILE_DEL_OK");
		} 
		return ResponseEntity.badRequest().body("FILE_DEL_FAIL");
	}
}
