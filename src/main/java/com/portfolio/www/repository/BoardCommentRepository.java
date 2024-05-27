package com.portfolio.www.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.portfolio.www.dto.BoardCommentDto;
import com.portfolio.www.dto.BoardCommentVoteDto;
import com.portfolio.www.forum.notice.dto.CommentResultDto;

public interface BoardCommentRepository {

	int save(BoardCommentDto commentDto);
	int modify(BoardCommentDto commentDto);
	int delete(int commentSeq);
	List<CommentResultDto> getAllCommentList(@Param("boardSeq") int boardSeq, @Param("boardTypeSeq") int boardTypeSeq);
	int count(@Param("boardSeq") int boardSeq, @Param("boardTypeSeq") int boardTypeSeq);
	BoardCommentVoteDto getVote(@Param("commentSeq") int commentSeq, @Param("memberSeq") int memberSeq);
	int addVote(BoardCommentVoteDto dto);
	int deleteVote(int commentSeq);
	int updateVote(BoardCommentVoteDto dto);
	int getLikeTotal(int commentSeq);
	int getUnlikeTotal(int commentSeq);

}
