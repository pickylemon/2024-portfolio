package com.portfolio.www.repository;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
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

	@Override
	public int delete() {
		return 0;
	}
	
	

}
