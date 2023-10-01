package com.doAn.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderServices {

	@Autowired(required = false)
	private JavaMailSender mailSender;
	
	
	public void sendEmail (String toEmail,
						   	String toSubject,
						   	String body) {
		try {
			SimpleMailMessage mess = new SimpleMailMessage();
			mess.setFrom("hieuahieua@gmail.com");
			mess.setTo(toEmail);
			mess.setSubject(toSubject);
			mess.setText(body);
			
			mailSender.send(mess);
			System.out.println("Mail Success");
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
