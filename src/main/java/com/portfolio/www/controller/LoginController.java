package com.portfolio.www.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LoginController {
	private final JoinService joinService;
	
	@RequestMapping("/loginPage.do")
	public String loginPage() {
		return "login";
	}
	
	@RequestMapping("/join.do")
	public ModelAndView join(@RequestParam)
}
