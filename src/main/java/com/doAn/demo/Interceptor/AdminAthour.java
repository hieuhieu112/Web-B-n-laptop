package com.doAn.demo.Interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.doAn.demo.entity.Admin;
import com.doAn.demo.entity.Customers;
import com.doAn.demo.service.DepartmentService;
import com.doAn.demo.service.EmployeeTypeService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class AdminAthour implements HandlerInterceptor{
	@Override
	public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response,
            Object handler)
     throws Exception{
		HttpSession session = request.getSession();
		Admin admin = (Admin) session.getAttribute("Admin");
		if(admin == null) {
			response.sendRedirect("/login");
			return false;
		}		
		return true;
	}
	
}
