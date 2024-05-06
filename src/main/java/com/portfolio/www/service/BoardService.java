package com.portfolio.www.service;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.portfolio.www.dto.BoardDto;
import com.portfolio.www.dto.BoardVoteDto;
import com.portfolio.www.repository.BoardDao;
import com.portfolio.www.util.PageHandler;
import com.portfolio.www.util.SearchCondition;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {
	private final BoardDao boardDao;
	
	/*
	 * 게시글 리스트 가져오기
	 */
	public List<BoardDto> getList(PageHandler ph, SearchCondition sc) {
		List<BoardDto> list = boardDao.getList(ph, sc);
		ph.calculatePage(boardDao.getTotalCnt(sc));
		return list;
	}
	
	public int getTotalCnt(SearchCondition sc) {
		return boardDao.getTotalCnt(sc);
	}
	
	public BoardDto getPost(Integer boardSeq) {
		return boardDao.getOne(boardSeq);
	}
	
	/**
	 * 
	 * 좋아요, 싫어요 반영하기
	 */
	
	//이미 이전 투표 결과가 있는지
	public BoardVoteDto getVote(int boardSeq, int boardTypeSeq, int memberSeq) {
		BoardVoteDto dto = null;
		try {
			dto = boardDao.getVote(boardSeq, boardTypeSeq, memberSeq);
		} catch (EmptyResultDataAccessException e) { 
			log.info(e.getMessage()); //투표 결과가 없을 때는 null을 반환
		} 
		return dto;
	}
	
	//투표 결과 add
	public int addVote(
			int boardSeq, int boardTypeSeq, int memberSeq, String isLike, String ip) {
		return boardDao.addVote(boardSeq, boardTypeSeq, memberSeq, isLike, ip);
	}
	
	//투표 결과 delete
	public int deleteVote(
			int boardSeq, int boardTypeSeq, int memberSeq, String isLike) {
		return boardDao.deleteVote(boardSeq, boardTypeSeq, memberSeq, isLike);
	}
	
	//투표 결과 update
	public int updateVote(
			int boardSeq, int boardTypeSeq, int memberSeq, String isLike, String ip) {
		return boardDao.updateVote(boardSeq, boardTypeSeq, memberSeq, isLike, ip);
	}
}
