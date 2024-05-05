package com.portfolio.www.controller.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class RestNoticeController {
	
	@GetMapping("/thumb-up.do")
	public void thumbUp(
			@RequestParam int boardSeq,
			@RequestParam int boardTypeSeq,
			HttpServletRequest request) {
		HttpSession session = request.getSession();
		try {
			int memberSeq = (int)session.getAttribute("memberSeq");
		} catch(NullPointerException nep) {
			//session에 memberSeq가 없을 경우
			log.info("\n 사용자 없음 \n");
		}
	
	}

}
