package com.portfolio.www.repository;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.portfolio.www.dto.MemberAuthDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
//@Repository
public class MemberAuthDao extends JdbcTemplate implements MemberAuthRepository{

	public MemberAuthDao(DataSource dataSource) {
		super(dataSource);
	}
	
	public int addAuthInfo(MemberAuthDto dto) throws DataAccessException {
		String sql = " INSERT INTO forum.member_auth "
	            + " (member_seq, auth_num, auth_uri, reg_dtm, expire_dtm, auth_yn) "
	            + " VALUES(" + dto.getMemberSeq()
	            + " , '', '" + dto.getAuthUri()
	            + "', " + dto.getRegDtm()
	            + ", " + dto.getExpireDtm()
	            + ", 'N')";
		
		return update(sql);
	}
	
	public MemberAuthDto getMemberAuthDto(String uri) throws DataAccessException {
		String sql = "SELECT auth_seq, member_seq, auth_num, auth_uri, reg_dtm, expire_dtm, auth_yn"
				+ " FROM forum.member_auth "
				+ " WHERE auth_uri = '" + uri +"' AND auth_yn = 'N'";

		
		return query(sql, resultSetExtractor());
	}


	public int authValidation(String uri) throws DataAccessException {
		String sql = "UPDATE forum.member_auth"
				+ " SET auth_yn='Y'"
				+ " WHERE auth_uri='" + uri+ "'";
		
		return update(sql);
	}
	
//	private RowMapper<MemberAuthDto> rowMapper() {
//		return ((rs, rowNum) -> {
//			MemberAuthDto dto = new MemberAuthDto();
//			dto.setAuthSeq(rs.getInt("auth_seq"));
//			dto.setMemberSeq(rs.getInt("member_seq"));
//			dto.setAuthNum(rs.getString("auth_num"));
//			dto.setAuthUri(rs.getString("auth_uri"));
//			dto.setAuthYn(rs.getString("auth_yn"));
//			dto.setRegDtm(rs.getLong("reg_dtm"));
//			dto.setExpireDtm(rs.getLong("expire_dtm"));
//			
//			return dto;
//		});
//	}
//	
	private ResultSetExtractor<MemberAuthDto> resultSetExtractor(){
		return (rs -> {
			MemberAuthDto dto = null;
			while(rs.next()) {
				dto = new MemberAuthDto();
				dto.setAuthSeq(rs.getInt("auth_seq"));
				dto.setMemberSeq(rs.getInt("member_seq"));
				dto.setAuthNum(rs.getString("auth_num"));
				dto.setAuthUri(rs.getString("auth_uri"));
				dto.setAuthYn(rs.getString("auth_yn"));
				dto.setRegDtm(rs.getLong("reg_dtm"));
				dto.setExpireDtm(rs.getLong("expire_dtm"));
			}
			return dto;
		});
	}
	

}
