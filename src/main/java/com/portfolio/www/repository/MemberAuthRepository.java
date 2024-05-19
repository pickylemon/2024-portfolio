package com.portfolio.www.repository;

import com.portfolio.www.dto.MemberAuthDto;

public interface MemberAuthRepository {

	public int addAuthInfo(MemberAuthDto dto);
	public MemberAuthDto getMemberAuthDto(String uri);
	public int authValidation(String uri);

}
