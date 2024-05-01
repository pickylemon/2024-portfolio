package com.portfolio.www.repository;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.portfolio.www.dto.BoardDto;
import com.portfolio.www.util.PageHandler;
import com.portfolio.www.util.SearchCondition;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class BoardDao extends JdbcTemplate {
	
	@Autowired
	public BoardDao(DataSource dataSource) {
		super(dataSource);
	}
	
	public List<BoardDto> getList(PageHandler ph, SearchCondition sc){
		int offset = ph.getOffset();
		int pageSize = ph.getPageSize();
		
		String sql = "SELECT b.board_seq, b.board_type_seq, b.title, b.content, b.hit, b.del_yn, b.reg_dtm, b.reg_member_seq, b.update_dtm, b.update_member_seq, m.member_id"
				+ " FROM board b "
				+ " JOIN board_type bt "
				+ " ON b.board_type_seq = bt.board_type_seq "
				+ " JOIN member m "
				+ " ON b.reg_member_seq = m.member_seq "
				+ " LIMIT ?, ?";
		
		return query(sql, rowMapper(), offset, pageSize);
		
	}
	
	public int getTotalCnt(SearchCondition sc) {
		String sql = "SELECT count(*) "
				+ " FROM board b "
				+ " JOIN board_type bt "
				+ " ON b.board_type_seq = bt.board_type_seq "
				+ " JOIN member m "
				+ " ON b.reg_member_seq = m.member_seq ";
		//나중에 SearchCondition으로 검색용 동적쿼리 작성해야함
		
		return queryForObject(sql, Integer.class);
		
	}
	
	public BoardDto getOne(Integer boardSeq) {
		String sql = "SELECT b.board_seq, b.board_type_seq, b.title, b.content, b.hit, b.del_yn, b.reg_dtm, b.reg_member_seq, b.update_dtm, b.update_member_seq, m.member_id"
				+ " FROM board b "
				+ " JOIN board_type bt "
				+ " ON b.board_type_seq = bt.board_type_seq "
				+ " JOIN member m "
				+ " ON b.reg_member_seq = m.member_seq "
				+ " WHERE b.board_seq = ?";
		
		return queryForObject(sql, rowMapper(), boardSeq);
	}
	
	public RowMapper<BoardDto> rowMapper(){
		return ((rs, rowNum) -> {
			BoardDto dto = new BoardDto();
			dto.setBoardSeq(rs.getInt("board_seq"));
			dto.setBoardTypeSeq(rs.getInt("board_type_seq"));
			dto.setTitle(rs.getString("title"));
			dto.setContent(rs.getString("content"));
			dto.setHit(rs.getInt("hit"));
			dto.setDelYn(rs.getString("del_yn"));
			dto.setRegDtm(rs.getString("reg_dtm"));
			dto.setRegMemberId(rs.getString("member_id"));
			dto.setRegMemberSeq(rs.getInt("reg_member_seq"));
			dto.setUpdateDtm(rs.getString("update_dtm"));
			dto.setUpdateMemberSeq(rs.getInt("update_member_seq"));
			
			return dto;
		});
	}

}
