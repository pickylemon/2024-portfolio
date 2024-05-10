package com.portfolio.www.repository;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.portfolio.www.dto.BoardDto;
import com.portfolio.www.dto.BoardModifyDto;
import com.portfolio.www.dto.BoardSaveDto;
import com.portfolio.www.dto.BoardVoteDto;
import com.portfolio.www.util.PageHandler;
import com.portfolio.www.util.SearchCondition;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class BoardDao extends JdbcTemplate implements BoardRepository {
	
	@Autowired
	public BoardDao(DataSource dataSource) {
		super(dataSource);
	}
	
	/**
	 * 게시글 리스트 가져오기 + 페이징
	 * @return
	 */
	@Override
	public List<BoardDto> getList(PageHandler ph, SearchCondition sc){
		int offset = ph.getOffset();
		int pageSize = ph.getPageSize();
		
		String sql = "SELECT b.board_seq, b.board_type_seq, b.title, b.content, b.hit, b.del_yn, b.reg_dtm, b.reg_member_seq, b.update_dtm, b.update_member_seq, m.member_id"
				+ " FROM board b "
				+ " JOIN board_type bt "
				+ " ON b.board_type_seq = bt.board_type_seq "
				+ " JOIN member m "
				+ " ON b.reg_member_seq = m.member_seq "
				+ " ORDER BY b.board_seq DESC "
				+ " LIMIT ?, ?";
		
		return query(sql, rowMapper(), offset, pageSize);
		
	}
	
	@Override
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
	
	@Override
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
	
	
	public int addLike(int boardSeq, int boardTypeSeq, int memberSeq, String ip) {
		String sql = "INSERT INTO forum.board_like"
				+ "(board_seq, board_type_seq, member_seq, ip, reg_dtm)"
				+ "VALUES(?, ?, ?, ?, DATE_FORMAT(NOW(), );";
		
		Object[] args = {boardSeq, boardTypeSeq, memberSeq, ip};
		return update(sql, args);
	}
	
	/**
	 * 좋아요, 싫어요 이미 존재하는지
	 */
	
	@Override
	public BoardVoteDto getVote(int boardSeq, int boardTypeSeq, int memberSeq) {
		String sql = "SELECT * FROM board_vote "
				+ " WHERE board_seq = ? "
				+ " AND board_type_seq = ? "
				+ " AND member_seq = ? ";
		
		Object[] args = {boardSeq, boardTypeSeq, memberSeq};
		
		//조회 결과가 없으면 EmptyResultDataAccessException던짐
		return queryForObject(sql, voteRowMapper(), args); 
	}
	
	@Override
	public int addVote(int boardSeq, int boardTypeSeq, int memberSeq, String isLike, String ip) {
		String sql = "INSERT INTO board_vote "
				+ " (board_seq, board_type_seq, member_seq, is_like, reg_dtm, ip) "
				+ " VALUES (?, ?, ?, ?, DATE_FORMAT(now(),'%Y%m%d%H%i%s'), ? ) ";
		
		Object[] args = {boardSeq, boardTypeSeq, memberSeq, isLike, ip };
		return update(sql, args);
	}
	
	@Override
	public int deleteVote(int boardSeq, int boardTypeSeq, int memberSeq, String isLike) {
		String sql = "DELETE FROM board_vote "
				+ " WHERE board_seq = ? "
				+ " AND board_type_seq = ? "
				+ " AND member_seq = ? "
				+ " AND is_like = ? ";
		
		Object[] args = {boardSeq, boardTypeSeq, memberSeq, isLike};
		return update(sql, args);
	}
	
	@Override
	public int updateVote(int boardSeq, int boardTypeSeq, int memberSeq, String isLike, String ip) {
		String sql = "UPDATE board_vote "
				+ " SET is_like = ?, "
				+ " ip = ? "
				+ " WHERE board_seq = ? "
				+ " AND board_type_seq = ? "
				+ " AND member_seq = ? ";
		
		Object[] args = { isLike, ip,  boardSeq, boardTypeSeq, memberSeq };
		
		return update(sql, args);
	}
	
//	public int updateVote(int boardSeq, int boardTypeSeq, int memberSeq, String isLike, String oldIsLike) {
//		String sql = "UPDATE board_vote "
//				+ " SET is_like = ? "
//				+ " WHERE board_seq = ? "
//				+ " AND board_type_seq = ? "
//				+ " AND member_seq = ? "
//				+ " AND is_like = ? ";
//		
//		Object[] args = { isLike, boardSeq, boardTypeSeq, memberSeq, oldIsLike };
//		
//		return update(sql, args);
//	}
	
	
	/**
	 * 게시글 저장(insert)
	 * @param dto
	 * @param memberSeq
	 * @return
	 */
	@Override
	public int save(BoardSaveDto dto, int memberSeq) {
		String sql = "INSERT INTO board "
				+ " (board_type_seq, title, content, reg_member_seq, reg_dtm) "
				+ " VALUES (?, ?, ?, ?, DATE_FORMAT(now(),'%Y%m%d%H%i%s'))";
				
		Object[] args = {dto.getBoardTypeSeq(), dto.getTitle(), dto.getContent(), memberSeq};
		return update(sql, args);
	}
	
	@Override
	public int update(BoardModifyDto dto) {
		String sql = "UPDATE board "
				+ "SET title = ? "
				+ ", content = ? "
				+ ", update_member_seq = ? "
				+ ", update_dtm = DATE_FORMAT(now(),'%Y%m%d%H%i%s') "
				+ " WHERE board_seq = ?";
		
		Object[] args = {dto.getTitle(), dto.getContent(), dto.getUpdateMemberSeq(), dto.getBoardSeq()};
		return update(sql, args);
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
	
	public RowMapper<BoardVoteDto> voteRowMapper() {
		return ((rs, rowNum) -> {
			
			BoardVoteDto dto = new BoardVoteDto();
			dto.setBoardSeq(rs.getInt("board_seq"));
			dto.setBoardTypeSeq(rs.getInt("board_type_seq"));
			dto.setMemberSeq(rs.getInt("member_seq"));
			dto.setIsLike(rs.getString("is_like"));
			dto.setRegDtm(rs.getString("reg_dtm"));
			dto.setIp(rs.getString("ip"));
			
			return dto;
		});
	}

}
