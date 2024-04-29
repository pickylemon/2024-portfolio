package com.portfolio.www.service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

import com.portfolio.www.dto.EmailDto;
import com.portfolio.www.dto.MemberAuthDto;
import com.portfolio.www.exception.InvalidAuthUriException;
import com.portfolio.www.exception.TimeoutException;
import com.portfolio.www.repository.MemberAuthDao;
import com.portfolio.www.repository.MemberDao;
import com.portfolio.www.util.EmailUtil;

import at.favre.lib.crypto.bcrypt.BCrypt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class JoinService {
	private final MemberDao memberDao;
	private final MemberAuthDao memberAuthDao;
	private final EmailUtil emailUtil;

	public int join(HashMap<String, String> params) {
		//params에 담겨있는 값 : 이름, 이메일, 비밀번호
		//join 로직 순서
		//1. 비밀번호 암호화
		//2. MemberDao.join 멤버 가입
		//3. 인증메일 보내기 (인증메일 구조 만들기, 메일 발송)
		
		//비밀번호와 이메일 암호화
		String passwd = params.get("passwd");
		String mail = params.get("email");
		String encPasswd = BCrypt.withDefaults().hashToString(12, passwd.toCharArray());
		String encEmail = BCrypt.withDefaults().hashToString(12, mail.toCharArray());
		log.info(">>>>>>>>> encPasswd = {}", encPasswd);
		log.info(">>>>>>>>> encEmail = {}", encEmail);
		BCrypt.Result passwdResult = BCrypt.verifyer().verify(passwd.toCharArray(), encPasswd);
		BCrypt.Result emailResult = BCrypt.verifyer().verify(mail.toCharArray(), encEmail);
		log.info(">>>>>>>>> result.verified = {}", passwdResult.verified);
		log.info(">>>>>>>>> result.verified = {}", emailResult.verified);
		
		//암호화된 비밀번호로 바꿔서 저장하기
		params.put("passwd", encPasswd);
		params.put("email", encEmail);
		log.info("\n\n params.get('email')={} \n\n",params.get("email"));
		log.info("\n\n mail={}\n\n", mail);
		
		//이메일을 저장할 때는 암호화해서 저장하지만, mail을 보낼때는...복호화해서 보내야하는데?
		
		//가입
		int cnt = memberDao.join(params);
		if(cnt == 1) {
			//인증메일 구조 만들기
			//이 부분을 좀더 직관적이게 리팩토링 해보기
			int memberSeq = memberDao.getMemberSeq(params.get("memberId"));
			MemberAuthDto authDto = new MemberAuthDto();
			authDto.setMemberSeq(memberSeq);
			//UUID
			String uuid = UUID.randomUUID().toString().replaceAll("-",""); 
			log.info("\n\n uuid={} \n\n", uuid);
			authDto.setAuthUri(uuid);
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MINUTE, 3); //유효시간 3분
			authDto.setRegDtm(Calendar.getInstance().getTimeInMillis());
			authDto.setExpireDtm(cal.getTimeInMillis());
			memberAuthDao.addAuthInfo(authDto);
			
			//인증메일 구조 만들어서 발송
			EmailDto email = new EmailDto();
			email.setFrom("novecento104@naver.com");
//			email.setReceiver(params.get("email"));
			email.setReceiver(mail);
			email.setSubject("인증을 완료해주세요");
			
			//직접 String을 짜지 않고 코드로 간단한 html 만들어주는 builder없을까
			//UriComponentBuilder같은..
			String html = "<a href='http://localhost:8080/240423/emailAuth.do?uri="+ authDto.getAuthUri()+"'>인증하기</a>";
			email.setText(html);
			
			emailUtil.sendMail(email, true);
			//보내는 email이 html인지 아닌지는 직접 구분해서 보내주기.
		}
		return cnt;
	}
	
	@Transactional
	public int emailAuth(String uri) {
		MemberAuthDto authDto = memberAuthDao.getMemberAuthDto(uri);
		if(authDto == null || !uri.equals(authDto.getAuthUri())) { 
			//client가 잘못된 혹은 임의의 auth uri로 접근했을 경우
			throw new InvalidAuthUriException("유효하지 않은 인증 uri");
		}
		
		int memberSeq = authDto.getMemberSeq();
		
		//인증시간이 유효한지 검사하기
		long now = Calendar.getInstance().getTimeInMillis();
		long expireDtm = authDto.getExpireDtm();
		log.info("\nnow - expireDtm = {}\n", now-expireDtm);
		
		if(now > expireDtm) {
			throw new TimeoutException("인증메일 유효시간 만료.");
			//그리고??
			//사용자에게 다른 이메일 주소를 받아 다시 인증을 요청할 것인지
			//아니면 회원 가입 자체를 무효화할 것인지?
		}
	
		memberAuthDao.authValidation(uri);
		return memberDao.authValidation(memberSeq);
		//member_auth와 member테이블의 auth_yn을 모두 update한다. (@Transactional)
				
	}
}
