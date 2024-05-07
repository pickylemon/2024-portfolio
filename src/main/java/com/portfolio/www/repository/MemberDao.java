package com.portfolio.www.repository;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.portfolio.www.dto.MemberDto;
import com.portfolio.www.exception.DuplicateMemberException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MemberDao extends JdbcTemplate implements MemberRepository{

	public MemberDao(DataSource dataSource) {
		super(dataSource);
	}
	
	@Override
	public int join(HashMap<String, String> params) throws DataAccessException {
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
	
	@Override
	public int getMemberSeq(String memberId) throws DataAccessException {
		String sql = "SELECT member_seq FROM member where member_id = '" + memberId +"'";
		return queryForObject(sql, Integer.class);
	}
	
	@Override
	public int authValidation(int memberSeq) throws DataAccessException {
		String sql = "UPDATE forum.`member` "
				+ " SET auth_yn = 'Y' "
				+ " WHERE member_seq = '" + memberSeq + "'";
		
		return update(sql);
	}
	
	
	@Override
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
