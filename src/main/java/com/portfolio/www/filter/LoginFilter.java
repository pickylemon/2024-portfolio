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
	
	private String[] urlMapping = { LoginAuthEnum.BOARD.getUrl(), 
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
		HttpSession session = request.getSession();
		
		log.info("contextPath={}", contextPath);
		log.info("requestURL = {}", requestURL);
		log.info("\n requestURI = {} \n", requestURI);
		
		//로그인 인증이 필요한 페이지에 한해 로그인 검증
		List<String> urlList = List.of(urlMapping);
		if(urlList.stream().anyMatch(url -> requestURI.startsWith(url))) {
			//로그인이 되어 있지 않으면 로그인 페이지로 redirect
			if(ObjectUtils.isEmpty(session.getAttribute("memberId"))) {
				response.sendRedirect(contextPath+"/loginPage.do?url="+requestURL);
			}
		}
		
		chain.doFilter(request, response);
		
	}

}
