package com.portfolio.www.service;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.portfolio.www.dto.BoardAttachDto;
import com.portfolio.www.dto.BoardDto;
import com.portfolio.www.dto.BoardModifyDto;
import com.portfolio.www.dto.BoardSaveDto;
import com.portfolio.www.dto.BoardVoteDto;
import com.portfolio.www.exception.FileSaveException;
import com.portfolio.www.repository.BoardAttachRepository;
import com.portfolio.www.repository.BoardRepository;
import com.portfolio.www.util.FileUtil;
import com.portfolio.www.util.PageHandler;
import com.portfolio.www.util.SearchCondition;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {
	private final BoardRepository boardRepository;
	private final BoardAttachRepository boardAttachRepository;
	private final FileUtil fileUtil;
	
	/*
	 * 게시글 리스트 가져오기(첨부파일 갯수, 댓글 갯수 목록도 포함시켜서)
	 */
	public List<BoardDto> getList(PageHandler ph, SearchCondition sc) {
		List<BoardDto> list = boardRepository.getList(ph, sc);
		ph.calculatePage(boardRepository.getTotalCnt(sc));
		
		//각 게시글에 첨부파일 갯수와 댓글 갯수를 조회해서 dto에 set
		for(BoardDto dto : list) {
			int boardSeq = dto.getBoardSeq();
			int boardTypeSeq = dto.getBoardTypeSeq();
			int attFileCnt = boardAttachRepository.count(boardSeq, boardTypeSeq);
//			int commentCnt = boardCommentRepository.count(dto);
			dto.setAttFileCnt(attFileCnt);
//			dto.setBoardCommentCnt(commentCnt);
		}
		return list;
	}
	
	public int getTotalCnt(SearchCondition sc) {
		return boardRepository.getTotalCnt(sc);
	}
	
	public BoardDto getPost(Integer boardSeq, Integer boardTypeSeq) {
		return boardRepository.getOne(boardSeq, boardTypeSeq);
	}
	
	/**
	 * 해당 게시글(boardSeq + boardTypeSeq) 의 모든 첨부파일 정보 가져오기
	 * @param boardSeq
	 * @param boardTypeSeq
	 * @return
	 */
	public List<BoardAttachDto> getAttFileInfoList(int boardSeq, int boardTypeSeq) {
		return boardAttachRepository.getList(boardSeq, boardTypeSeq);
	}
	
	/**
	 * 특정 첨부파일의 정보 가져오기
	 * @param attachSeq
	 * @return 
	 */
	public BoardAttachDto getSingleAttFileInfo(Integer attachSeq) {
		return boardAttachRepository.getOne(attachSeq);
	}
	
	
	/**
	 * 
	 * 좋아요, 싫어요 반영하기
	 */
	
	//이미 이전 투표 결과가 있는지
	public BoardVoteDto getVote(int boardSeq, int boardTypeSeq, int memberSeq) {
		BoardVoteDto dto = null;
		try {
			dto = boardRepository.getVote(boardSeq, boardTypeSeq, memberSeq);
		} catch (EmptyResultDataAccessException e) { 
			log.info(e.getMessage()); //투표 결과가 없을 때는 null을 반환
		} 
		return dto;
	}
	
	//비즈니스 로직
	//이미 vote테이블에 값이 존재하는지?
	@Transactional
	public int vote(int boardSeq, int boardTypeSeq, int memberSeq, String isLike, String ip) {
		BoardVoteDto dto = getVote(boardSeq, boardTypeSeq, memberSeq);
		
		int code = 0;
		
		if(ObjectUtils.isEmpty(dto)) { //테이블에 투표 결과가 없으면 추가
			boardRepository.addVote(boardSeq, boardTypeSeq, memberSeq, isLike, ip);
			code = 0;
		} else if(dto.getIsLike().equals(isLike)) { //같은 버튼 두번 누르면 취소
			boardRepository.deleteVote(boardSeq, boardTypeSeq, memberSeq, isLike);
			code = 1;
		} else { //이전과 다른 버튼 누르면 갱신 (이전: 좋아요 -> 현재 : 싫어요)
			boardRepository.updateVote(boardSeq, boardTypeSeq, memberSeq, isLike, ip);
			code = 2;
		}
		
		return code;
	}

	//투표 결과 add
//	public int addVote(
//			int boardSeq, int boardTypeSeq, int memberSeq, String isLike, String ip) {
//		
//	}
//	
//	//투표 결과 delete
//	public int deleteVote(
//			int boardSeq, int boardTypeSeq, int memberSeq, String isLike) {
//		
//	}
//	
//	//투표 결과 update
//	public int updateVote(
//			int boardSeq, int boardTypeSeq, int memberSeq, String isLike, String ip) {
//		
//	}
	

	/**
	 * 게시글 저장
	 * * 게시글에 대한 insert와 첨부파일의 물리적 저장, insert가 모두 하나의 Tx
	 * 1. 게시글 데이터 DB 저장
	 * 2. 파일을 물리적으로 저장
	 * 3. 파일에 대한 메타 정보를 DB에 저장
	 * @param dto
	 * @param mfs
	 * @return
	 */
	@Transactional
	public int savePost(BoardSaveDto dto, MultipartFile[] mfs) {
		int code = 1;
		try {
			//1. 게시글 데이터 DB에 저장
			int boardSeq = boardRepository.save(dto); //내부적으로 keyholder를 사용해 pk반환
			log.info("boardSeq={}", boardSeq);

			for(MultipartFile mf : mfs) { //첨부파일 배열에 대해 루프 돌림
				if(!mf.isEmpty()) {
					//2. 첨부파일 물리적 저장 및	
					File destfile = fileUtil.saveFiles(mf);
					//3-1. BoardAttachDto 생성
					BoardAttachDto attachDto = BoardAttachDto.makeBoardAttachDto(mf, destfile);
					attachDto.setBoardSeq(boardSeq);
					attachDto.setBoardTypeSeq(dto.getBoardTypeSeq());
					//3-2. 첨부파일 메타데이터 DB에 저장
					log.info("attachDto={}", attachDto);
					boardAttachRepository.saveAttachFile(attachDto);
				}
			}
		} catch(DataAccessException e) {
			//게시글 등록에 실패하면
			log.info("e.getMessage()={}", e.getMessage());
			code = -1;
			e.printStackTrace();
		} catch(FileSaveException e) {
			code = -2; //사용자에게 게시글 등록 실패 이유를 전달하기 위해 굳이 code를 나눴다.
			e.printStackTrace();
		}
		return code;
	}

	
	/**
	 * 게시물 수정
	 * 게시물 작성과 달리 게시물 수정은 이미 boardSeq가 있기 때문에 keyHolder를 쓰지 않아도 된다.
	 * @param modifyDto
	 * @return
	 */
	@Transactional
	public int modify(BoardModifyDto modifyDto, MultipartFile[] attFiles) {
		//1. 게시글 데이터 DB 업데이트
		int code = 0;
		try {
			code = boardRepository.update(modifyDto);
			
			for(MultipartFile mf : attFiles) {
				if(!mf.isEmpty()) {
					//2. 첨부파일 물리적 저장
					File destFile = fileUtil.saveFiles(mf);
					
					//dto
					BoardAttachDto attachDto = BoardAttachDto.makeBoardAttachDto(mf, destFile);
					attachDto.setBoardSeq(modifyDto.getBoardSeq());
					attachDto.setBoardTypeSeq(modifyDto.getBoardTypeSeq());
					
					//3. 첨부파일 데이터 DB 저장
					boardAttachRepository.saveAttachFile(attachDto);
				}
			}
		} catch(DataAccessException e) {
			//게시글 수정에 실패하면
			log.info("e.getMessage()={}", e.getMessage());
			code = -1;
			e.printStackTrace();
		} catch(FileSaveException e) {
			code = -2; //사용자에게 게시글 수정 실패 이유를 전달하기 위해 code를 나눴다.
			e.printStackTrace();
		}
		
		log.info("code={}",code);
		return code;
	}
	
	
	/**
	 * 게시물 삭제
	 * -> 해당 게시물의 게시글과 첨부파일 모두 삭제
	 * -> (물리적으로 저장된 첨부파일도 삭제하고, DB에서 파일 데이터도 모두 삭제)
	 * @param boardSeq
	 * @param boardTypeSeq
	 * @return
	 * 처음엔 try-catch로 처리했었는데, 
	 * 예외가 발생하면 catch구문에 잡혀서 rollback이 안될 것 같아서 rollbackFor로만 설정함.. 
	 * 예외에 대해 처리도 하고 rollback도 하고 싶으면 어떻게 하지?? 
	 */
	
	@Transactional(rollbackFor = {DataAccessException.class, FileSaveException.class})
	public int delete(Integer boardSeq, Integer boardTypeSeq) {
		int code = -1;
	
		//1. 물리적으로 저장되어있는 파일 삭제
		List<File> delFileList = getDeleteFileList(boardSeq, boardTypeSeq);
		fileUtil.deleteFiles(delFileList);
		
		//2. 파일정보 DB에서 삭제
		boardAttachRepository.deleteList(boardSeq, boardTypeSeq);
		//3. 게시글 DB에서 삭제(FK 제약조건이 restrict on delete라서, board_attach의 delete가 먼저 수행되어야 한다.)
		code = boardRepository.delete(boardSeq, boardTypeSeq);

		log.info("code={}", code);
		return code;
	}

	
	
	private List<File> getDeleteFileList(Integer boardSeq, Integer boardTypeSeq) {
		List<BoardAttachDto> attFileList = boardAttachRepository.getList(boardSeq, boardTypeSeq);
		List<File> delFileList = attFileList.stream()
								.map(dto -> new File(dto.getSavePath()))
								.collect(Collectors.toList());
		return delFileList;
	}

	
	/**
	 * 게시물 수정 페이지에서 개별 첨부 파일 삭제 요청
	 * 
	 * Tx의 rollbackFor 디폴트가 runtimeException이지만, 혹시 몰라 명시적으로 적어두었다.
	 * SecurityException은 file.delete()에서 발생할 수 있는 runtimeException인데,
	 * DB 예외는 아니지만, 파일을 물리적으로 삭제하는 과정에서 예외가 생겨도 rollback 처리 하고 싶었음.
	 * @param attachSeq
	 */
	
	@Transactional(rollbackFor = {SecurityException.class, DataAccessException.class})
	public int deleteFile(Integer attachSeq) {
		//1.첨부파일을 물리적으로 지우고
		//2.DB에서도 첨부파일 정보 삭제
		BoardAttachDto dto = boardAttachRepository.getOne(attachSeq);
		File file = new File(dto.getSavePath());
		fileUtil.deleteFile(file);
		return boardAttachRepository.deleteOne(attachSeq);
	}
}
