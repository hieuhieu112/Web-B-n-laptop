package com.doAn.demo.Interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.doAn.demo.entity.Employee;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class AdminIntercepter implements HandlerInterceptor{
	
	@Override
	public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response,
            Object handler)
     throws Exception{
		HttpSession session = request.getSession();
		Employee employee = (Employee) session.getAttribute("Admin");
		if(employee == null) {
			response.sendRedirect("/login");
			return false;
		}		
		return true;
	}
}
