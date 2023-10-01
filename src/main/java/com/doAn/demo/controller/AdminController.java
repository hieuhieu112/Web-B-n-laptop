package com.doAn.demo.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.doAn.demo.entity.Admin;
import com.doAn.demo.entity.Bonus;
import com.doAn.demo.entity.Category;
import com.doAn.demo.entity.Contracts;
import com.doAn.demo.entity.Customers;
import com.doAn.demo.entity.Departments;
import com.doAn.demo.entity.Employee;
import com.doAn.demo.entity.EmployeeTypes;
import com.doAn.demo.entity.Order;
import com.doAn.demo.entity.Producer;
import com.doAn.demo.entity.Product;
import com.doAn.demo.entity.Receipt;
import com.doAn.demo.entity.ReceiptDetail;
import com.doAn.demo.responsitory.ProducerRepository;
import com.doAn.demo.service.AdminService;
import com.doAn.demo.service.BonusService;
import com.doAn.demo.service.CartService;
import com.doAn.demo.service.CategoryService;
import com.doAn.demo.service.ContractService;
import com.doAn.demo.service.CustomerService;
import com.doAn.demo.service.DepartmentService;
import com.doAn.demo.service.EmployeeService;
import com.doAn.demo.service.EmployeeTypeService;
import com.doAn.demo.service.OrderDetailService;
import com.doAn.demo.service.OrderService;
import com.doAn.demo.service.ProducerService;
import com.doAn.demo.service.ProductService;
import com.doAn.demo.service.ReceiptDetailService;
import com.doAn.demo.service.ReceiptService;

