package com.portfolio.www.repository;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.portfolio.www.dto.BoardCommentDto;

@Repository
public class BoardCommentDao extends JdbcTemplate implements BoardCommentRepository{
	@Autowired
	public void setDataSource(DataSource dataSource) {
		super.setDataSource(dataSource);
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

	@Override
	public int update(BoardCommentDto commentDto) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(int commentSeq) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<BoardCommentDto> getList(int boardTypeSeq, int boardSeq) {
		// TODO Auto-generated method stub
		return null;
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

}
