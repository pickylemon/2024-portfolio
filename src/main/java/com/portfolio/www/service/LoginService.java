package com.portfolio.www.service;

import java.util.HashMap;

import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.portfolio.www.dto.MemberDto;
import com.portfolio.www.exception.NoSuchMemberException;
import com.portfolio.www.repository.LoginDao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {
	private final LoginDao loginDao;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public boolean login(HashMap<String, String> params) {
		//사용자 입력 아이디로 회원 조회
		String memberId = params.get("memberId");
		String passwd = params.get("passwd");
		try {
			MemberDto memberDto = loginDao.find(memberId);
			log.info("\n memberDto={}\n", memberDto);
			return bCryptPasswordEncoder.matches(passwd, memberDto.getPasswd());
		} catch (DataRetrievalFailureException e) {
			//memberId로 검색되는 회원이 없는 경우
			NoSuchMemberException ex = new NoSuchMemberException("해당 아이디의 회원이 없습니다.");
			ex.initCause(e);
			throw ex;
		}
	}

}
