package com.portfolio.www.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.portfolio.www.dto.BoardDto;
import com.portfolio.www.repository.BoardDao;
import com.portfolio.www.util.PageHandler;
import com.portfolio.www.util.SearchCondition;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardService {
	private final BoardDao boardDao;
	
	public List<BoardDto> getList(PageHandler ph, SearchCondition sc) {
		return boardDao.getList(ph, sc);
	}

}
