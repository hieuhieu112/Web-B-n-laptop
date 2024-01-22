package com.doAn.demo.controller;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.doAn.demo.entity.Category;
import com.doAn.demo.entity.Customers;
import com.doAn.demo.entity.Employee;
import com.doAn.demo.entity.Order;
import com.doAn.demo.entity.Producer;
import com.doAn.demo.entity.Product;
import com.doAn.demo.entity.Receipt;
import com.doAn.demo.entity.ReceiptDetail;
import com.doAn.demo.service.CategoryService;
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
import com.doAn.demo.service.RoleServices;

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
	private OrderService orderService;
	
	@Autowired
	private EmployeeService employService;
	
	@Autowired 
	private DepartmentService departmentService;
	
	@Autowired
	private EmployeeTypeService employeeTypeService;
	
	@Autowired
	private RoleServices roleServices;
	
	
	@Autowired
	private OrderDetailService orderDetailService;
	@Autowired
	private ReceiptDetailService receiptDetailService ;
	@Autowired
	private ReceiptService receiptService;
	
	public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\images\\";

	
	@Autowired
	private ProductService productService;
	
	
	@Autowired
	private ProducerService producerService;
	@Autowired
	private HttpSession session;
	
	@GetMapping("/Admin/home")
	public String homeAdmin() {
		return "Admin/homeAdmin";
	}
	
	@GetMapping("/Admin/myAccount")
	public String chaneAdminAccount(Model model,HttpSession session) {
		Employee ad = (Employee) session.getAttribute("Admin");
		model.addAttribute("employee", ad);
		return "Admin/accountAdmin";
	}
	
	@GetMapping("/Admin/AccountEmployee")
	public String ManageListAccountEmployee(Model model) {
		List<Employee> list = employService.findByRole(1);
		model.addAttribute("listEmployee", list);
		model.addAttribute("message", session.getAttribute("messageEmployee"));
		session.removeAttribute("messageEmployee");
		session.removeAttribute("idEmployeeChange");
		return "Admin/listAccountEmployee";
	}	
	
	@PostMapping("/Admin/saveAdminAccount")
	public String saveAdminAccount(Employee employee,HttpSession session,HttpServletRequest request, @RequestParam("image") MultipartFile image,Model model) {
		
		Employee ad = (Employee) session.getAttribute("Admin");
		employee.setAccess(ad.getAccess());
		employee.setActivated(ad.getActivated());
		employee.setDepartment(ad.getDepartment());
		employee.setEmpolyeetype(ad.getEmpolyeetype());
		employee.setOrder(ad.getOrder());
		employee.setReceipt(ad.getReceipt());
		employee.setRole(ad.getRole());
		
		if(request.getParameter("birthdaye") != null && !request.getParameter("birthdaye").equals("")) {
			employee.setBirthday( LocalDate.parse(request.getParameter("birthdaye")));
			
		}
		
		if(!image.isEmpty()) {							
			try {
				String path = UPLOAD_DIRECTORY + "employee\\" + image.getOriginalFilename();
				image.transferTo(new File(path));				
				employee.setPhoto(image.getOriginalFilename());
			} catch (IllegalStateException | IOException e) {
				employee.setPhoto(image.getOriginalFilename());
				e.printStackTrace();
			}			
		}else {
				employee.setPhoto(ad.getPhoto());
			
		}

		if(!employee.checkPhone()) {
			model.addAttribute("message", "Sai sdt");
			model.addAttribute("employee", employee);
					
			return "Admin/accountAdmin";
		}
		
		if(employee.checkEmptyField()) {
			employService.save(employee);
			
			session.setAttribute("Admin", employee);	
			return "redirect:/Admin/home";
		}else {
			model.addAttribute("message", "Thiếu thông tin!");
			model.addAttribute("employee", employee);
					
			return "Admin/accountAdmin";
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
		session.setAttribute("messageCate", "Thể loại đã chứa sản phẩm, không thể xoá!");
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
			
			model.addAttribute("message", "Thông tin có chứa dấu * là bắt buộc!");
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
		return "Admin/detailDepartment";
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
		return "Admin/detailEmployeeType";
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
		
		if(orderDetailService.findByProduct(id).isEmpty() && receiptDetailService.findByProduct(id).isEmpty()) {
			productService.deleteById(id);
		}else {
			session.setAttribute("messageDeleteProduct", "Sản phẩm đã có phiếu đặt hoặc phiếu nhập, không thể xóa");
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
		System.out.println(p.getProductdate());
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
			
			model.addAttribute("message", "Trường chứ * là bắt buộc");			
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
			session.setAttribute("messageDeleteProduct", "Sản phẩm đã lập phiếu đặt/ phiếu nhập thì không thể xoá!");
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
			session.setAttribute("messageDeleteProducer", "Nhà cung cấp đã được lập phiếu nhập thì không thể xoá!");
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
			model.addAttribute("message","Thông tin chứa dấu * là bắt buộc!" );
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
		p.setProducer(producerService.findByID(id));p.setProductdate(LocalDate.now());
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
		model.addAttribute("amount", receiptService.getAmount(id));
		return "Admin/detailReceipt";
	}
	
	
//	@PostMapping("/Admin/Receipt/search")
//	public String find
	
	@PostMapping("/Receipt/search")
		public String searchReceiptAdmin(Model model,HttpServletRequest request) {
			model.addAttribute("listReceipt", receiptService.findByKeyword("%" + request.getParameter("keyword")  +"%" ));
			if(session.getAttribute("Admin") != null) {
				return "Admin/listReceipt";
			}
			else {
				return "Employee/listReceipt";
			}
			
		}
	
	@GetMapping("/Admin/listReceipt/Add")
	public String adminAddreceipt(Model model) {
		model.addAttribute("listProducer", producerService.findAll());
		return "Admin/listProducer";
	}	
	
	@GetMapping("/Admin/createReceipt/{id}")
	public String adminCreateReceipt(@PathVariable("id") Integer id,Model model) {
		Receipt receipt = new Receipt();
		receipt.setDateimport(LocalDate.now());
		receipt.setEmployee((Employee) session.getAttribute("Admin"));
		receipt.setProducer(producerService.findByID(id));
		
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
			obj[0] = (categoryService.findByID((Integer) obj[0])).getNamecategory();
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
		
		List<Order> a =  orderService.findOrderByCustomer(id);
		if(!a.isEmpty()) {
			model.addAttribute("listOrder", a);
		}else {
			model.addAttribute("listOrder", null);
		}
		
		session.setAttribute("idCustomerChange", customer.getId());
		return "Admin/AccountCustomerDetail";
	}
	@GetMapping("/Admin/Customer/delete/{id}")
	public String deleteACustomer(@PathVariable("id") String id, Model model) {
		if(orderService.findOrderByCustomer(id).isEmpty()) {
			customerService.deleteByID(id);
			model.addAttribute("listCustomer", customerService.findAll());
			return "redirect:/Admin/AccountCustomer";
		}
		session.setAttribute("messageCustomer", "Khách đã mua thì không thể xóa");
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
		
		
		
		if(!customer.checkPhone()) {
			
			model.addAttribute("message", "Sai định dạng sdt");
			model.addAttribute("customer", customer);
			return "Admin/AccountCustomerDetail";
		}

		if(request.getParameter("date") != null && !request.getParameter("date").equals("")) {
			customer.setBirthday( LocalDate.parse(request.getParameter("date")));
			if(customer.checkDate()) {
				model.addAttribute("message", "Sai ngày");
				model.addAttribute("customer", customer);
				return "Admin/AccountCustomerDetail";
			}
			
		}else {
			if(session.getAttribute("idCustomerChange") != null) customer.setBirthday(customerService.findById((String) session.getAttribute("idCustomerChange")).get().getBirthday());
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
		if(!customer.checkEmptyField()) {
			if(session.getAttribute("idCustomerChange") != null){
				
				if(session.getAttribute("idCustomerChange").toString().equals(customer.getId())) customerService.save(customer);
				else {
					if(customerService.findById(customer.getId()).isEmpty()) customerService.save(customer);
					else {
						model.addAttribute("customer", customer);
						model.addAttribute("message", "ID đang được dùng.");
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
					model.addAttribute("message", "ID đang được dùng!");
									
					return "Admin/AccountCustomerDetail";
				}
			}
			return "redirect:/Admin/AccountCustomer";
		}else {
			model.addAttribute("message", "Thông tin * là bắt buộc!");
			model.addAttribute("customer", customer);
					
			return "Admin/AccountCustomerDetail";
		}	
	}
	
	
	
	
	@GetMapping("/Admin/Employee/delete/{id}")
	public String deleteAccountEmployee(@PathVariable("id") String id,Model model) {
		
		if(receiptService.findByEmployee(id).isEmpty()) {
			
			employService.deleteById(id);
			
		}else {
			String message = "Không thể xóa nhân viên đã tạo phiếu nhập!";
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
		
		session.removeAttribute("messageContract");
		
		return "Admin/AccountEmployeeDetail";
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
			if(session.getAttribute("idEmployeeChange") != null) {
				if(session.getAttribute("idEmployeeChange").toString().equals(employee.getId())) {
					employee.setPhoto((employService.findByID((String) session.getAttribute("idEmployeeChange"))).get().getPhoto());
				}
			}
			
		}

		employee.setDepartment(departmentService.findByID(request.getParameter("department")));			
		employee.setEmpolyeetype(employeeTypeService.findByID(request.getParameter("empolyeetype")));	
		employee.setRole(roleServices.findByID(1).get());
		
		model.addAttribute("department" , departmentService.findAll());
		model.addAttribute("employeeType" , employeeTypeService.findAll());
		if(employee.checkEmptyField()) {
			if(!employee.checkPhone()) {
				model.addAttribute("employee", employee);
				model.addAttribute("message", "Sai sdt");
				
				return "Admin/AccountEmployeeDetail";
			}
			if(employee.checkAge() == false) {
				model.addAttribute("message", "Không đủ tuổi!");
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
						model.addAttribute("message", "ID đã được sử dụng!");
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
					model.addAttribute("message", "ID đã được sử dụng!");
									
					return "Admin/AccountEmployeeDetail";
				}
			}
			return "redirect:/Admin/AccountEmployee";
		}else {
			System.out.println(employee.getAddress());
			model.addAttribute("message", "Thông tin chứ * là bắt buộc!");
			model.addAttribute("employee", employee);
					
			return "Admin/AccountEmployeeDetail";
		}	
	}
	
	
	@GetMapping("/Admin/listOrder")
	public String listOrder(Model model) {
		
		if(session.getAttribute("listOrder") != null) {
			List<Order>list = (List<Order>) session.getAttribute("listOrder");
			model.addAttribute("listOrder",list);
			session.removeAttribute("listOrder");
			
			return "Admin/listOrder";
		}
		
		List<Order>list = orderService.findByType(1);
		model.addAttribute("listOrder",list);
		
		return "Admin/listOrder";
	}
	
	@GetMapping("/Admin/Order/{id}")
	public String detailOrder(@PathVariable("id") Integer id,Model model) {
		model.addAttribute("listOrdeDetail", orderDetailService.findByOrderID(id));
		model.addAttribute("order", orderService.findByID(id));
		model.addAttribute("amount", orderService.getAmount(id));
		session.setAttribute("orderID", id);
		return "Admin/detailOrder";
	}
	@PostMapping("/Admin/saveOrder")
	public String saveOrder(HttpServletRequest request) {
		Order order = orderService.findByID((Integer) session.getAttribute("orderID"));
		order.setStatus(Integer.valueOf( request.getParameter("status")));
		order.setEmployee((Employee)session.getAttribute("Admin"));
		orderService.save(order);
		return "redirect:/Admin/listOrder";
	}
	
	@PostMapping("/Admin/order/search")
	public String searchOrderAdmin(Model model, HttpServletRequest request) {
		model.addAttribute("listOrder",orderService.findByKeyword("%" + request.getParameter("keyword") + "%"));
		return "Admin/listOrder";
	}
	@GetMapping("/Admin/logout")
	public String adminLogout() {
		session.removeAttribute("Admin");
		return "redirect:/";
	}
	
	
	
}
