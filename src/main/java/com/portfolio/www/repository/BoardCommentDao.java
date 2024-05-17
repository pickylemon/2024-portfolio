package com.portfolio.www.repository;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.portfolio.www.dto.BoardCommentDto;
import com.portfolio.www.dto.BoardCommentVoteDto;

@Repository
public class BoardCommentDao extends JdbcTemplate implements BoardCommentRepository{
	@Autowired
	public void setDataSource(DataSource dataSource) {
		super.setDataSource(dataSource);
	}

	
	/**
	 * 해당 게시글의 댓글 수를 반환
	 */
	@Override
	public int count(int boardSeq, int boardTypeSeq) {
		String sql = "SELECT COUNT(*) "
				+ " FROM forum.board_comment "
				+ " WHERE board_seq = ?"
				+ " AND board_type_seq = ?";
		
		Object[] args = {boardSeq, boardTypeSeq};
		
		return queryForObject(sql, Integer.class, args);
	}

	/**
	 * 댓글을 저장한다.
	 */
	@Override
	public int save(BoardCommentDto commentDto) {
		String sql = "INSERT INTO forum.board_comment "
				+ " (lvl, content, board_seq, board_type_seq, member_seq, parent_comment_seq, reg_dtm)"
				+ " VALUES(?, ?, ?, ?, ?, ?, DATE_FORMAT(now(),'%Y%m%d%H%i%s'))";
		
		Object[] args = { commentDto.getLvl(), commentDto.getContent(), 
				commentDto.getBoardSeq(), commentDto.getBoardTypeSeq(), 
				commentDto.getMemberSeq(), commentDto.getParentCommentSeq()
			};
		
		return update(sql, args);
	
	}

	/**
	 * 댓글 수정
	 */
	@Override
	public int modify(BoardCommentDto commentDto) {
		String sql = "UPDATE forum.board_comment "
				+ " SET content = ?,"
				+ " update_dtm = DATE_FORMAT(now(),'%Y%m%d%H%i%s') "
				+ " WHERE comment_seq = ?";
		
		Object[] args = { commentDto.getContent(), commentDto.getCommentSeq()};
		
		return update(sql, args);
	}

	/**
	 * 댓글 삭제
	 */
	@Override
	public int delete(int commentSeq) {
		String sql = "DELETE FROM forum.board_comment "
				+ " WHERE comment_seq = ?";

		return update(sql, commentSeq);
	}

	/**
	 * boardSeq와 boardTypeSeq로 식별되는 모든 댓글 가져오기
	 * 계층을 유지하는 방식으로 정렬해서 가져와야 함
	 * 계층형 쿼리 중요! 
	 * & 한 댓글에 대댓글이 여러게 달릴 경우 해당 댓글이 중복으로 출력되는 것을 막기 위해 group by해주기
	 */
	@Override
	public List<BoardCommentDto> getList(int boardSeq, int boardTypeSeq) {
		String sql = "SELECT a.*, m.member_nm "
				+ " FROM "
				+ "	(SELECT bc1.comment_seq, bc1.lvl, bc1.content,"
				+ "		    bc1.board_seq, bc1.board_type_seq, bc1.member_seq, "
				+ "		    IFNULL(bc1.parent_comment_seq, bc2.parent_comment_seq) parent_comment_seq,"
				+ "		    bc1.reg_dtm, bc1.update_dtm, bc1.delete_dtm"
				+ "		  	FROM forum.board_comment bc1"
				+ "			LEFT OUTER JOIN forum.board_comment bc2"
				+ "			ON bc1.comment_seq = bc2.parent_comment_seq"
				+ "			GROUP BY bc1.comment_seq) a"
				+ " JOIN forum.member m"
				+ " ON a.member_seq = m.member_seq"
				+ " WHERE board_seq = ? "
				+ " AND board_type_seq = ? "
				+ " ORDER BY IFNULL(parent_comment_seq, 9999999), reg_dtm, comment_seq";
		Object[] args = {boardSeq, boardTypeSeq};
		
		return query(sql, boardCommentDtoRowMapper(), args);
	}

	/**
	 * (댓글의) 이전 투표 결과를 반환
	 */
	@Override
	public BoardCommentVoteDto getVote(int commentSeq) {
		String sql = "SELECT * FROM forum.comment_vote"
				+ " WHERE comment_seq = ?";
		
		return queryForObject(sql, boardCommentVoteDtoRowMapper(), commentSeq);
	}
	
	/**
	 * 댓글 투표 추가
	 */

	@Override
	public int addVote(BoardCommentVoteDto dto) {
		String sql = "INSERT INTO forum.comment_vote"
				+ " (comment_seq, member_seq, is_like, ip, reg_dtm)"
				+ " VALUES(?, ?, ?, ?, DATE_FORMAT(now(),'%Y%m%d%H%i%s'))";
		
		Object[] args = {dto.getCommentSeq(), dto.getMemberSeq(), dto.getIsLike(), dto.getIp()};
		return update(sql, args);
		
	}

	/**
	 * 댓글 투표 삭제
	 */
	@Override
	public int deleteVote(int commentSeq) {
		String sql = "DELETE FROM forum.comment_vote "
				+ " WHERE comment_seq = ?";
		
		return update(sql, commentSeq);
	}

	/**
	 * 댓글 투표 업데이트(좋아요, 싫어요 변경)
	 */
	@Override
	public int updateVote(BoardCommentVoteDto dto) {
		String sql = "UPDATE forum.comment_vote "
				+ " SET is_like = ?"
				+ " , reg_dtm = DATE_FORMAT(now(),'%Y%m%d%H%i%s')"
				+ " , ip = ?"
				+ " WHERE comment_seq = ?";
		
		Object[] args = {dto.getIsLike(), dto.getIp(), dto.getCommentSeq() };
		
		return update(sql, args);
		
	}

	
	private RowMapper<BoardCommentVoteDto> boardCommentVoteDtoRowMapper() {
		return (rs, rowNum) -> {
			BoardCommentVoteDto voteDto = new BoardCommentVoteDto();
			voteDto.setCommentSeq(rs.getInt("comment_seq"));
			voteDto.setMemberSeq(rs.getInt("member_seq"));
			voteDto.setRegDtm(rs.getString("reg_dtm"));
			voteDto.setIsLike(rs.getString("is_like"));
			voteDto.setIp(rs.getString("ip"));
			
			return voteDto;
		};
	}

	
	private RowMapper<BoardCommentDto> boardCommentDtoRowMapper(){
		return (rs, rowNum) -> {
			BoardCommentDto dto = new BoardCommentDto();
			dto.setBoardSeq(rs.getInt("board_seq"));
			dto.setBoardTypeSeq(rs.getInt("board_type_seq"));
			dto.setCommentSeq(rs.getInt("comment_seq"));
			dto.setContent(rs.getString("content"));
			dto.setDeleteDtm(rs.getString("delete_dtm"));
			dto.setLvl(rs.getInt("lvl"));
			dto.setMemberSeq(rs.getInt("member_seq"));
			dto.setParentCommentSeq(rs.getInt("parent_comment_seq"));
			dto.setRegDtm(rs.getString("reg_dtm"));
			dto.setUpdateDtm(rs.getString("update_dtm"));
			dto.setMemberNm(rs.getString("member_nm"));
			
			return dto;
		};
	}
}
