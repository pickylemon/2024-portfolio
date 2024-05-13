package com.portfolio.www.repository;


import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.portfolio.www.dto.BoardAttachDto;

@Repository
public class BoardAttachDao extends JdbcTemplate implements BoardAttachRepository {
	@Autowired
	public BoardAttachDao(DataSource dataSource) {
		super(dataSource);
	}

	@Override
	public int saveAttachFile(BoardAttachDto dto) {
		String sql = "INSERT INTO forum.board_attach "
				+ " (board_seq, board_type_seq, org_file_nm, save_path, chng_file_nm, file_size, file_type, reg_dtm)"
				+ " VALUES( ? , ? , ? , ? , ? , ? , ?, DATE_FORMAT(now(),'%Y%m%d%H%i%s') );";
		
		Object[] args = {dto.getBoardSeq(), dto.getBoardTypeSeq(), 
						dto.getOrgFileNm(), dto.getSavePath(), 
						dto.getChngFileNm(), dto.getFileSize(), dto.getFileType() 
						};
		
		return update(sql, args);
	}
	
	/**
	 * 해당 게시물의 첨부파일 갯수를 세기
	 */
	
	@Override
	public int count(int boardSeq, int boardTypeSeq) {
		String sql = "SELECT count(*) FROM forum.board_attach "
				+ " WHERE board_seq = ? "
				+ " AND board_type_seq = ?";
		
		Object[] args = {boardSeq, boardTypeSeq};
		return queryForObject(sql, Integer.class, args);
	}
	
	@Override
	public int deleteList(int boardSeq, int boardTypeSeq) {
		String sql = "DELETE FROM forum.board_attach "
				+ " WHERE board_seq=? AND board_type_seq=?";
		Object[] args = {boardSeq, boardTypeSeq};
		
		return update(sql, args);
	}
	
	@Override
	public int deleteOne(int attachSeq) {
		// TODO Auto-generated method stub
		return 0;
	}



	/**
	 * 첨부파일info 리스트 가져오기
	 */
	@Override
	public List<BoardAttachDto> getList(int boardSeq, int boardTypeSeq) {
		String sql = "SELECT attach_seq, board_seq, board_type_seq, org_file_nm, "
				+ " save_path, chng_file_nm, file_size, file_type, access_uri, reg_dtm "
				+ " FROM forum.board_attach "
				+ " WHERE board_seq = ?"
				+ " AND board_type_seq = ?";
		
		Object[] args = {boardSeq, boardTypeSeq};
		
		return query(sql, rowMapper(), args);
	}
	
	
	/**
	 * 첨부파일 info 하나 가져오기
	 */
	@Override
	public BoardAttachDto getOne(Integer attachSeq) {
		String sql = "SELECT attach_seq, board_seq, board_type_seq, org_file_nm, "
				+ " save_path, chng_file_nm, file_size, file_type, access_uri, reg_dtm "
				+ " FROM forum.board_attach "
				+ " WHERE attach_seq = ?";
		
		return queryForObject(sql, rowMapper(), attachSeq);
	}
	
	
	public RowMapper<BoardAttachDto> rowMapper(){
		return ((rs, rowNum) -> {
			BoardAttachDto dto = new BoardAttachDto();
			dto.setBoardSeq(rs.getInt("board_seq"));
			dto.setBoardTypeSeq(rs.getInt("board_type_seq"));
			dto.setAccessUri(rs.getString("access_uri"));
			dto.setAttachSeq(rs.getInt("attach_seq"));
			dto.setChngFileNm(rs.getString("chng_file_nm"));
			dto.setFileSize(rs.getInt("file_size"));
			dto.setRegDtm(rs.getString("reg_dtm"));
			dto.setSavePath(rs.getString("save_path"));
			dto.setFileType(rs.getString("file_type"));
			dto.setOrgFileNm(rs.getString("org_file_nm"));
			
			return dto;
		});
	}




}
