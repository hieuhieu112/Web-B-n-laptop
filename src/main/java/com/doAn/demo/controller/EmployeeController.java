package com.doAn.demo.controller;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.doAn.demo.entity.Admin;
import com.doAn.demo.entity.Contracts;
import com.doAn.demo.entity.Employee;
import com.doAn.demo.entity.Order;
import com.doAn.demo.entity.Product;
import com.doAn.demo.entity.Receipt;
import com.doAn.demo.entity.ReceiptDetail;
import com.doAn.demo.service.ContractService;
import com.doAn.demo.service.EmployeeService;
import com.doAn.demo.service.OrderDetailService;
import com.doAn.demo.service.OrderService;
import com.doAn.demo.service.ProducerService;
import com.doAn.demo.service.ProductService;
import com.doAn.demo.service.ReceiptDetailService;
import com.doAn.demo.service.ReceiptService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class EmployeeController {
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private HttpSession session;
	@Autowired
	private ProductService productService;
	@Autowired
	private ContractService contractService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private ReceiptService receiptService;
	@Autowired
	private ProducerService producerService;
	@Autowired
	private OrderDetailService orderDetailService;
	@Autowired
	private ReceiptDetailService receiptDetailService;
	
	public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\images\\";
	
	
	
	
	@GetMapping("/Employee/home")
	public String homeEmployee() {
		return "Employee/home";
	}
	
	@GetMapping("/Employee/myAccount")
	public String employeeMyAccount(Model model) {
		model.addAttribute("employee", session.getAttribute("employeeLogin"));
		
		return "Employee/myAccount";
	}
	@PostMapping("/Employee/saveAccount")
	public String employeeSaveAccount(Employee employee, HttpServletRequest request, @RequestParam("image") MultipartFile image) {
		Employee em = (Employee) session.getAttribute("employeeLogin");
		employee.setAccess(em.getAccess());
		employee.setActivated(em.getActivated());
		employee.setDepartment(em.getDepartment());
		employee.setEmpolyeetype(em.getEmpolyeetype());
		
		
		if(request.getParameter("date") != null && !request.getParameter("date").equals("")) {
			employee.setBirthday( LocalDate.parse(request.getParameter("date")));
		}
		
		if(!image.isEmpty()) {							
			try {
				String path = UPLOAD_DIRECTORY + "employee\\" + image.getOriginalFilename();
				System.out.println(path);
				image.transferTo(new File(path));				
				employee.setPhoto(image.getOriginalFilename());
				
			} catch (IllegalStateException | IOException e) {
				
				e.printStackTrace();
			}			
		}else {
		
			employee.setPhoto(((Employee) session.getAttribute("employeeLogin")).getPhoto());
			
		}
		
		employeeService.save(employee);
		session.setAttribute("employeeLogin", employee);
		return "Employee/home";
	}
	
	@GetMapping("/Employee/myContract")
	public String employeeMyContract(Model model) {
		
		List<Contracts> list = contractService.findByEmployeeID(((Employee)session.getAttribute("employeeLogin")).getId());
		model.addAttribute("list", list);
		return"Employee/listContract";
	}
	@GetMapping("/Employee/Contract/{id}")
	public String employeeDetailContract(@PathVariable("id") Integer id,Model model) {
		model.addAttribute("contract", contractService.findByID(id).get());
		return "Employee/detailContract";
	}
	
	@GetMapping("/Employee/listOrder")
	public String employeeListOrder(Model model) {
		model.addAttribute("listOrder", orderService.findAll());
		
		return "Employee/listOrder";
	}
	@PostMapping("/Employee/Order/search")
	public String searchOrderAdmin(Model model, HttpServletRequest request) {
		model.addAttribute("listOrder",orderService.findByKeyword("%" + request.getParameter("keyword") + "%"));
		return "Employee/listOrder";
	}
	@GetMapping("/Employee/Order/{id}")
	public String detailOrder(@PathVariable("id") Integer id,Model model) {
		model.addAttribute("listOrdeDetail", orderDetailService.findByOrderID(id));
		model.addAttribute("order", orderService.findByID(id));
		session.setAttribute("orderID", id);
		return "Employee/detailOrder";
	}
	@PostMapping("/Employee/saveOrder")
	public String saveOrder(HttpServletRequest request) {
		Order order = orderService.findByID((Integer) session.getAttribute("orderID"));
		order.setStatus(Integer.valueOf( request.getParameter("status")));
		orderService.save(order);
		return "redirect:/Employee/listOrder";
	}
	
	
	@GetMapping("/Employee/listProduct")
	public String employeeListProduct(Model model) {
		model.addAttribute("listProduct", productService.findAll());
		return "Employee/listProduct";
	}
	@GetMapping("/Employee/Product/{id}")
	public String detailProduct(@PathVariable("id") Integer id,Model model,HttpServletRequest request) {
		session.setAttribute("productid", id);
		Product p =  productService.findByID(id);
		p.setDescription(request.getParameter("Description"));
		model.addAttribute("product",p);		
		return "Employee/detailProduct";
	}
	@PostMapping("/Employee/product/search")
	public String searchProductAdmin(Model model,HttpServletRequest request) {
		model.addAttribute("listProduct", productService.findByKeyword("%" + request.getParameter("keyword") +"%" ));
		return "Employee/listProduct";
	}
	
	
	@GetMapping("/Employee/listProducer")
	public String listProducer(Model model) {
		model.addAttribute("listProducer", producerService.findAll());
		return "Employee/listProducer";
	}	
	@GetMapping("/Employee/Producer/{id}")
	public String detailProducer(@PathVariable("id") Integer id, Model model) {
		session.setAttribute("producerid", id);
		model.addAttribute("producer", producerService.findByID(id));
		model.addAttribute("listProduct", productService.findByProducer(id));
		return "Employee/detailProducer";
	}
	@GetMapping("/Employee/createReceipt/{id}")
	public String employeeCreateReceipt(@PathVariable("id") Integer id,Model model) {
		Receipt receipt = new Receipt();
		receipt.setDateimport(LocalDate.now());
		receipt.setEmployee((Employee) session.getAttribute("employeeLogin"));
		receipt.setProducer(producerService.findByID(id));
		receipt.setTotal(0.0f);
		
		session.setAttribute("receipt", receipt);
		model.addAttribute("receipt", receipt);
		model.addAttribute("listProduct",  productService.findByProducer(id));
		
		
		return "Employee/createReceipt";
	}
	@PostMapping("/Employee/createReceipt")
	public String adminSaveReceipt(Model model,HttpServletRequest request) {
		Receipt re = (Receipt) session.getAttribute("receipt");
		List<Product> listP = productService.findByProducer(re.getProducer().getId());
		receiptService.save(re);
		int checkEmpty = 0;
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
				checkEmpty = 1;
			}
		}
		if(checkEmpty == 0) {
			model.addAttribute("receipt", re);
			model.addAttribute("listProduct", listP);
			model.addAttribute("message", "Receipt can't be empty!");
			return "Employee/createReceipt";
		}
		
		receiptService.save(re);
		
		return "redirect:/Employee/listReceipt";
	}
	@GetMapping("/Employee/listReceipt")
	public String listReceipt(Model model) {
		model.addAttribute("listReceipt", receiptService.findAll());		
		return "Employee/listReceipt";
	}

	@GetMapping("/Employee/Receipt/{id}")
	public String detailReceipt(@PathVariable("id") Integer id,Model model) {
		model.addAttribute("listReceiptDetail", receiptDetailService.findByReceipt(id));
		model.addAttribute("receipt", receiptService.findByID(id).get());
		return "Employee/detailReceipt";
	}
}
