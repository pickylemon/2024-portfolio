package com.portfolio.www.util;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.portfolio.www.dto.EmailDto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class EmailUtil {
	private final JavaMailSender mailSender;
	
	//보내는 메일 형식에 따라 오버로딩된 메서드 중에서 선택 가능
	public String sendMail(EmailDto email) {
		return sendMail(email, false);
	}
	
	//해당 이메일이 html형식인지 아닌지는 EmailUtil에서 판단하기보단
	//외부에서 정해서 이렇게 넘겨주는게 맞다. 
	public String sendMail(EmailDto email, boolean isHtml) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
			messageHelper.setTo(email.getReceiver());
//			messageHelper.setText(email.getText());
			messageHelper.setFrom(email.getFrom());
			messageHelper.setSubject(email.getSubject());
			messageHelper.setText(email.getText(), isHtml);
			mailSender.send(message);
			
		} catch (Exception e) {
			e.printStackTrace(); //예외처리도 좀 더 신경써서 해보자.
			return "Error";
		}
		return "Success";
	}
	

}
