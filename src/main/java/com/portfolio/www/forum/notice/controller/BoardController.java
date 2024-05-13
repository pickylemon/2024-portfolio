package com.portfolio.www.forum.notice.controller;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.portfolio.www.dto.BoardAttachDto;
import com.portfolio.www.dto.BoardDto;
import com.portfolio.www.dto.BoardSaveDto;
import com.portfolio.www.dto.BoardVoteDto;
import com.portfolio.www.service.BoardService;
import com.portfolio.www.util.PageHandler;
import com.portfolio.www.util.SearchCondition;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
//@RequestMapping("/board")
@RequestMapping("/forum")
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
	 * * pageHandler와 검색 조건을 넘겨서 해당 페이지에 맞는 게시판 목록을 반환받고 모델에 담아 뷰로 넘김
	 */
	@GetMapping("/notice/listPage.do") //게시판 페이지(한 페이지) 요청
	public String listPage(Integer page, Integer size, Model model, HttpServletRequest request) {	
		if(ObjectUtils.isEmpty(page)) {
			page = 1;
		}
		if(ObjectUtils.isEmpty(size)) {
			size = 10;
		}
		
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
	 * 게시글 제목, 내용 (BoardDto), 좋아요/싫어요(boardVoteDto), 첨부파일(boardAttachDto), 댓글(boardCommentDto)
	 * Model에 BoardDto, BoardVoteDto(의 isLike), BoardAttachDto, List<BoardCommentDto> 담아서 전달
	 * @param boardSeq
	 * @param boardTypeSeq
	 * @param session
	 * @param model
	 * @return
	 */
	@GetMapping("/notice/readPage.do") 
	public String readPage(Integer boardSeq, Integer boardTypeSeq, HttpSession session, Model model) {
		// FIXME validation 필요
		//queryparam으로 boardSeq와 boardTypeSeq가 넘어가야하는데 param없이 readPage.do로 접근했을 경우
		//500에러가 터짐. 
		
		int memberSeq = (int)session.getAttribute("memberSeq");
		String isLike = "";
		
		BoardDto boardDto = boardService.getPost(boardSeq);
		BoardVoteDto voteDto = boardService.getVote(boardSeq, boardTypeSeq, memberSeq);
		List<BoardAttachDto> attFileList = boardService.getAttFileInfoList(boardSeq, boardTypeSeq);
		//List<BoardCommentDto> comments = boardCommentService.getList(boardSeq, boardTypeSeq);
		
		if(!ObjectUtils.isEmpty(voteDto)) {
			//해당 게시글에 대한 좋아요/싫어요 투표 결과가 있으면 꺼내기
			isLike = voteDto.getIsLike();
		}
		model.addAttribute("boardDto", boardDto);
		model.addAttribute("isLike", isLike);
		model.addAttribute("attFileList", attFileList);
//		model.addAttribute("comments", comments);
		
		return "forum/notice/read";
	}
	
	/**
	 * 게시판 수정 페이지 요청
	 * @param boardSeq
	 * @param model
	 * @return
	 */
	@GetMapping("/notice/{boardTypeSeq}/{boardSeq}/modifyPage.do")
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
	@GetMapping("/notice/writePage.do") 
	public String writePage(Model model) {
		model.addAttribute("page", "write");
		return "forum/notice/write";
	}
	
	
	/**
	 * 글쓰기 데이터를 form태그 요청으로 받을 경우(parameters형태로 전달 받음)
	 * rest 패키지에는 비동기방식으로 전달 받을 경우의 메서드 작성함
	 * @param params
	 * @param mReq
	 * @return
	 */
	@PostMapping("/notice/{boardTypeSeq}/write.do")
	public String write(
			@PathVariable("boardTypeSeq") int boardTypeSeq,
			@RequestParam HashMap<String, String> params,
			HttpSession session,
			MultipartFile[] attFiles, Model model, RedirectAttributes rattr) {
		//MultipartHttpServletRequest에 대해서도 알아보기(ex.getFileMap)
		
		params.entrySet().forEach(System.out::println);
		
		log.info("attFiles.length={}, attFilesArr={}", attFiles.length, Arrays.toString(attFiles));
		//게시글 관련 데이터
		int regMemberSeq = (int)session.getAttribute("memberSeq");
		String title = params.get("title");
		String content = params.get("trumbowyg-demo");
		BoardSaveDto saveDto = new BoardSaveDto(title, content, boardTypeSeq, regMemberSeq);
		
		//게시글 데이터와 파일 데이터를 service에 넘김
		int code = boardService.savePost(saveDto, attFiles);
		if(code == 1) {
			//성공적으로 게시글+첨부파일이 등록된 경우
			rattr.addFlashAttribute("msg", "게시글 등록이 성공적으로 완료되었습니다.");
			return "redirect:/forum/notice/listPage.do"; //목록으로 이동
		} else if (code == -1) {
			//게시글 등록에 예외 발생
			model.addAttribute("msg", "게시글 등록에 실패하였습니다");
		} else { //코드 -2 : 첨부파일을 물리적으로 저장하는데 예외 발생
			model.addAttribute("msg", "첨부파일 등록에 오류가 발생해 게시글 등록에 실패하였습니다.");
		}
		//게시글 등록에 실패한 경우에도, 사용자가 입력한 정보가 사라지면 안되고 그대로 다시 뷰에 뿌려주어야함
			model.addAttribute("boardSaveDto", saveDto);
			return "forum/notice/write";
	}
	
	@GetMapping("/download.do")
	public String download(@RequestParam(required=false) Integer attachSeq, Model model) {
		if(attachSeq == null) { 
			//queryString없이 온 요청은 전체 다운로드
			//해당 게시글의 모든 파일을 압축해서 다운로드 
			
		} else { //single 첨부파일 다운로드
			log.info("attachSeq={}", attachSeq);
			
			BoardAttachDto attachDto = boardService.getSingleAttFileInfo(attachSeq);
			File file = new File(attachDto.getSavePath());
			Map<String, Object> fileInfo = new HashMap();
			
			fileInfo.put("orgFileNm", attachDto.getOrgFileNm());
			fileInfo.put("file", file);
			
			model.addAttribute("fileInfo", fileInfo);
			
		}
		
		return "fileDownloadView"; //beanNameViewResolver에 의해 view이름으로 해석됨
	}
}
