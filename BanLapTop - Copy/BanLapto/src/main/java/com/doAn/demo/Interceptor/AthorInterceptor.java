package com.doAn.demo.Interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.doAn.demo.entity.Customers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class AthorInterceptor implements HandlerInterceptor{
	@Override
	public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response,
            Object handler)
     throws Exception{
		HttpSession session = request.getSession();
		Customers cus = (Customers) session.getAttribute("customerLogin");
		if(cus == null) {
			session.setAttribute("backLogin", request.getRequestURI());
			response.sendRedirect("/login");
			return false;
		}		
		return true;
	}
}