import jakarta.persistence.criteria.CriteriaBuilder.In;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {
	final Integer YEARTIMESTART= 2023;
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private EmployeeService employService;
	
	@Autowired 
	private DepartmentService departmentService;
	
	@Autowired
	private EmployeeTypeService employeeTypeService;
	
	@Autowired
	private BonusService bonusService;
	
	@Autowired
	private ContractService contractService;
	
	public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\images\\";

	@Autowired
	private AdminService adminServce;
	@Autowired
	private ProductService productService;
	
	@Autowired
	private OrderService orderService;
	@Autowired
	private ProducerService producerService;
	@Autowired
	private CartService cartService;
	@Autowired
	private HttpSession session;
	@Autowired
	private OrderDetailService orderDetailService;
	@Autowired
	private ReceiptService receiptService;
	@Autowired
	private ReceiptDetailService receiptDetailService;
	
	@GetMapping("/Admin/home")
	public String homeAdmin() {
		return "Admin/homeAdmin";
	}
	
	@GetMapping("/Admin/myAccount")
	public String chaneAdminAccount(Model model,HttpSession session) {
		Admin ad = (Admin) session.getAttribute("Admin");
		model.addAttribute("ad", ad);
		return "Admin/accountAdmin";
	}	
	@PostMapping("/Admin/saveAdminAccount")
	public String saveAdminAccount(@RequestParam("repass")String repass,Admin ad,Model model) {
		if(!repass.equals(ad.getPass())) {
			model.addAttribute("ad", ad);
			model.addAttribute("message", "Confirm password doesn't match password!");
			return "Admin/accountAdmin";
		}
		ad.setAccess(1);
		adminServce.save(ad);
		session.setAttribute("Admin", ad);	
		return "redirect:/Admin/home";
	}	
	
	
	
	@GetMapping("/Admin/AccountCustomer")
	public String ManageListAccountCustomer(Model model) {
		model.addAttribute("listCustomer", customerService.findAll());
		model.addAttribute("message", session.getAttribute("messageCustomer"));
		session.removeAttribute("messageCustomer");
		return "Admin/listAccountCustomer";
	}
	@GetMapping("/Admin/Customer/{id}")
	public String detailAccountCustomer(@PathVariable("id") String id,Model model) {
		Customers customer = customerService.findById(id).get();
		model.addAttribute("customer", customer);
		model.addAttribute("listOrder", orderService.findByCustomer(id));
		session.setAttribute("idCustomerChange", customer.getId());
		return "Admin/AccountCustomerDetail";
	}
	@GetMapping("/Admin/Customer/delete/{id}")
	public String deleteACustomer(@PathVariable("id") String id, Model model) {
		if(orderService.findByCustomer(id).isEmpty()) {
			customerService.deleteByID(id);
			model.addAttribute("listCustomer", customerService.findAll());
			return "redirect:/Admin/AccountCustomer";
		}
		session.setAttribute("messageCustomer", "Customer created Order can't be delete!");
		model.addAttribute("listCustomer", customerService.findAll());
		return "redirect:/Admin/AccountCustomer";
	}		
	@GetMapping("/Admin/listAccountCustomer/Add")
	public String createCustomer(Model  model) {
		Customers customer = new Customers();
		model.addAttribute("customer", customer);
		session.removeAttribute("customer");
		return "Admin/AccountCustomerDetail";
	}	
	@PostMapping("/Admin/saveAccountCustomer")
	public String saveAccountCustomer(Model model, Customers customer,@RequestParam("image") MultipartFile image, HttpServletRequest request) {
		

		if(request.getParameter("date") != null && !request.getParameter("date").equals("")) {
			customer.setBirthday( LocalDate.parse(request.getParameter("date")));
		}
		if(!image.isEmpty()) {							
			try {
				String path = UPLOAD_DIRECTORY + "employee\\" + image.getOriginalFilename();
				image.transferTo(new File(path));
				customer.setPhoto(image.getOriginalFilename());
			} catch (IllegalStateException | IOException e) {
				
				e.printStackTrace();
			}			
		}
		if(customer.checkEmptyField()) {
			if(session.getAttribute("idCustomerChange") != null){
				
				if(session.getAttribute("idCustomerChange").toString().equals(customer.getId())) customerService.save(customer);
				else {
					if(customerService.findById(customer.getId()).isEmpty()) customerService.save(customer);
					else {
						model.addAttribute("customer", customer);
						model.addAttribute("message", "ID is using for another customer!");
						String redirect = "redirect:/Admin/Customer/" + session.getAttribute("idCustomerChange").toString();
						return redirect;
					}
				}
				model.addAttribute("listCustomer", customerService.findAll());
				session.removeAttribute("idCustomerChange");
			}
			else {
				if(customerService.findById(customer.getId()).isEmpty()) {
					customerService.save(customer);
				}else {
					model.addAttribute("customer", customer);
					model.addAttribute("message", "ID is using for another customer!");
									
					return "Admin/AccountCustomerDetail";
				}
			}
			return "redirect:/Admin/AccountCustomer";
		}else {
			model.addAttribute("message", "Fields containing * are required!");
			model.addAttribute("customer", customer);
					
			return "Admin/AccountCustomerDetail";
		}	
	}
	
	
	
	@GetMapping("/Admin/AccountEmployee")
	public String ManageListAccountEmployee(Model model) {
		List<Employee> list = employService.findAll();
		model.addAttribute("listEmployee", list);
		model.addAttribute("message", session.getAttribute("messageEmployee"));
		session.removeAttribute("messageEmployee");
		session.removeAttribute("idEmployeeChange");
		return "Admin/listAccountEmployee";
	}	
	@GetMapping("/Admin/Employee/delete/{id}")
	public String deleteAccountEmployee(@PathVariable("id") String id,Model model) {
		
		if(contractService.findByEmployeeID(id).isEmpty()) {
			
			employService.deleteById(id);
			
		}else {
			String message = "This Employee is having Contract, you must delete it first!";
			session.setAttribute("messageEmployee", message );			
		}
		List<Employee> list = employService.findAll();
		model.addAttribute("listEmployee", list);
		return "redirect:/Admin/AccountEmployee";
	}	
	@GetMapping("/Admin/Employee/{id}")
	public String detailAccountEmployee(@PathVariable("id") String id,Model model) {		
		Employee employee = employService.findByID(id).get();		
		model.addAttribute("employee", employee);		
		session.setAttribute("idEmployeeChange", id);
		model.addAttribute("department" , departmentService.findAll());
		model.addAttribute("employeeType" , employeeTypeService.findAll());
		model.addAttribute("messageContract", session.getAttribute("messageContract"));
		model.addAttribute("list", contractService.findByEmployeeID(id));
		session.removeAttribute("messageContract");
		
		return "Admin/AccountEmployeeDetail";
	}
	@GetMapping("/Admin/employee/Contract/delete/{id}")
	public String deleteContractInEmployee(@PathVariable("id") Integer id, Model model,HttpServletRequest request) {
		if(contractService.findByID(id).get().getStarttime().isAfter( LocalDate.now())) {
			contractService.deleteByID(id);
		}else {
			session.setAttribute("messageContract",  "Too late to delete contract!");				
		}
		model.addAttribute("listContract", contractService.findAll());		
		return "redirect:/Admin/Employee/" + session.getAttribute("idEmployeeChange");
	}
	@GetMapping("/Admin/listEmployee/AddContract")
	public String addContractInEmployee(Model model) {	
		Contracts co =  new Contracts();
		co.setEmployee(employService.findByID((String) session.getAttribute("idEmployeeChange")).get());
		model.addAttribute("contract", co);	
		session.setAttribute("backContract", "/Admin/Employee/" + co.getEmployee().getId());
		return "Admin/createContract";
	}
	@GetMapping("/Admin/listAccountEmployee/Add")
	public String createEmployee(Model  model) {
		Employee employee = new Employee();
		model.addAttribute("employee", employee);	
		model.addAttribute("department" , departmentService.findAll());
		model.addAttribute("employeeType" , employeeTypeService.findAll());
		return "Admin/AccountEmployeeDetail";
	}	
	@PostMapping("/Admin/saveAccountEmployee")
	public String saveAccountEmployee(Model model,HttpServletRequest request, Employee employee,@RequestParam("image") MultipartFile image) {
		
		if(request.getParameter("birthdaye") != null && !request.getParameter("birthdaye").equals("")) {
			employee.setBirthday( LocalDate.parse(request.getParameter("birthdaye")));
			
		}
		
		if(!image.isEmpty()) {							
			try {
				String path = UPLOAD_DIRECTORY + "employee\\" + image.getOriginalFilename();
				image.transferTo(new File(path));				
				employee.setPhoto(image.getOriginalFilename());
			} catch (IllegalStateException | IOException e) {
				
				e.printStackTrace();
			}			
		}else {
			if(session.getAttribute("idEmployeeChange").toString().equals(employee.getId())) {
				employee.setPhoto((employService.findByID((String) session.getAttribute("idEmployeeChange"))).get().getPhoto());
			}
		}

		employee.setDepartment(departmentService.findByID(request.getParameter("department")));			
		employee.setEmpolyeetype(employeeTypeService.findByID(request.getParameter("empolyeetype")));	
		
		System.out.println(employee.getBirthday());
		model.addAttribute("department" , departmentService.findAll());
		model.addAttribute("employeeType" , employeeTypeService.findAll());
		if(employee.checkEmptyField()) {
			if(employee.checkAge() == false) {
				model.addAttribute("message", "Not enough age!");
				model.addAttribute("employee", employee);
				return "Admin/AccountEmployeeDetail";
			}
			
			if(session.getAttribute("idEmployeeChange") != null){
				if(session.getAttribute("idEmployeeChange").toString().equals(employee.getId())) employService.save(employee);
				else {
					if(customerService.findById(employee.getId()).isEmpty()) {
						
						employService.save(employee);
					}else {
						model.addAttribute("employee", employee);
						model.addAttribute("message", "ID is using for another employee!");
						String redirect = "redirect:/Admin/Employee/" + session.getAttribute("idEmployeeChange").toString();
						return redirect;
					}
				}
				model.addAttribute("listEmployee", employService.findAll());
				session.removeAttribute("idEmployeeChange");
			}
			else {
				if(employService.findByID(employee.getId()).isEmpty()) {
					
					employService.save(employee);
				}else {
					model.addAttribute("employee", employee);
					model.addAttribute("message", "ID is using for another employee!");
									
					return "Admin/AccountEmployeeDetail";
				}
			}
			return "redirect:/Admin/AccountEmployee";
		}else {
			System.out.println(employee.getAddress());
			model.addAttribute("message", "Fields containing * are required!");
			model.addAttribute("employee", employee);
					
			return "Admin/AccountEmployeeDetail";
		}	
	}
	
	
	
	
	@GetMapping("/Admin/listCategory")
	public String ManageListCategory(Model model) {
		model.addAttribute("listCategory",categoryService.findAll());
		model.addAttribute("message", session.getAttribute("messageCate"));
		session.removeAttribute("messageCate");
		return "Admin/listCategory";
	}	
	@GetMapping("/Admin/Category/delete/{id}")
	public String deleteCategory(@PathVariable("id") Integer id, Model model) {
		if(productService.findByCategory(id).isEmpty()) {
			categoryService.deleteByID(id);
			session.setAttribute("category", categoryService.findAll());
			model.addAttribute("listCategory");
			return "redirect:/Admin/listCategory";
		}
		session.setAttribute("messageCate", "Category had product, Can't delete it!");
		model.addAttribute("listCategory");
		return "redirect:/Admin/listCategory";
	}	
	@GetMapping("/Admin/Category/{id}")
	public String detailCategory(@PathVariable("id") Integer id,Model model) {
		model.addAttribute("category", categoryService.findByID(id));
		session.setAttribute("categoryid", id);
		return "Admin/detailCategory";
	}
	@GetMapping("/Admin/listCategory/Add")
	public String addCategory(Model model) {
		Category category = new Category();
		model.addAttribute("category", category);
		session.removeAttribute("categoryid");
		return "Admin/detailCategory";
	}
	@PostMapping("/Admin/Category/save")
	public String saveCategory(Model model,Category category,@RequestParam("image") MultipartFile image) {
		
		if(!image.isEmpty()) {							
			try {
				String path = UPLOAD_DIRECTORY + "category\\" + image.getOriginalFilename();
				image.transferTo(new File(path));				
				category.setPhoto(image.getOriginalFilename());
			} catch (IllegalStateException | IOException e) {
				
				e.printStackTrace();
			}			
		}else {
			category.setPhoto(categoryService.findByID((Integer) session.getAttribute("categoryid")).getPhoto());
		}
		
		
		
		if(!category.checkEmptyField()  == true) {
			
			model.addAttribute("message", "Fields containing * are required!");
			model.addAttribute("category", category);
			return "Admin/detailCategory";
		}
		if(session.getAttribute("categoryid") != null) {
			category.setId((Integer) session.getAttribute("categoryid"));
		}
		
		
		categoryService.save(category);
		session.removeAttribute("categoryid");
		session.setAttribute("category", categoryService.findAll());
		return "redirect:/Admin/listCategory";
	}
	
	
	
	@GetMapping("/Admin/listDepartment")
	public String ManageListDepartment(Model model) {
		model.addAttribute("listDepartment", departmentService.findAll() );
		model.addAttribute("message", session.getAttribute("messageDepartment"));
		session.removeAttribute("messageDepartment");
		return "Admin/listDepartment";
	}	
	@GetMapping("/Admin/Department/{id}")
	public String detailDepartment(@PathVariable("id") String id, Model model) {
		model.addAttribute("department", departmentService.findByID(id));
		session.setAttribute("departmentid", id);
		
		model.addAttribute("listEmployee", employService.findByDepartment(id));
		return "Admin/detailDepartment";
	}	
	@GetMapping("/Admin/Department/delete/{id}")
	public  String deleteDepartment(@PathVariable ("id") String id, Model model) {
		if(employService.findByDepartment(id).isEmpty()) {
			departmentService.deleteByID(id);
		}else {
			session.setAttribute("messageDepartment", "Department had employee, can't delete!");
		}
		
		model.addAttribute("listDepartment", departmentService.findAll() );
		return "redirect:/Admin/listDepartment";
	}
	@GetMapping("/Admin/listDepartment/create")
	public String addDepartment(Model model) {
		session.removeAttribute("departmentid");
		Departments department = new Departments();
		model.addAttribute("department", department);
		return "Admin/detailDepartment";
	}	
	@PostMapping("/Admin/listDepartment/save")
	public String saveDepartment(Model model,Departments department) {
		if(department.checkEmptyField()) {
			model.addAttribute("message","Fields containing * are required!");
			model.addAttribute("department", department);
			return "Admin/detailDepartment";
		}
		
		if(session.getAttribute("departmentid") != null) {
			departmentService.deleteByID((String) session.getAttribute("departmentid"));
		}
		
		departmentService.save(department);
		model.addAttribute("listDepartment", departmentService.findAll() );
		return "redirect:/Admin/listDepartment";
	}
	
	
	
	
	
	@GetMapping("/Admin/employeeType")
	public String ManageListEmployeeType(Model model) {
		model.addAttribute("listEmployeeType", employeeTypeService.findAll());
		model.addAttribute("message", session.getAttribute("deleteEmployeeType"));
		session.removeAttribute("deleteEmployeeType");
		return"Admin/listEmployeeType";
	}	
	@GetMapping("/Admin/EmployeeType/{id}")
	public String detailEmployeeType(@PathVariable("id") String id ,Model model) {
		model.addAttribute("employeetype", employeeTypeService.findByID(id));
		session.setAttribute("employeeType", id);
		model.addAttribute("listEmployee", employService.findByType(id));
		model.addAttribute("message", session.getAttribute("messageEmployeeType"));
		session.removeAttribute("messageEmployeeType");
		return "Admin/detailEmployeeType";
	}
	@GetMapping("/Admin/EmloyeeType/Employee/delete/{id}")
	public String deleteAccountEmployeeInType(@PathVariable("id") String id,Model model) {
		
		if(contractService.findByEmployeeID(id).isEmpty()) {
			
			employService.deleteById(id);
			
		}else {
			String message = "This Employee is having Contract, you must delete it first!";
			session.setAttribute("messageEmployeeType", message );			
		}
		model.addAttribute("listEmployee", employService.findByType(id));
		model.addAttribute("message", session.getAttribute("messageEmployeeType"));
		model.addAttribute("employeetype", employeeTypeService.findByID((String) session.getAttribute("employeeType")));
		return "redirect:/Admin/EmployeeType/" + (String) session.getAttribute("employeeType");
	}	
	@GetMapping("/Admin/listEmployeeType/Add")
	public String createEmployeeType(Model model) {
		model.addAttribute("employeetype", new EmployeeTypes());
		session.removeAttribute("employeeType");
		return "Admin/detailEmployeeType";
	}
	@GetMapping("/Admin/EmployeeType/delete/{id}")
	public String deleteEmployeeType(@PathVariable("id") String id, Model model) {
		if(employService.findByType(id).isEmpty()) {
			employeeTypeService.deleteById(id);
		}else {
			session.setAttribute("deleteEmployeeType", "Employee Type had employee, can't delete!");
		}
		
		model.addAttribute("listEmployeeType", employeeTypeService.findAll());
		return"redirect:/Admin/employeeType";
	}
	@PostMapping("/Admin/saveEmployeeType")
	public String saveEmployeetype(Model model,EmployeeTypes employeeType) {
		
		if(!employeeType.checkEmptyField()) {
			model.addAttribute("employeetype",employeeType);
			model.addAttribute("message", "Fields containing * are required!");
			return "Admin/detailEmployeeType";
		}
		
		if(session.getAttribute("employeeType") != null && !((String) session.getAttribute("employeeType")).equals(employeeType.getId())) {
			employeeTypeService.deleteById((String) session.getAttribute("employeeType"));
		}
		employeeTypeService.save(employeeType);
		session.removeAttribute("employeeType");
		model.addAttribute("listEmployeeType", employeeTypeService.findAll());
		return "redirect:/Admin/employeeType";
	}
	
	
	
	@GetMapping("/Admin/listBonus")
	public String ManageListBonus(Model model) {
		List<Bonus> list = bonusService.findAll();
		model.addAttribute("listBonus", list);
				
		return"Admin/listBonus";
	}	
	@GetMapping("/Admin/Bonus/delete/{id}")
	public String seleteBonus(@PathVariable Integer id, Model model) {
		bonusService.deleteByID(id);
		List<Bonus> list = bonusService.findAll();
		model.addAttribute("listBonus", list);
		return "redirect:/Admin/listBonus";
	}	
	@GetMapping("/Admin/Bonus/{id}")
	public String detailBonus(@PathVariable("id") Integer id,Model model) {
		model.addAttribute("bonus", bonusService.findByID(id));
		session.setAttribute("bonusid", id);
		return "Admin/detailBonus";
	}
	@GetMapping("/Admin/listBonus/Add")
	public String addNewBonus(Model model) {
		Bonus bonus =  new Bonus();
		model.addAttribute("bonus", bonus);
		bonus.setAdmin((Admin) session.getAttribute("Admin"));
		session.removeAttribute("bonusid");
		return "Admin/detailBonus";
	}
	@PostMapping("/Admin/listBonus/Save")
	public String createBonus(Bonus bonus, Model model,HttpServletRequest request) {
		if(request.getParameter("datewritee") != null && !request.getParameter("datewritee").equals("")) {
			bonus.setDatewrite( LocalDate.parse(request.getParameter("datewritee")));
		}
		if(request.getParameter("datewritee") != null && !request.getParameter("datewritee").equals("")) {
			bonus.setDatestart(LocalDate.parse(request.getParameter("datewritee")));
		}
		if(request.getParameter("datewritee") != null && !request.getParameter("datewritee").equals("")) {
			bonus.setDateend( LocalDate.parse(request.getParameter("datewritee")));
		}
		
		
		
		bonus.setAdmin((Admin) session.getAttribute("Admin"));
		if(bonus.checkEmptyField() == false) {
			
			
			model.addAttribute("bonus", bonus);
			model.addAttribute("message", "Fields containing * are required!");
			
			return "Admin/detailBonus";
		}
		
		if(session.getAttribute("bonusid") != null) {
			bonus.setId((Integer) session.getAttribute("bonusid"));
		}
		
		bonusService.save(bonus);
			List<Bonus> list = bonusService.findAll();
			model.addAttribute("listBonus", list);
		
		
		return "redirect:/Admin/listBonus";
	}
	

	
	@GetMapping("/Admin/listProduct")
	public String ManageListProduct(Model model) {
		model.addAttribute("listProduct", productService.findAll());
		model.addAttribute("message", session.getAttribute("messageDeleteProduct"));
		session.removeAttribute("messageDeleteProduct");
		return "Admin/listProduct";
	}
	
	@GetMapping("/Admin/Product/{id}")
	public String detailProduct(@PathVariable("id") Integer id,Model model) {
		session.setAttribute("productid", id);
		session.removeAttribute("createProductInProducer");
		model.addAttribute("product", productService.findByID(id));
		model.addAttribute("listCategory",categoryService.findAll());
		model.addAttribute("listProducer", producerService.findAll());
		return "Admin/detailProduct";
	}
	@GetMapping("/Admin/Product/delete/{id}")
	public String deleteProduct(@PathVariable("id") Integer id, Model model) {
		System.out.println(orderDetailService.findByProduct(id).isEmpty());
		System.out.println(receiptDetailService.findByReceipt(id).isEmpty());
		if(orderDetailService.findByProduct(id).isEmpty() && receiptDetailService.findByProduct(id).isEmpty()) {
			productService.deleteById(id);
		}else {
			session.setAttribute("messageDeleteProduct", "Product had in Order or Receipt can't delete!");
		}
		
		model.addAttribute("listProduct", productService.findAll());
		return "redirect:/Admin/listProduct";
	}
	@GetMapping("/Admin/Product/Add")
	public String createProduct(Model model) {
		session.removeAttribute("productid");
		session.removeAttribute("createProductInProducer");
		Product p =new Product();
		p.setProductdate(LocalDate.now());
		model.addAttribute("product", p);
		model.addAttribute("listProducer", producerService.findAll());
		model.addAttribute("listCategory", categoryService.findAll());
		return "Admin/detailProduct";
	}
	@PostMapping("/Admin/Product/save")
	public String saveProduct(Model model, Product product,@RequestParam("image") MultipartFile image,HttpServletRequest request) {
		product.setDescription(request.getParameter("description"));
		if(request.getParameter("productdatee") != null && !request.getParameter("productdatee").equals("")) {
			product.setProductdate( LocalDate.parse(request.getParameter("productdatee")));
		}
		
		if(!image.isEmpty()) {							
			try {
				String path = UPLOAD_DIRECTORY + "product\\" + image.getOriginalFilename();
				System.out.println(path);
				image.transferTo(new File(path));				
				product.setPhoto(image.getOriginalFilename());
				
			} catch (IllegalStateException | IOException e) {
				
				e.printStackTrace();
			}			
		}else {
			if(session.getAttribute("productid") != null) {
				product.setPhoto(productService.findByID((Integer) session.getAttribute("productid")).getPhoto());
			}
		}
		
		if(session.getAttribute("productid") != null) {
			product.setQuantity(productService.findByID((Integer) session.getAttribute("productid")).getQuantity());
		}
		
		if(session.getAttribute("productid") != null && !session.getAttribute("productid").equals("")) {
			product.setId((Integer) session.getAttribute("productid"));
		}
		
		product.setCategory(categoryService.findByID(Integer.valueOf(request.getParameter("category"))));
		
		if(product.checkEmptyField()) {
			
			model.addAttribute("message", "Fields containing * are required!");			
			model.addAttribute("product", product);
			model.addAttribute("listProducer", producerService.findAll());

			model.addAttribute("listCategory", categoryService.findAll());
			return "Admin/detailProduct";
		}
		
					
		//product.setCategory(categoryService.findByID());		
		
		productService.save(product);
		if(session.getAttribute("createProductInProducer") != null) {
			String srt = (String) session.getAttribute("createProductInProducer");
			session.removeAttribute("createProductInProducer");
			return "redirect:" + srt;
		}
		model.addAttribute("listProduct", productService.findAll());
		return "redirect:/Admin/listProduct";
	}
	@PostMapping("/Admin/product/search")
	public String searchProductAdmin(Model model,HttpServletRequest request) {
		model.addAttribute("listProduct", productService.findByKeyword("%" + request.getParameter("keyword") +"%" ));
		return "Admin/listProduct";
	}

	
	
	
	
	@GetMapping("/Admin/listContract")
 	public String listContract(Model model) {
		model.addAttribute("listContract", contractService.findAll());		
		model.addAttribute("message", session.getAttribute("messageDelContract"));
		session.removeAttribute("messageDelContract");
		return"Admin/listContract";
	}	
	@PostMapping("/Admin/contract/search")
	public String findContract(Model model,HttpServletRequest request) {
		model.addAttribute("listContract", contractService.findByKeyWord("%" + request.getParameter("keyword") + "%"));		
		return"Admin/listContract";		
	}
	@GetMapping("/Admin/Contract/{id}")
	public String detailContract(@PathVariable("id") Integer id,Model model) {
		model.addAttribute("contract", contractService.findByID(id).get());
		session.setAttribute("contractid", id);
		session.removeAttribute("backContract");
		return "Admin/detailContract";
	}
	@GetMapping("/Admin/Contract/delete/{id}")
	public String deleteContract(@PathVariable("id") Integer id, Model model,HttpServletRequest request) {
		if(contractService.findByID(id).get().getStarttime().isAfter( LocalDate.now())) {
			contractService.deleteByID(id);
		}else {
			session.setAttribute("messageDelContract", "Too late to delete contract!");
		}
		
		model.addAttribute("listContract", contractService.findAll());		
		return "redirect:/Admin/listContract";
	}
	@GetMapping("/Admin/listContract/Add")
	public String addContract(Model model) {		
		model.addAttribute("contract", new Contracts());	
		session.removeAttribute("contractid");
		session.removeAttribute("backContract");
		return "Admin/createContract";
	}
	@PostMapping("/Admin/Contract/save")
	public String saveContract(Model model, Contracts contract,HttpServletRequest request) {		
		if(request.getParameter("endtimee") != null && !request.getParameter("endtimee").equals("")) {
			contract.setEndtime(LocalDate.parse(request.getParameter("endtimee")));
		}
		
		if(request.getParameter("starttimee") != null && !request.getParameter("starttimee").equals("")) {
			contract.setStarttime(LocalDate.parse(request.getParameter("starttimee")));
		}
		
		if(session.getAttribute("contractid") != null) {
			 contract.setId( (Integer) session.getAttribute("contractid"));			 
		}
		
		if(request.getParameter("idEmployee") != null && !request.getParameter("idEmployee").equals("") ) {
			if(employService.findByID(request.getParameter("idEmployee")).isEmpty()) {
				model.addAttribute("contract", contract);		
				model.addAttribute("message", "Wrong Employee ID");
				return "Admin/createContract";
			}
			else {
				contract.setEmployee(employService.findByID(request.getParameter("idEmployee")).get());
			}
		}
		
		if(contract.checkEmptyField()) {
			model.addAttribute("contract", contract);		
			model.addAttribute("message", "Fields containing * are required!");
			return "Admin/createContract";
		}
		
		if(!contract.checkTime()) {
			model.addAttribute("contract", contract);		
			model.addAttribute("message", "Time  Wrong!");
			return "Admin/createContract";
		}
		List<Contracts> list = contractService.findByEmployeeID(contract.getEmployee().getId());
		if(!list.isEmpty()) {
			for (Contracts con : list) {
				if(con.getEndtime().isAfter(contract.getStarttime())) {
					model.addAttribute("contract", contract);		
					model.addAttribute("message", "Another contract in force during that time!");
					return "Admin/createContract";
				}
			}
		}
		
		session.removeAttribute("contractid");
		contractService.save(contract);
		if(session.getAttribute("backContract") != null ) {
			
			return "redirect:/" + session.getAttribute("backContract");
		}
		return "redirect:/Admin/listContract";
	}
	
	
	@GetMapping("/Admin/listOrder")
	public String listOrder(Model model) {
		model.addAttribute("listOrder",orderService.findAll());
		return "Admin/listOrder";
	}
	@PostMapping("/Admin/Order/search")
	public String searchOrderAdmin(Model model, HttpServletRequest request) {
		model.addAttribute("listOrder",orderService.findByKeyword("%" + request.getParameter("keyword") + "%"));
		return "Admin/listOrder";
	}
	@GetMapping("/Admin/Order/{id}")
	public String detailOrder(@PathVariable("id") Integer id,Model model) {
		model.addAttribute("listOrdeDetail", orderDetailService.findByOrderID(id));
		model.addAttribute("order", orderService.findByID(id));
		session.setAttribute("orderID", id);
		return "Admin/detailOrder";
	}
	@PostMapping("/Admin/saveOrder")
	public String saveOrder(HttpServletRequest request) {
		Order order = orderService.findByID((Integer) session.getAttribute("orderID"));
		order.setStatus(Integer.valueOf( request.getParameter("status")));
		orderService.save(order);
		return "redirect:/Admin/listOrder";
	}
	
	
	
	
	@GetMapping("/Admin/listProducer")
	public String listProducer(Model model) {
		model.addAttribute("listProducer", producerService.findAll());
		model.addAttribute("message", session.getAttribute("messageDeleteProducer"));
		session.removeAttribute("messageDeleteProducer");
		return "Admin/listProducer";
	}	
	@GetMapping("/Admin/Producer/{id}")
	public String detailProducer(@PathVariable("id") Integer id, Model model) {
		session.setAttribute("producerid", id);
		model.addAttribute("producer", producerService.findByID(id));
		model.addAttribute("listProduct", productService.findByProducer(id));
		model.addAttribute("messageDeleteProduct", session.getAttribute("messageDeleteProduct"));
		session.removeAttribute("messageDeleteProduct");
		return "Admin/detailProducer";
	}
	@GetMapping("/Admin/Producer/deleteProduct/{id}")
	public String deleteProductInProducer(@PathVariable("id") Integer id, Model model) {
		Integer idProducer = productService.findByID(id).getProducer().getId();
		if(orderDetailService.findByProduct(id).isEmpty() && receiptDetailService.findByProduct(id).isEmpty()) {
			productService.deleteById(id);
		}else {
			session.setAttribute("messageDeleteProduct", "Product had in Order or Receipt can't delete!");
		}
		
		model.addAttribute("producer", producerService.findByID(idProducer));
		model.addAttribute("listProduct", productService.findByProducer(idProducer));
		return "redirect:/Admin/Producer/" + idProducer;
	}
	@GetMapping("/Admin/Producer/delete/{id}")
	public String deleteProducer(@PathVariable("id") Integer id, Model model) {
		
		Producer p = (producerService.findByID(id));
		if(p.getProduct().isEmpty()) {
			producerService.deleteById(id);
			model.addAttribute("listProducer", producerService.findAll());
			return "redirect:/Admin/listProducer";
		}
		else {
			session.setAttribute("messageDeleteProducer", "The supplier who provided the product cannot be deleted");
		}	
		
		model.addAttribute("listProducer", producerService.findAll());
		return "redirect:/Admin/listProducer";
	}
	@GetMapping("/Admin/listProducer/Add")
	public String addProducer(Model model) {
		model.addAttribute("producer", new Producer());
		session.removeAttribute("producerid");
		return "Admin/detailProducer";
	}
	@PostMapping("/Admin/Producer/save")
	public String saveProducer(Model model,Producer producer,@RequestParam("image") MultipartFile image) {
		System.out.println(session.getAttribute("producerid"));
		
		if(session.getAttribute("producerid") != null) {
			producer.setId((Integer) session.getAttribute("producerid"));
		}
		
		if(producer.checkEmptyField()) {
			model.addAttribute("producer", producer);
			model.addAttribute("message","Fields containing * are required!" );
			return "Admin/detailProducer";
		}
		
		if(!image.isEmpty()) {							
			try {
				String path = UPLOAD_DIRECTORY + "producer\\" + image.getOriginalFilename();
				System.out.println(path);
				image.transferTo(new File(path));				
				producer.setLogo(image.getOriginalFilename());
			} catch (IllegalStateException | IOException e) {
				
				e.printStackTrace();
			}			
		}else {
			if(session.getAttribute("producerid") != null) {
				producer.setLogo(producerService.findByID((Integer) session.getAttribute("producerid")).getLogo());
			}
		}
		
		producerService.save(producer);
		session.removeAttribute("producerid");
		return "redirect:/Admin/listProducer";
	}
	@GetMapping("/Admin/Producer/AddProduct/{id}")
	public String createProductInProducer(Model model,@PathVariable("id") Integer id) {
		session.removeAttribute("productid");
		session.setAttribute("createProductInProducer", "/Admin/Producer/" + id);
		Product p = new Product();
		p.setProducer(producerService.findByID(id));
		model.addAttribute("product",p);
		model.addAttribute("listProducer", producerService.findAll());
		model.addAttribute("listCategory", categoryService.findAll());
		return "Admin/detailProduct";
	}
	
	
	
	@GetMapping("/Admin/listReceipt")
	public String listReceipt(Model model) {
		model.addAttribute("listReceipt", receiptService.findAll());		
		return "Admin/listReceipt";
	}
	@GetMapping("/Admin/Receipt/{id}")
	public String detailReceipt(@PathVariable("id") Integer id,Model model) {
		model.addAttribute("listReceiptDetail", receiptDetailService.findByReceipt(id));
		model.addAttribute("receipt", receiptService.findByID(id).get());
		return "Admin/detailReceipt";
	}
	
	
//	@PostMapping("/Admin/Receipt/search")
//	public String find
	
	@GetMapping("/Admin/listReceipt/Add")
	public String adminAddreceipt(Model model) {
		model.addAttribute("listProducer", producerService.findAll());
		return "Admin/listProducer";
	}	
	
	@GetMapping("/Admin/createReceipt/{id}")
	public String adminCreateReceipt(@PathVariable("id") Integer id,Model model) {
		Receipt receipt = new Receipt();
		receipt.setDateimport(LocalDate.now());
		receipt.setAdmin((Admin) session.getAttribute("Admin"));
		receipt.setProducer(producerService.findByID(id));
		receipt.setTotal(0.0f);
		
		session.setAttribute("receipt", receipt);
		model.addAttribute("receipt", receipt);
		model.addAttribute("listProduct",  productService.findByProducer(id));
		
		return "Admin/createReceipt";
	}
	
	@PostMapping("/Admin/createReceipt")
	public String adminSaveReceipt(Model model,HttpServletRequest request) {
		Receipt re = (Receipt) session.getAttribute("receipt");
		List<Product> listP = productService.findByProducer(re.getProducer().getId());
		receiptService.save(re);
		for (Product p : listP) {
			Integer quantity =Integer.valueOf(request.getParameter("quantity" + p.getId()));//String 
			Integer unit =Integer.valueOf(request.getParameter("unit" + p.getId()));//String
			if(unit !=0 && quantity!=0) {
				ReceiptDetail detail = new ReceiptDetail();
				detail.setProduct(p);
				detail.setQuantity(quantity);
				detail.setReceipt(re);
				detail.setUnitprice(unit);
				receiptDetailService.save(detail);
				re.setTotal( re.getTotal() + (detail.getQuantity() * detail.getUnitprice()));
				p.setQuantity(p.getQuantity() + quantity);
			}
			
		}
		
		receiptService.save(re);
		
		return "redirect:/Admin/listReceipt";
	}
	
	
	
	
	
	@GetMapping("/Admin/statisticsByTime")
	public String statisticsByTime(Model model, HttpServletRequest request) {
		LocalDate time =LocalDate.now();
		
		
		ArrayList<ArrayList<String>> values = new ArrayList<>();
		for(int i = YEARTIMESTART;i <= time.getYear(); i++) {
			
				for(int j = 1; j< 13; j++) {
					ArrayList<String> value = new ArrayList<>();
					if(i == time.getYear() && j > time.getMonthValue()) {
						break;
					}else {
						value.add(String.valueOf(j));
						value.add(String.valueOf(i));
						value.add(String.valueOf(orderService.totalOrderByTime(j, i)));
						value.add(String.valueOf(receiptService.getTotalByMonth(j,i)));
					}
					values.add(value);
				}
		}
		Collections.reverse(values);
		model.addAttribute("value", values);
		
		
		return "Admin/statisticsByMonth";
	}
	
	@GetMapping("/Admin/statisticsByCategory")
	public String statisticsByCategory(Model model) {
		
		List<Object[]> ob = orderDetailService.revenueByCategory();
		for (Object[] obj : ob) {
			obj[0] = (categoryService.findByID((Integer) obj[0])).getNamecategoryen();
		}
		model.addAttribute("list", ob);
		return "Admin/statisticsByCategory";
	}
	@GetMapping("/Admin/topCategory/{id}")
	public String topCategoryID(Model model,@PathVariable("id") Integer id) {
		model.addAttribute("listOrderDetail", orderDetailService.findByProduct(id));
		return "Admin/listProduct";
	}
	@GetMapping("/Admin/OrderMonth/{month}/{year}")
	public String orderByMonth(Model model, @PathVariable("month") Integer month, @PathVariable("year") Integer year) {
		model.addAttribute("listOrder", orderService.findByMonthYear(month,year));
		
		
		return "Admin/listOrder";	
	}
	
	@GetMapping("/Admin/statisticsByProduct")
	public String statisticsByProduct(Model model) {
		model.addAttribute("list", productService.revenueByProduct());
		return "Admin/statisticsByProduct";
	}
	
	@GetMapping("/Admin/ReceiptMonth/{month}/{year}")
	public String receiptByMonth(Model model, @PathVariable("month") Integer month, @PathVariable("year") Integer year) {
		model.addAttribute("listReceipt", receiptService.findByMonthYear(month, year));		
		return "Admin/listReceipt";
	}
	
	
	@GetMapping("/Admin/logout")
	public String adminLogout() {
		session.removeAttribute("Admin");
		return "redirect:/";
	}
	
	
	
}
