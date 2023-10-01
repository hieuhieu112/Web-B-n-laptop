package com.doAn.demo.service;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class CookieService {

	@Autowired
	HttpServletRequest request;
	@Autowired
	HttpServletResponse respon;
	
	public Cookie create(String name, String value, int days) {
		String encode = Base64.getEncoder().encodeToString(value.getBytes());
		Cookie cookie = new Cookie(name, encode);
		cookie.setMaxAge(days*24*60*60);
		cookie.setPath("/");
		respon.addCookie(cookie);
		return cookie;
	}
	
	public Cookie read(String name) {
		Cookie[] cookies = request.getCookies();
		if(cookies == null){ return null;}
		for(Cookie cookie: cookies) {
			
			if(cookie.getName().equals(name)) {
				String val = new String (Base64.getDecoder().decode(cookie.getValue()));
				cookie.setValue(val);
				return cookie;
			}
		}
		return null;
	}
	
	public void delete(String key) {
		this.create(key, "", 0);
	}
}
