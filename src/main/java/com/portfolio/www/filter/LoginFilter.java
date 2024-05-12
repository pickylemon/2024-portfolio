package com.portfolio.www.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.util.ObjectUtils;

import com.portfolio.www.util.LoginAuthEnum;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginFilter implements Filter {
	
	private String[] urlMapping = { 
			LoginAuthEnum.BOARD.getUrl(), 
			LoginAuthEnum.DEVELOP.getUrl(), 
			LoginAuthEnum.SALES.getUrl(),
			LoginAuthEnum.QA.getUrl(),
			LoginAuthEnum.SUPPORT.getUrl()};

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		String requestURL = request.getRequestURL().toString();
		String contextPath = request.getContextPath();
		String requestURI = request.getRequestURI().replace(contextPath,"");
		HttpSession session = request.getSession(false);
		
//		log.info("referer={}", request.getHeader("referer"));
//		log.info("requestURI={}", request.getRequestURI());		
//		log.info("contextPath={}", contextPath);
//		log.info("requestURL = {}", requestURL);
//		log.info("requestURI = {}", requestURI);
		
		//로그인 인증이 필요한 페이지에 한해 로그인 검증
		List<String> urlList = List.of(urlMapping);
		if(urlList.stream().anyMatch(url -> requestURI.startsWith(url))) {
			log.info("로그인 검증 대상 페이지");
			//로그인이 되어 있지 않으면 로그인 페이지로 redirect
			if(session == null || ObjectUtils.isEmpty(session.getAttribute("memberSeq"))) {
				log.info("로그인 페이지로 이동");
				response.sendRedirect(request.getContextPath()+"/loginPage.do?url="+requestURL);
				return;
			} else {
//				log.info("session.new?={}", session.isNew());
//				log.info("session.getAttribute={}", (int)session.getAttribute("memberSeq"));
			}
		}
		chain.doFilter(request, response);
	}
}
