package com.portfolio.www.repository;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.portfolio.www.dto.BoardAttachDto;

public interface BoardAttachRepository {
	
	int saveAttachFile(BoardAttachDto dto);

	List<BoardAttachDto> getList(@Param("boardSeq") int boardSeq, @Param("boardTypeSeq") int boardTypeSeq);
	int deleteList(@Param("boardSeq") int boardSeq, @Param("boardTypeSeq") int boardTypeSeq);
	int deleteOne(int attachSeq);
	BoardAttachDto getOne(Integer attachSeq);
	int count(@Param("boardSeq") int boardSeq, @Param("boardTypeSeq") int boardTypeSeq);
	int updateDownloadCnt(int attachSeq);

}
