package com.doAn.demo.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.doAn.demo.entity.Customers;
import com.doAn.demo.entity.Employee;
import com.doAn.demo.mapper.User;
import com.doAn.demo.service.CustomerService;
import com.doAn.demo.service.EmployeeService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpSession;


@Controller
public class Login {
	
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private HttpSession sesion;
	@Autowired
	CustomerService customerService;

	@Autowired
	Email emailService;
	public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\images";


	@GetMapping("/login")
	public String loginPageGet(Model model) {
		return "home/Login";
	}
	
	@PostMapping(value = "/login")
	public String loginPage(ModelMap modelMap,Model model, User user,HttpSession session) {		
		
		try {
			Optional<Customers> cus = customerService.findById(user.getUsername());
			if(!cus.isEmpty() && cus.get().getPass().equals(user.getPass())) {
				Customers customer =cus.get();
				if(customer.getActivated() == false) {
					modelMap.addAttribute("message", "Tài khoản chưa được kích hoạt");
					return "home/Login";
				}
				session.setAttribute("customerLogin", customer);
				model.addAttribute("customer", customer);
				
				if(session.getAttribute("backLogin") != null) {
					return "redirect:" + session.getAttribute("backLogin");
				}
				
				return "redirect:/";
			}
			
			
					
			
			Optional<Employee> em = employeeService.findByID(user.getUsername());
			if(!em.isEmpty() && em.get().getPass().equals(user.getPass())) {
				Employee employee = em.get();
				if(employee.getActivated() == false) {
					modelMap.addAttribute("message", "Tài khoản hưa được kích hoạt");
					return "home/Login";
				}
				if(employee.getRole().getId() == 1) {
					session.setAttribute("employeeLogin", employee);
					return "redirect:/Employee/home";
				}else {
					session.setAttribute("Admin", employee);
					return "redirect:/Admin/home";
				}	
			}
		} catch (Exception e) {
			modelMap.addAttribute("message", "Sai thông tin");
			return "home/Login";
		}
		

		modelMap.addAttribute("message", "Sai thông tin");
		return "home/Login";
	}
	
	
	
	@GetMapping("/logout")
	public String logOutCustomer() {
		sesion.removeAttribute("customerLogin");
		sesion.removeAttribute("employeeLogin");
		sesion.removeAttribute("Admin");
		return "redirect:/";
	}
	
	
	

	@GetMapping("/signUp")
	public String signUpPage(Model model) {		
		
		return "Customer/signUp";
	}
	
	@PostMapping("registerCus")
	public String registerCustomer(Model model, Customers customer,@RequestParam("image") MultipartFile image) throws IllegalStateException, IOException {
		
		if(!customerService.findById(customer.getId()).isEmpty()) {
			
			model.addAttribute("message", "ID đã được sử dụng");
			return "Customer/signUp";
		}
		
		if(customer.checkDate()) {
			model.addAttribute("message", "Sai ngày");
			return "Customer/signUp";
		}
		
		if(customer.checkRe()) {
			model.addAttribute("message", "Thiếu thông tin");
			return "Customer/signUp";
		}
		
		if(!customer.checkPhone()) {
			
			model.addAttribute("message", "Sai định dạng sdt");
			return "Customer/signUp";
		}
		
		if(image.isEmpty()) {
			customer.setPhoto("kh.jpg");
		}else {			
			StringBuilder fileNames = new StringBuilder();
			Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY +"\\customer\\", image.getOriginalFilename());
			fileNames.append(image.getOriginalFilename());
			Files.write(fileNameAndPath, image.getBytes());
			   
			customer.setPhoto(fileNames.toString());
		}
		customer.setActivated(false);
		customerService.save(customer);
		sesion.setAttribute("customerRegister", customer);
		return "redirect:/sendMailActive";
	}
	
	@GetMapping("/sendMailActive")
	public String sendMail( ModelMap modelMap,Model model) {
		model.addAttribute("customerLogin", sesion.getAttribute("customerLogin"));
		try {
			String code = String.valueOf(Math.random());
			code = code.substring(2, 6);
			String email = ((Customers) sesion.getAttribute("customerRegister")).getEmail();
			emailService.sendEmail(email, "Code đăng ký web bán laptop", code);
			sesion.setAttribute("code", code);
			return "Customer/confirm";
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			modelMap.addAttribute("message", "Gui that bai");
		}
		return "home/index";
	}
	
	@PostMapping("/checkConfirm")
	public String checkConfirm(@RequestParam("code")String code,Model model) {
		model.addAttribute("customerLogin", sesion.getAttribute("customerLogin"));
		if(code.equals(sesion.getAttribute("code"))) {
			sesion.removeAttribute("code"); 
			model.addAttribute("message", "Đăng ký thành công!");
			Customers cus = (Customers) sesion.getAttribute("customerRegister");
			cus.setActivated(true);
			customerService.save(cus);
			return "home/Login";
		}else {
			model.addAttribute("message", "Code sai");
			return "Customer/confirm";
		}
		
		
	}
	
	@PostMapping("/checkNewPassCustomer")
	public String checkNewPassCustomer(@RequestParam("pw") String pw,@RequestParam("repw") String repw,Model model) {
		if(pw.equals(repw)) {
			Customers cus = ((Customers) sesion.getAttribute("customerChangePass"));
			System.out.println(cus);
			System.out.println(cus.getId());
			System.out.println(repw);
			cus.setPass(repw);
			customerService.save(cus);
			model.addAttribute("adminid", cus.getId());
			model.addAttribute("adminpw", cus.getPass());
			sesion.removeAttribute("customerRegister");
			return "home/Login";
		}
		else {
			model.addAttribute("message", "Mật khẩu và nhập lại mật khẩu bị sai");
			return"Customer/changeCustomerPass";
		}
		
	}
	
	
	@PostMapping("/checkConfirmFogotpass")
	public String checkConfirmFogotpass(@RequestParam("code") String codeType,Model model) {
		String code = (String) sesion.getAttribute("code");
		if(code.equals(codeType)) {
			return "Customer/changeCustomerPass";
		}
		model.addAttribute("message", "Sai Code!");
		return "Customer/confirmFogotPass";
	}

	
	@PostMapping("/sendMailForgot")
	public String takeCodeTakePass(@RequestParam("username") String username,Model model) {
		Optional<Customers> cus = customerService.findById(username);
		System.out.println(cus);
		if(!cus.isEmpty()) {
			Customers customer = cus.get();
			sesion.setAttribute("customerChangePass",customer); 
			String code = String.valueOf(Math.random());
			code = code.substring(2, 6);
			sesion.setAttribute("code", code);
			emailService.sendEmail(customer.getEmail(), "send Code", "Code: " + code);
			return "Customer/confirmFogotPass";
		}
		model.addAttribute("message", "Sai thông tin");
		System.out.println("wrong info");
		return "Customer/forgotPass";	
	}	

	@GetMapping("/forgot")
	public String forgotPassPage() {
		return "Customer/forgotPass";
	}
	
}
