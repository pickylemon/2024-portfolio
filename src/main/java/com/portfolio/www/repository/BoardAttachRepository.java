package com.portfolio.www.repository;

import com.portfolio.www.dto.BoardAttachDto;

public interface BoardAttachRepository {
	
	int saveAttachFile(BoardAttachDto dto);
	int delete();

}
