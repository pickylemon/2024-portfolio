package com.portfolio.www.forum.notice.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.portfolio.www.exception.DuplicateMemberException;
import com.portfolio.www.exception.InvalidAuthUriException;
import com.portfolio.www.exception.NoSuchMemberException;
import com.portfolio.www.exception.TimeoutException;
import com.portfolio.www.service.JoinService;
import com.portfolio.www.service.LoginService;
import com.portfolio.www.util.MessageEnum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LoginController {
	private final JoinService joinService;
	private final LoginService loginService;
	
	@RequestMapping("/loginPage.do") 
	//로그인 페이지
	public String loginPage(HttpServletRequest request, Model model) {
		log.info("\n model={}\n", model);
		String requestURL = request.getParameter("url");
		model.addAttribute("requestURL", requestURL);

		return "login";
	}
	
	@PostMapping("/login.do")
	public String login(@RequestParam HashMap<String, String> params, HttpServletRequest request, Model model) {
		log.info("\n params={} \n", params);
//		String goalUrl = request.getHeader("referer"); //이전 요청페이지에 대한 정보
//		log.info("referer = {} ", goalUrl);
		
		String requestURL = params.get("requestURL");
		log.info("\n requestURL in params = {}\n", requestURL);
//		log.info("\n requestURL from request = {}\n", request.getRequestURL());
		
		//회원 인증
		//service에서 회원 조회 및 비밀번호 대조까지 진행
		int memberSeq = 0;
		try {
			memberSeq = loginService.login(params);
			if(memberSeq == -1) { //비밀번호가 틀렸으면
				//비밀번호가 틀리면
				model.addAttribute("code", MessageEnum.WRONG_PASSWORD.getCode());
				model.addAttribute("msg", MessageEnum.WRONG_PASSWORD.getMessage());
				model.addAllAttributes(params);
				
				return "login"; //입력 정보와 메시지를 가지고 다시 입력창으로 이동.
			}
		} catch (NoSuchMemberException e) {
			// 존재하지 않는 아이디면 예외발생 
			model.addAttribute("code", MessageEnum.USER_NOT_FOUND.getCode());
			model.addAttribute("msg", MessageEnum.USER_NOT_FOUND.getMessage());
			model.addAllAttributes(params);
			
			return "login";
		}
		
		//로그인 성공하면, 세션에 아이디 저장 후
		HttpSession session = request.getSession();
//		session.setAttribute("memberId", params.get("memberId"));
		session.setAttribute("memberSeq", memberSeq); //로그인 일치시 받아온 MemberSeq를 세션에 저장
		// 원래 요청 주소로 이동.
		return "redirect:"+ (ObjectUtils.isEmpty(requestURL)? "/home.do" : requestURL);
		//로그인 페이지로
		
	}
	
	@GetMapping("/logout.do")
	public String logout(HttpSession session, RedirectAttributes rattr) {
		String memberSeq = (String)session.getAttribute("memberSeq");
		if(!ObjectUtils.isEmpty(memberSeq)) {
			session.invalidate(); //세션 invalidate 후
			rattr.addFlashAttribute("msg", "정상적으로 로그아웃 되었습니다.");
		}//나중에 view에서 로그인이 된 경우에만 로그아웃 버튼이 보이도록 만들어야 함.
		return "redirect:/home.do"; //홈으로 redirect
	}
	
	@PostMapping("/join.do")
	public ModelAndView join(@RequestParam HashMap<String, String> params, HttpServletRequest request, Model model, RedirectAttributes rattr) {
//	public ModelAndView join(MemberJoinDto member, HttpServletRequest request, RedirectAttributes rattr) {
		log.info("\n\n params = {}", params);
		
		ModelAndView mv = new ModelAndView();
		String msg = "";
		int result = 0;
		
		for(String value : params.values()) { 
			if(value == null || "".equals(value.trim())) {
				msg = "회원 가입 양식을 모두 채워주세요.";
				mv.addAllObjects(params);
				mv.addObject("msg", msg);
				mv.setViewName("login");
				return mv; //params이 유효하지 않을 경우. 사용자 입력 값을 가지고 다시 입력창으로 이동.
			}
		}
		
		
//		RedirectView rv = new RedirectView(request.getContextPath()+"/loginPage.do");
//		//상대경로는 안먹는듯(404). contextPath를 붙여줘야함
//		rv.setExposeModelAttributes(false);	
//		ModelAndView mv = new ModelAndView(rv);
		
		
		try {
			result = joinService.join(params);
		} catch(DuplicateMemberException e) {
			result = -1;
			msg = "중복된 회원 아이디입니다.";
		}
		
		if(result == 1) {
			msg = "회원 가입에 성공했습니다. 메일 인증을 완료해주세요.";
		} else if (result != -1) {
			msg = "회원 가입에 실패했습니다.";
		}
		log.info("result = {}, msg = {}", result, msg);
		//mv.addObject("result", result);
		rattr.addAttribute("result",result);
		rattr.addFlashAttribute("msg", msg);
		mv.setViewName("redirect:/loginPage.do");
		
		
		return mv;
		
		//1. post로 온 요청에 대해서는 get 요청으로 흐름을 바꿔줘야한다. (RRG패턴)
		//2. 기존, jsp에서 get요청으로 보내던 것을, 그냥 컨트롤러에서 model을 전달하며 리다이렉트 시키는 방향으로 바꾸고 싶음
		//3. viewName에 redirect 경로를 담았더니, model에 담은 값이 URL에 쿼리파람으로 붙는다.(당연. get이니까)
		//4. RedirectView라는게 있다. redirect:/ 키워드와 차이점은 무엇일까? 정리하기.
		
	}
	
	@RequestMapping("/emailAuth.do")
	public ModelAndView emailAuth(@RequestParam("uri") String uri) throws IllegalAccessException {
//		if(uri==null || "".equals(uri.trim())) {
//			log.info("\n\n null체크 시작\n\n");
//			throw new IllegalAccessException("정상적인 uri 인증 주소로 접근해주세요");
//		}
		
		//정리
		//1. @RequestParam의 디폴트가 required=true여서 그런지, 
		//uri = null이면 컨트롤러의 null체크 코드에는 오지도 못하고 바로 400이 터진다. 
		//톰캣의 400 페이지가 바로 나오기 때문에, 여기서 할 수 있는 일은 기본 400 페이지 대신 내가 만든 400페이지를 보여주는 것 뿐
		
		//2. uri = "" 이면, 컨트롤러 로직에서 널체크 및 에러 처리 가능. 
		// service쪽에서 runtimeException을 만들어 던졌으므로, 해당 ExceptionHandler에서 처리
		
	
		ModelAndView mv = new ModelAndView();
		String msg = "";
		try {
			joinService.emailAuth(uri);
			msg = "메일 인증이 성공적으로 완료되었습니다.";
		} catch (TimeoutException e) {
			msg = "인증메일의 유효기간이 만료되었습니다. 회원가입을 다시 시도해주세요.";
			//시간의 유효성 검사는, 인증 Y로 set하기 전에 이루어져야 하는것 아닐까?
			//왜 메일 인증이 회원 가입 이후에 일어나는 걸까? 메일 인증이 완료된 회원만 승인처리 하지 않고..
			//member 필드에도 auth_yn이 있는데 member_auth의 auth_yn과 어떤 차이가 있는걸까?
			mv.addObject("msg",msg);
		} 
		
		mv.addObject("msg", msg);
		mv.setViewName("login");
		
		return mv;
		
	}
	
	@ExceptionHandler(InvalidAuthUriException.class) 
	public String pageNotFound(InvalidAuthUriException e, Model m) {
		m.addAttribute("msg", e.getMessage());
		return "/error-page/404";
	}
	
}
