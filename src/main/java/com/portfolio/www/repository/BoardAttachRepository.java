package com.portfolio.www.repository;


import java.util.List;

import com.portfolio.www.dto.BoardAttachDto;

public interface BoardAttachRepository {
	
	int saveAttachFile(BoardAttachDto dto);

	List<BoardAttachDto> getList(int boardSeq, int boardTypeSeq);
	int deleteList(int boardSeq, int boardTypeSeq);
	int deleteOne(int attachSeq);
	BoardAttachDto getOne(Integer attachSeq);
	int count(int boardSeq, int boardTypeSeq);

}
