package com.doAn.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doAn.demo.entity.OrderDetail;
import com.doAn.demo.responsitory.OrderDetailRepository;

@Service
public class OrderDetailService {

	@Autowired
	private OrderDetailRepository repos;
	
	public OrderDetail save(OrderDetail detail) {
		return repos.save(detail);
	}
	
	public List<OrderDetail> findByOrderID(Integer id){
		return repos.findByOrder(id);
	}
	
	public List<OrderDetail> findByProduct(Integer productID){
		return repos.findByProduct(productID);
	}
	
	public List<OrderDetail> findByCategory(Integer categoryID){
		return repos.findByCategory(categoryID);
	}
	
	public List<Object[]> revenueByCategory(){
		return repos.revenueByCategory();
	}
}
