package com.portfolio.www.repository;

import java.util.List;

import com.portfolio.www.dto.BoardDto;
import com.portfolio.www.dto.BoardModifyDto;
import com.portfolio.www.dto.BoardSaveDto;
import com.portfolio.www.dto.BoardVoteDto;
import com.portfolio.www.util.PageHandler;
import com.portfolio.www.util.SearchCondition;

public interface BoardRepository {
	List<BoardDto> getList(PageHandler ph, SearchCondition sc);
	
	int getTotalCnt(SearchCondition sc);
	
	BoardDto getOne(Integer boardSeq);
	
	BoardVoteDto getVote(int boardSeq, int boardTypeSeq, int memberSeq);
	
	int addVote(int boardSeq, int boardTypeSeq, int memberSeq, String isLike, String ip);
	
	
	int deleteVote(int boardSeq, int boardTypeSeq, int memberSeq, String isLike);
	
	int updateVote(int boardSeq, int boardTypeSeq, int memberSeq, String isLike, String ip);
	
	int save(BoardSaveDto dto, int memberSeq);
	
	int update(BoardModifyDto dto);
	
	int delete(Integer boardSeq, Integer boardTypeSeq);

}
