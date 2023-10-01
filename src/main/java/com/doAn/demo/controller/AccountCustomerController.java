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

import com.doAn.demo.entity.Admin;
import com.doAn.demo.entity.Customers;
import com.doAn.demo.entity.Employee;
import com.doAn.demo.mapper.User;
import com.doAn.demo.service.AdminService;
import com.doAn.demo.service.CookieService;
import com.doAn.demo.service.CustomerService;
import com.doAn.demo.service.EmployeeService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpSession;


@Controller
public class AccountCustomerController {
	@Autowired
	private AdminService adminService;
	@Autowired
	private CookieService cookieService;
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private HttpSession sesion;
	@Autowired
	CustomerService customerService;

	@Autowired
	EmailSenderServices emailService;
	public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\images";


	@GetMapping("/login")
	public String loginPageGet(Model model) {
		Cookie temp1 = cookieService.read("adminid");
		Cookie temp2 = cookieService.read("adminpw");
		if(temp1 != null && temp2 != null) {
			model.addAttribute("adminpw", temp2.getValue());
			model.addAttribute("adminid",temp1.getValue());
		}
		
		return "home/Login";
	}
	
	@PostMapping(value = "/login")
	public String loginPage(ModelMap modelMap,Model model, User user,HttpSession session,@RequestParam(value = "rm", defaultValue = "false") boolean rm) {		
		
		try {
			Optional<Admin> ad = adminService.getByUsername(user.getUsername());
			//Admin
			if (!ad.isEmpty() && ad.get().getPass().equals( user.getPass())){
				Admin admin = ad.get();
				if(admin.getAccess() == 0) {
					modelMap.addAttribute("message", "This account can't access!");
					return "home/Login";
				}
				else if(rm == true)	{
					cookieService.create("adminid", admin.getUsername(), 30);
					cookieService.create("adminpw", admin.getPass(), 30);
				}
				session.setAttribute("Admin", admin);
					
				return "redirect:/Admin/home";
			}
			
			Optional<Customers> cus = customerService.findById(user.getUsername());
			if(!cus.isEmpty() && cus.get().getPass().equals(user.getPass())) {
				Customers customer =cus.get();
				if(customer.getActivated() == false) {
					modelMap.addAttribute("message", "This account can't access!");
					return "home/Login";
				}else if(rm == true) {
					cookieService.create("customerid", customer.getId(), 30);
					cookieService.create("customerpw", customer.getPass(), 30);
				}
				session.setAttribute("customerLogin", customer);
				model.addAttribute("customer", customer);
				
				//return home Customer
				String backLogin = (String) session.getAttribute("backLogin");
				if(backLogin != null) return "redirect:"+ backLogin;
				return "redirect:/";
			}
			
			Optional<Employee> em = employeeService.findByID(user.getUsername());
			if(!em.isEmpty() && em.get().getPass().equals(user.getPass())) {
				Employee employee = em.get();
				if(employee.getActivated() == false) {
					modelMap.addAttribute("message", "This account can't access!");
					return "home/Login";
				}
				if(rm == true) {
					cookieService.create("rememid", employee.getId(), 30);
					cookieService.create("remempw", employee.getPass(), 30);
				}
				
				
				session.setAttribute("employeeLogin", employee);
				return "redirect:/Employee/home";
			}
		} catch (Exception e) {
			modelMap.addAttribute("message", "Wrong information!");
			return "home/Login";
		}
		

		modelMap.addAttribute("message", "Wrong information!");
		model.addAttribute("adminid", user.getUsername());
		model.addAttribute("adminpw", user.getPass());
		return "home/Login";
	}
	
	@GetMapping("/logout")
	public String logOutCustomer() {
		sesion.removeAttribute("customerLogin");
		sesion.removeAttribute("backLogin");
		sesion.removeAttribute("employeeLogin");
		sesion.removeAttribute("Admin");
		return "redirect:/";
	}
	
	
	

	@GetMapping("/signUp")
	public String signUpPage() {
		
		return "Customer/signUp";
	}
	
	@PostMapping("registerCus")
	public String registerCustomer(Model model, Customers customer,@RequestParam("image") MultipartFile image) throws IllegalStateException, IOException {
		System.err.println(customerService.findById(customer.getId()));
		if(!customerService.findById(customer.getId()).isEmpty()) {
			model.addAttribute("customer", customer);
			model.addAttribute("message", "ID is using");
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
			emailService.sendEmail(email, "send Code", code);
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
			model.addAttribute("message", "Register success!");
			Customers cus = (Customers) sesion.getAttribute("customerRegister");
			cus.setActivated(true);
			customerService.save(cus);
			return "home/Login";
		}else {
			model.addAttribute("message", "Wrong code.");
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
			model.addAttribute("message", "Password and confirm password don't match");
			return"Customer/changeCustomerPass";
		}
		
	}
	
	
	@PostMapping("/checkConfirmFogotpass")
	public String checkConfirmFogotpass(@RequestParam("code") String codeType,Model model) {
		String code = (String) sesion.getAttribute("code");
		if(code.equals(codeType)) {
			return "Customer/changeCustomerPass";
		}
		model.addAttribute("message", "Wrong Code!");
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
		model.addAttribute("message", "Wrong information!");
		System.out.println("wrong info");
		return "Customer/forgotPass";
		
		
	}
	

	@GetMapping("/forgot")
	public String forgotPassPage() {
		return "Customer/forgotPass";
	}
	
}
