package com.doAn.demo.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
import com.doAn.demo.service.CartService;
import com.doAn.demo.service.CategoryService;
import com.doAn.demo.service.OrderDetailService;
import com.doAn.demo.service.OrderService;
import com.doAn.demo.service.ProductService;

import jakarta.servlet.http.HttpSession;

@Controller
public class OrderController {
	@Autowired
	HttpSession session;
	@Autowired
	CartService cartService;
	@Autowired
	OrderService orderService;
	@Autowired
	CategoryService cateSaervice;
	@Autowired 
	ProductService productService;
	@Autowired
	OrderDetailService orderDetailService;
	

	@GetMapping("/addToCart/{id}")
	public String addToCart(@PathVariable("id") Integer id,Model model) {
		Product p = productService.findByID(id);		
		cartService.add(id);		
		System.out.println(cartService.getAmount());
		String re = "redirect:/listCategory/" + p.getCategory().getId();
		
		return re;
	}
	
	@GetMapping("/detailProductaddToCart/{id}")
	public String detailProductaddToCart(@PathVariable("id") Integer id,Model model) {
		Product p = productService.findByID(id);		
		cartService.add(id);		
		System.out.println(cartService.getAmount());
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
		cartService.remove(id);
		
		Collection<Product> list = cartService.getAllItems();
		model.addAttribute("listProduct", list);
		
		return "redirect:/cart";
	}
	
	@GetMapping("/cart/clear")
	public String clearCart(Model model) {
		cartService.clear();		
		Collection<Product> list = cartService.getAllItems();
		model.addAttribute("listProduct", list);
		
		return "redirect:/cart";
	}
	@GetMapping("/cart/product/minus/{id}")
	public String minusProduct(@PathVariable("id") Integer id,Model model) {
		cartService.minus(id);
		Collection<Product> list = cartService.getAllItems();
		model.addAttribute("listProduct", list);
		
		return "redirect:/cart";
	}
	
	@GetMapping("/cart/product/add/{id}")
	public String addProduct(@PathVariable("id") Integer id,Model model) {		
		cartService.add(id);
		Collection<Product> list = cartService.getAllItems();
		model.addAttribute("listProduct", list);
		
		return "redirect:/cart";
	}

	
	@GetMapping("/cart/continue")
	public String continuePageCart(@ModelAttribute("order") Order order,Model model) {
		Customers userLogin = (Customers) session.getAttribute("customerLogin");
		order.setAddress(userLogin.getAddress());
		order.setAmount(cartService.getAmount());
		order.setNumberphone(userLogin.getNumberphone());
		
		order.setOrderdate( LocalDate.now());
		order.setReceiver(userLogin.getFullname());	
		order.setRequiredate(LocalDate.now());
	
		Collection<Product> list = cartService.getAllItems();
		model.addAttribute("listProduct", list);
		return "cartAndOrder/continue";
	}
	
	@PostMapping("/cart/continue")
	public String submitOrder(Order order,Model model) {
		Customers userLogin = (Customers) session.getAttribute("customerLogin");
		
		order.setCustomer(userLogin);
		order.setStatus(1);
			
		Collection<Product> listProduct = cartService.getAllItems();
		List<OrderDetail> listOrderDetail = new ArrayList<>();
		
		for(Product product:listProduct) {
			OrderDetail detail = new OrderDetail();
			detail.setQuantity(product.getQuantity());
			detail.setDiscount(product.getDiscount());
			detail.setOrder(order);
			detail.setProduct(product);
			detail.setUnitprice(product.getUnitprice());
			listOrderDetail.add(detail);
		}
		orderService.create(order, listOrderDetail);
		cartService.clear();
		
		List<Product> bestSell = new ArrayList<>();
		for(int i=0; i< 5; i++) {
			if(i == productService.getBestSell().size() -1) break;
			Product p = productService.getBestSell().get(i);
			bestSell.add(p);
		}
		model.addAttribute("listBestSeller", bestSell);
		
		//newest
		List<Product> newest = new ArrayList<>();
		for(int i=0; i< 5; i++) {
			if(i == productService.getNewest().size() -1) break;
			Product p = productService.getNewest().get(i);
			newest.add(p);
		}
		model.addAttribute("listNewest", newest);
		
		//Most Interest
		List<Product> inter = new ArrayList<>();
		for(int i =0; i< 5;i++) {
			if(i == productService.getMostInterest().size() -1) break;
			Product p = productService.getMostInterest().get(i);
			inter.add(p);
		}
		model.addAttribute("listInterest", inter);
		
		//Most Discount
		List<Product> dis = new ArrayList<>();
		for(int i =0; i< 5;i++) {
			if(i >= productService.getMostDiscount().size() -1) break;
			Product p = productService.getMostDiscount().get(i);
			dis.add(p);
		}
		model.addAttribute("listDiscount", dis);
		
		//Special
		List<Product> spec = new ArrayList<>();
		for(int i =0; i< 5;i++) {
			if(i == productService.findBySpecial().size() -1) break;
			Product p = productService.findBySpecial().get(i);
			spec.add(p);
			System.out.println(p.getId());
		}
		model.addAttribute("listSpec", spec);
		session.setAttribute("category", cateSaervice.findAll());
		return "redirect:/";
	}
	
	@GetMapping("/infor/customer/order/{id}")
	public String listOrder(Model model) {
		Customers userLogin = (Customers) session.getAttribute("customerLogin");
		
		List<Order> listOrder = orderService.findByCustomer(userLogin.getId());
		model.addAttribute("list", listOrder);
		return "cartAndOrder/listOrder";
	}
	
	@GetMapping("/cart")
	public String cart(Model model) {
		model.addAttribute("listProduct", cartService.getAllItems());
		return  "cartAndOrder/cart";
	}
	
	@GetMapping("/cart/order/{id}")
	public String orderByID(Model model,@PathVariable("id") Integer id) {
		model.addAttribute("list", orderDetailService.findByOrderID(id));
		model.addAttribute("order", orderService.findByID(id));
		return "cartAndOrder/detailOrder";
	}
	
}
