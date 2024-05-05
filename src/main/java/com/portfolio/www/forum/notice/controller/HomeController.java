package com.portfolio.www.forum.notice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.portfolio.www.service.LoginService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {
	private final LoginService loginService;
	
	@GetMapping("home.do")
	public String home() {
		return "home";
	}

}
