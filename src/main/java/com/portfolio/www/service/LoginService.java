package com.portfolio.www.service;

import org.springframework.stereotype.Service;

import com.portfolio.www.repository.LoginDao;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginService {
	private final LoginDao loginDao;

}
