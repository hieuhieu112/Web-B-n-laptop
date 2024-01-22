package com.doAn.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.doAn.demo.Interceptor.AdminIntercepter;
import com.doAn.demo.Interceptor.AthorInterceptor;
import com.doAn.demo.Interceptor.EmployeeInterceptor;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

	
	@Autowired
	private AthorInterceptor author;
	@Autowired
	private EmployeeInterceptor employee;
	@Autowired
	private AdminIntercepter admin;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(admin).addPathPatterns("/Admin/**"); 
		registry.addInterceptor(author).addPathPatterns("/accountCustomer/**","/cart/**","order/**");
		registry.addInterceptor(employee).addPathPatterns("/Employee/**");
	}
	
	
}
