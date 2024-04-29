package com.portfolio.www.repository;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.portfolio.www.dto.MemberDto;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class LoginDao extends JdbcTemplate {
	
	@Autowired
	LoginDao(DataSource dataSource) { //jdbcTemplate을 생성할 때 dataSource가 필요하다.
		super(dataSource);
	}
	
	public MemberDto find(String memberId) throws DataRetrievalFailureException {
		String sql = "SELECT * FROM forum.`member` WHERE member_id = ?"; //pstmt로 작성
	
		log.info("\n memberId = {}\n", memberId);
		return queryForObject(sql, memberDtoRowMapper(), memberId);
		//조회대상이 없거나(empty) 단건이 아니면 DataAccessException 발생함. (RuntimeException)
		//사실상 unique키로 조회하는 거라 조회대상이 없는 경우의 예외만 발생할 것.
	}
	
	private RowMapper<MemberDto> memberDtoRowMapper() {
		return ((rs, rowNum) -> {
			MemberDto dto = new MemberDto();
			dto.setMemberSeq(rs.getInt("member_seq"));
			dto.setMemberId(rs.getString("member_id"));
			dto.setMemberNm(rs.getString("member_nm"));
			dto.setPasswd(rs.getString("passwd"));
			dto.setAuthYn(rs.getString("auth_yn"));
			dto.setEmail(rs.getString("email"));
			dto.setJoinDtm(rs.getString("join_dtm"));
			dto.setPwdChngDtm(rs.getString("pwd_chng_dtm"));
		
			return dto;
		}); 
	}
	
	 

}
