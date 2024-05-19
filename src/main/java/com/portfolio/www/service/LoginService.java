package com.portfolio.www.service;

import java.util.HashMap;

import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.portfolio.www.dto.MemberDto;
import com.portfolio.www.exception.NoSuchMemberException;
import com.portfolio.www.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {
	private final MemberRepository memberRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public int login(HashMap<String, String> params) {
		//사용자 입력 아이디로 회원 조회
		String memberId = params.get("memberId");
		String passwd = params.get("passwd");
		try {
			MemberDto memberDto = memberRepository.find(memberId);
			log.info("\n memberDto={}\n", memberDto);
			if(bCryptPasswordEncoder.matches(passwd, memberDto.getPasswd())){
				return memberDto.getMemberSeq(); //비밀번호가 일치하면 memberSeq를 반환
			}
			return -1; //비밀번호가 틀릴 경우 -1을 반환
		} catch (DataRetrievalFailureException e) {
			//memberId로 검색되는 회원이 없는 경우
			NoSuchMemberException ex = new NoSuchMemberException("해당 아이디의 회원이 없습니다.");
			ex.initCause(e);
			throw ex;
		}
	}

}
