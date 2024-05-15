package com.portfolio.www.service;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.portfolio.www.dto.BoardCommentDto;
import com.portfolio.www.repository.BoardCommentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardCommentService {
	private final BoardCommentRepository boardCommentRepository;

	public int addComment(BoardCommentDto commentDto) {
		int code = -1;
		try {
			code = boardCommentRepository.save(commentDto);
		} catch(DataAccessException e) {
			e.printStackTrace();
		}
	
		return code;
	}
	
	public List<BoardCommentDto> getCommentList(int boardSeq, int boardTypeSeq){
		return boardCommentRepository.getList(boardSeq, boardTypeSeq);
	}

	public int deleteComment(int commentSeq) {
		int code = -1;
		try {
			code = boardCommentRepository.delete(commentSeq);
		} catch(DataAccessException e) {
			e.printStackTrace();
		}
		return code;
	}

}
