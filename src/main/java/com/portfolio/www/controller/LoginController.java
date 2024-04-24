package com.portfolio.www.controller;

import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.portfolio.www.exception.DuplicateMemberException;
import com.portfolio.www.exception.TimeoutException;
import com.portfolio.www.service.JoinService;

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
	public ModelAndView join(@RequestParam HashMap<String, String> params) {
		log.info("\n\n params = {}", params);
		
		String msg = "";
		int result = 0;
		ModelAndView mv = new ModelAndView();
		try {
			result = joinService.join(params);
		} catch(DuplicateMemberException e) {
			result = -1;
			msg = "중복된 회원 아이디입니다.";
		}
		
		if(result == 1) {
			msg = "회원 가입에 성공했습니다";
//			msg = "Success";
		} else if (result != -1) {
			msg = "회원 가입에 실패했습니다.";
		}
		log.info("result = {}, msg = {}", result, msg);
		mv.addObject("result", result);
		mv.addObject("msg",msg);
		mv.setViewName("login");
		return mv;
	}
	
	@RequestMapping("/emailAuth.do")
	public ModelAndView emailAuth(@RequestParam("uri") String uri) {
		ModelAndView mv = new ModelAndView();
		try {
			joinService.emailAuth(uri);
		} catch (TimeoutException e) {
			String msg = "인증메일의 유효기간이 만료되었습니다. 회원가입을 다시 시도해주세요.";
			//시간의 유효성 검사는, 인증 Y로 set하기 전에 이루어져야 하는것 아닐까?
			mv.addObject("msg",msg);
		}
		mv.setViewName("login");
		return mv;
		
		
		
		
	}
}
