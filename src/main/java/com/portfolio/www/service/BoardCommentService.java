package com.portfolio.www.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.portfolio.www.dto.BoardCommentDto;
import com.portfolio.www.repository.BoardCommentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardCommentService {
	private final BoardCommentRepository boardCommentRepository;

	public int addComment(BoardCommentDto commentDto) {
		return boardCommentRepository.save(commentDto);
	}
	
	public List<BoardCommentDto> getCommentList(int boardSeq, int boardTypeSeq){
		return boardCommentRepository.getList(boardSeq, boardTypeSeq);
	}

}
