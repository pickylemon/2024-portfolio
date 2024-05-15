package com.portfolio.www.repository;

import java.util.List;

import com.portfolio.www.dto.BoardCommentDto;

public interface BoardCommentRepository {

	int save(BoardCommentDto commentDto);
	int update(BoardCommentDto commentDto);
	int delete(int commentSeq);
	List<BoardCommentDto> getList(int boardTypeSeq, int boardSeq);
	int count(int boardTypeSeq, int boardSeq);
}
