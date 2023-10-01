package com.doAn.demo.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.doAn.demo.entity.Order;
import com.doAn.demo.entity.OrderDetail;
import com.doAn.demo.entity.Product;
import com.doAn.demo.responsitory.OrderRepository;

@Service
public class OrderService {

	@Autowired
	private OrderRepository repos;
	@Autowired
	private OrderDetailService orderDetailService;
	@Autowired
	private CartService cartService;
	
	
	public List<Order> findAll(){
		return repos.findAll();
	}
	
	public Order findByID(Integer id) {
		return repos.findById(id).get();
	}

	public void deleteById(Integer id) {
		repos.deleteById(id);
	}
	public void save(Order order) {
		repos.save(order);
	}
	
	
	public void create(Order order, List<OrderDetail> details) {
		order.setOrderdetail(details);
		repos.save(order);
		
		for(OrderDetail detail:details) {
			orderDetailService.save(detail);
		}
	}
	
	
	public List<Order> findByCustomer(String idCustomer){	
		
		return repos.findByCustomer(idCustomer);
	}
	
	public List<Order> findByKeyword(String keyword){
		return repos.findByKeyWord(keyword);
	}
	
	public Float totalOrderByTime(Integer month, Integer year) {
		return repos.getTotalByMonth(month, year);
	}
	
	public List<Order> findByMonthYear(Integer month, Integer year){
		return repos.findByMonthYear(month, year);
	}
}
