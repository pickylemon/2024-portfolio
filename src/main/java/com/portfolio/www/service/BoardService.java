package com.portfolio.www.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.portfolio.www.dto.BoardDto;
import com.portfolio.www.repository.BoardDao;
import com.portfolio.www.util.PageHandler;
import com.portfolio.www.util.SearchCondition;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {
	private final BoardDao boardDao;
	
	@Transactional
	public List<BoardDto> getList(PageHandler ph, SearchCondition sc) {
		List<BoardDto> list = boardDao.getList(ph, sc);
		ph.calculatePage(boardDao.getTotalCnt(sc));
		return list;
	}
	
	public int getTotalCnt(SearchCondition sc) {
		return boardDao.getTotalCnt(sc);
	}

}
