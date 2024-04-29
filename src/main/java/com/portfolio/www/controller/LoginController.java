package com.portfolio.www.controller;

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
	//로그인 페이지
	public String loginPage(Model model) {
		String msg = (String)model.getAttribute("msg");
		log.info("map={}", model.asMap().toString());
		log.info("\n\n msg={} \n\n", msg);
		
	
		return "login";
	}
	
//	@PostMapping("/login.do")
//	public String login() {
//		
//	}
	
	@GetMapping("/logout.do")
	public String logout(HttpSession session, RedirectAttributes rattr) {
		String memberId = (String)session.getAttribute("memberId");
		if(!ObjectUtils.isEmpty(memberId)) {
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
