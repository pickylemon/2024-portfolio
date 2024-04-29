package com.portfolio.www.repository;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class LoginDao extends JdbcTemplate {
	
	@Autowired
	LoginDao(DataSource dataSource) { //jdbcTemplate을 생성할 때 dataSource가 필요하다.
		super(dataSource);
	}

}
