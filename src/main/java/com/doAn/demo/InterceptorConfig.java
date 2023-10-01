package com.doAn.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.doAn.demo.Interceptor.AthorInterceptor;
import com.doAn.demo.Interceptor.EmployeeInterceptor;
import com.doAn.demo.Interceptor.AdminAthour;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

	@Autowired
	private AdminAthour adminAthor;
	@Autowired
	private AthorInterceptor author;
	@Autowired
	private EmployeeInterceptor employee;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(adminAthor).addPathPatterns("/Admin/**"); 
		registry.addInterceptor(author).addPathPatterns("/accountCustomer/**","/cart/continue");
		registry.addInterceptor(employee).addPathPatterns("/Employee/**");
	}
	
	
}
