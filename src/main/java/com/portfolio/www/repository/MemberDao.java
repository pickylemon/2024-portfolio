package com.portfolio.www.repository;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.portfolio.www.exception.DuplicateMemberException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MemberDao extends JdbcTemplate{

	public MemberDao(DataSource dataSource) {
		super(dataSource);
	}
	
	public int join(HashMap<String, String> params) {
		String sql = "INSERT INTO forum.`member`"
				+ " (member_id, passwd, member_nm, email, auth_yn, pwd_chng_dtm, join_dtm)"
				+ " VALUES('"+params.get("memberId")
				+"', '"+params.get("passwd")
				+"', '', '" + params.get("email")
				+ "', '', '', DATE_FORMAT(NOW()  ,'%Y%m%d%H%i%s'))";
		
		try {
			return update(sql);
		} catch(DuplicateKeyException e) {
			log.info("e.getClass = {}", e.getClass());
			DuplicateMemberException ex = new DuplicateMemberException(e);
			throw ex;
		}
	}
	
	public int getMemberSeq(String memberId) {
		String sql = "SELECT member_seq FROM member where member_id = '" + memberId +"'";
		return queryForObject(sql, Integer.class);
	}
	

}
