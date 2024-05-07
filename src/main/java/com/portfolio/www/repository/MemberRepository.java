package com.portfolio.www.repository;

import java.util.HashMap;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;

import com.portfolio.www.dto.MemberDto;

public interface MemberRepository {
	int join(HashMap<String, String> params) throws DataAccessException;
	
	int getMemberSeq(String memberId) throws DataAccessException;
	
	int authValidation(int memberSeq) throws DataAccessException;
	
	MemberDto find(String memberId) throws DataRetrievalFailureException;
	
}
