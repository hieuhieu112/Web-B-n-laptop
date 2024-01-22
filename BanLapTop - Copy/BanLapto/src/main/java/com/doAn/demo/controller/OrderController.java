package com.doAn.demo.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.doAn.demo.entity.Customers;
import com.doAn.demo.entity.Order;
import com.doAn.demo.entity.OrderDetail;
import com.doAn.demo.entity.Product;
import com.doAn.demo.service.CategoryService;
import com.doAn.demo.service.EmployeeService;
import com.doAn.demo.service.OrderDetailService;
import com.doAn.demo.service.OrderService;
import com.doAn.demo.service.ProductService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class OrderController {
	@Autowired
	HttpSession session;
	
	@Autowired
	OrderService orderService;
	@Autowired
	CategoryService cateSaervice;
	@Autowired 
	ProductService productService;
	@Autowired
	OrderDetailService orderDetailService;
	@Autowired 
	EmployeeService employeeService;

	
	
	@GetMapping("/cart/addToCart/{id}")
	public String addToCart(@PathVariable("id") Integer id,Model model) {
		Product p = productService.findByID(id);
		Customers userLogin = (Customers) session.getAttribute("customerLogin");
		orderService.addProduct(userLogin, p);	
		String re = "redirect:/listCategory/" + p.getCategory().getId();
		
		return re;
	}
	
	@GetMapping("/cart/detailProductaddToCart/{id}")
	public String detailProductaddToCart(@PathVariable("id") Integer id,Model model) {
		Product p = productService.findByID(id);
		Customers userLogin = (Customers) session.getAttribute("customerLogin");
		orderService.addProduct(userLogin, p);	
		String re = "redirect:/listProduct/" + id;
		
		return re;
	}

	@GetMapping("/cart/product/{id}")
	public String viewProductDetail(@PathVariable("id") Integer id,Model model) {
		String re = "redirect:/listProduct/" + id.toString();
		return re;
	}
	@GetMapping("/cart/removeProduct/{id}")
	public String removeProductCart(@PathVariable("id") Integer id,Model model) {
		Product p = productService.findByID(id);
		Customers userLogin = (Customers) session.getAttribute("customerLogin");
		orderService.removeProduct(userLogin, p);
		
		Collection<Product> list = orderService.getAllProductFromCart(userLogin.getId());
		model.addAttribute("listProduct", list);
		
		return "redirect:/cart";
	}
	
	@GetMapping("/cart/clear")
	public String clearCart(Model model) {
		Customers userLogin = (Customers) session.getAttribute("customerLogin");
		orderService.clear(userLogin);		

		return "redirect:/cart";
	}
	@GetMapping("/cart/product/minus/{id}")
	public String minusProduct(@PathVariable("id") Integer id,Model model) {
		Product p = productService.findByID(id);
		Customers userLogin = (Customers) session.getAttribute("customerLogin");
		
		
		orderService.minusProduct(userLogin, p);
		Collection<Product> list = orderService.getAllProductFromCart(userLogin.getId());
		model.addAttribute("listProduct", list);
		
		return "redirect:/cart";
	}
	
	@GetMapping("/cart/product/add/{id}")
	public String addProduct(@PathVariable("id") Integer id,Model model) {	
		Product p = productService.findByID(id);
		Customers userLogin = (Customers) session.getAttribute("customerLogin");
		orderService.addProduct(userLogin, p);
		List<Product> list = orderService.getAllProductFromCart(userLogin.getId());
		model.addAttribute("listProduct", list);
		
		return "redirect:/cart";
	}

	
	@GetMapping("/cart/continue")
	public String continuePageCart(Model model) {
		Customers userLogin = (Customers) session.getAttribute("customerLogin");
		
	
		Collection<Product> list = orderService.getAllProductFromCart(userLogin.getId());
		if(list == null ||list.isEmpty()) {
			
			return "redirect:/cart";
		}
		
		Order order = orderService.findCartByCustomer(userLogin.getId());
		model.addAttribute("orderDetails", order.getOrderdetail());
		model.addAttribute("amount", orderService.getAmount(order.getId()));
		model.addAttribute("order", order);
		return "cartAndOrder/continue";
	}
	
	@PostMapping("/cart/continue")
	public String submitOrder(Order order1,Model model) {
		Customers userLogin = (Customers) session.getAttribute("customerLogin");
		Order cart = orderService.findCartByCustomer(userLogin.getId());
		
		if(cart == null) {
			return "redirect:/cart";
		}else {
			
			
			List<Product> listProduct = orderService.getAllProductFromCart(((Customers) session.getAttribute("customerLogin")).getId());
			if(listProduct.isEmpty()) {
				
				return "redirect:/cart";
			}
			
			List<OrderDetail> listOrderDetail = new ArrayList<>();
			for(Product product:listProduct) {
				OrderDetail detail = new OrderDetail();
				detail.setQuantity(product.getQuantity());
				detail.setDiscount(product.getDiscount());
				detail.setOrder(cart);
				detail.setProduct(product);
				detail.setUnitprice(product.getUnitprice());
				listOrderDetail.add(detail);
				
				Product p = productService.findByID(product.getId());
				p.setQuantity(p.getQuantity() - 1);
				productService.save(p);
			}
			
			cart.setType(1);
			
			cart.setOrderdetail(listOrderDetail);
			for (OrderDetail orderDetail : listOrderDetail) {
				orderDetailService.save(orderDetail);
			}

			
			return "redirect:/order/" + userLogin.getId();
		}		
	}
	
	@GetMapping("/order/{id}")
	public String listOrder(Model model) {
		Customers userLogin = (Customers) session.getAttribute("customerLogin");
		
		List<Order> listOrder = orderService.findOrderByCustomer(userLogin.getId());
		model.addAttribute("list", listOrder);
		return "cartAndOrder/listOrder";
	}
	
	@GetMapping("/cart")
	public String cart(Model model) {
		
		Order o = orderService.findCartByCustomer(((Customers)session.getAttribute("customerLogin")).getId());
		model.addAttribute("order", o);
		
		return  "cartAndOrder/cart";
	}
	
	@GetMapping("/cart/order/{id}")
	public String orderByID(Model model,@PathVariable("id") Integer id) {
		model.addAttribute("list", orderDetailService.findByOrderID(id));
		model.addAttribute("order", orderService.findByID(id));
		model.addAttribute("amount", orderService.getAmount(id));
		session.setAttribute("idOrder", id);
		return "cartAndOrder/detailOrder";
	}
	
	@PostMapping("/Customer/saveOrder")
	public String saveOrder(Model model,HttpServletRequest request) {
		Customers userLogin = (Customers) session.getAttribute("customerLogin");
		
		List<Order> listOrder = orderService.findOrderByCustomer(userLogin.getId());
		model.addAttribute("list", listOrder);
		
		Integer id = (Integer) session.getAttribute("idOrder");
		Order order = orderService.findByID(id);
		order.setStatus(3);
		
		orderService.save(order);
		
		return "redirect:/infor/customer/order/" + userLogin.getId();
	}
}
