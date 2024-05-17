package com.portfolio.www.repository;

import java.util.List;

import com.portfolio.www.dto.BoardCommentDto;
import com.portfolio.www.dto.BoardCommentVoteDto;

public interface BoardCommentRepository {

	int save(BoardCommentDto commentDto);
	int modify(BoardCommentDto commentDto);
	int delete(int commentSeq);
	List<BoardCommentDto> getList(int boardSeq, int boardTypeSeq);
	int count(int boardTypeSeq, int boardSeq);
	BoardCommentVoteDto getVote(int commentSeq);
	int addVote(BoardCommentVoteDto dto);
	int deleteVote(int commentSeq);
	int updateVote(BoardCommentVoteDto dto);

}
