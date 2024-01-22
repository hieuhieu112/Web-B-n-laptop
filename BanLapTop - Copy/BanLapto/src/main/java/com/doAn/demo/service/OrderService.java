package com.doAn.demo.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doAn.demo.entity.Customers;
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
	
	public void minusProduct(Customers cus, Product p) {
		Order o = repos.findCartByCustomer(cus.getId());
		List<OrderDetail> odList = orderDetailService.findByOrderID(o.getId());
		if(odList == null) {
			return;
		}
		
		for (OrderDetail orderDetail : odList) {
			
			if(orderDetail.getProduct().getId() == p.getId()) {
				
				if(orderDetail.getQuantity() == 1) {
					
					orderDetailService.deleteById(orderDetail.getId());
					odList.remove(orderDetail);
				}else {
					
					orderDetail.setQuantity(orderDetail.getQuantity() - 1);
					orderDetailService.save(orderDetail);
				}
				
				break;
			}
		}
		if(odList.isEmpty()) {
			o.setOrderdetail(null);
			
		}
	}
	
	public void clear(Customers cus) {
		Order order = repos.findCartByCustomer(cus.getId());
		List<OrderDetail> odList = orderDetailService.findByOrderID(order.getId());
		
		
		for (OrderDetail orderDetail : odList) {
			orderDetailService.deleteById(orderDetail.getId());
		}
		
		odList.clear();
		order.setOrderdetail(null);
	}
	
	public void removeProduct(Customers customer,Product p){
		Order o = repos.findCartByCustomer(customer.getId());
		List<OrderDetail> odList = orderDetailService.findByOrderID(o.getId());
		
		
		for (OrderDetail orderDetail : odList) {
			
			if(orderDetail.getProduct().getId() == p.getId()) {
				orderDetailService.deleteById(orderDetail.getId());
				odList.remove(orderDetail);
				break;
			}
		}
		
		o.setOrderdetail(odList);
	}
	
	
	public void addProduct(Customers customer,Product p){	
		Order order = repos.findCartByCustomer(customer.getId());
		if(order == null) {
			Order newOrder = new Order();
			newOrder.setAddress(customer.getAddress());
			newOrder.setCustomer(customer);
			newOrder.setDescription("");
			newOrder.setNumberphone(customer.getNumberphone());
			newOrder.setOrderdate(LocalDate.now());
			newOrder.setReceiver(customer.getFullname());
			newOrder.setStatus(1);
			newOrder.setType(0);
			
			
			OrderDetail oDetail = new OrderDetail();
			oDetail.setDiscount(p.getDiscount());
			oDetail.setOrder(newOrder);
			oDetail.setProduct(p);
			oDetail.setQuantity(1);
			oDetail.setUnitprice(p.getUnitprice());
			
			List<OrderDetail> odList = new ArrayList<>();
			odList.add(oDetail);
			newOrder.setOrderdetail(odList);
			
			repos.save(newOrder);
			orderDetailService.save(oDetail);
			
		}else {
			List<OrderDetail> listOD = order.getOrderdetail();
			
			
			OrderDetail oDetail = orderDetailService.findByOrderPro(order.getId(), p.getId());
			if(oDetail == null) {
				oDetail = new OrderDetail();
				oDetail.setDiscount(p.getDiscount());
				oDetail.setOrder(order);
				oDetail.setProduct(p);
				oDetail.setQuantity(1);
				oDetail.setUnitprice(p.getUnitprice());
				listOD.add(oDetail);
				
				
			}else {
				oDetail.setQuantity(oDetail.getQuantity() + 1);
			}
			
			orderDetailService.save(oDetail);
			
		}
	}
	
	
	public List<Order> findOrderByCustomer(String idCustomer){	
		
		return repos.findOrderByCustomer(idCustomer);
	}
	

	public Order findCartByCustomer(String idCustomer){	
		
		return repos.findCartByCustomer(idCustomer);
	}
	
	public List<Order> findByKeyword(String keyword){
		return repos.findByKeyWord(keyword);
	}
	
	public List<Order> findByType(Integer type){
		return repos.findByType(type);
	}
	
	public Float totalOrderByTime(Integer month, Integer year) {
		List<Order> list = repos.findByMonthYear(month, year);
		Float total = 0.0f;
		for(int i =0; i< list.size(); i++) {
			total += repos.findAmountByID(list.get(i).getId());
		}
		return total;
	}
	
	public Float getAmount(Integer id) {
		return repos.findAmountByID(id);
	}
	
	public List<Product> getAllProductFromCart(String Customerid) {
		Order cart  =  repos.findCartByCustomer(Customerid);
		if(cart == null) {
			return null;
		}
		else {
			List<OrderDetail> oDetail = orderDetailService.findByOrderID(cart.getId());
			List<Product> result = new ArrayList<>();
			for (OrderDetail o : oDetail) {
				result.add(o.getProduct());
			}
			return result;
		}
	}
	
	public List<Order> findByMonthYear(Integer month, Integer year){
		return repos.findByMonthYear(month, year);
	}
}
