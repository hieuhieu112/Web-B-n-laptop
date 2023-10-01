package com.doAn.demo.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import com.doAn.demo.entity.Product;

@SessionScope
@Service
public class ReceiptTemp {
	@Autowired 
	ProductService productService;
	
	Map<Integer, Product> list = new HashMap<>();
	
	public void add(Integer id) {
		Integer max = productService.findByID(id).getQuantity();
		if(max != 0) {
			Product p = list.get(id);
			System.out.println(id);
			if(p == null) {
				p = productService.findByID(id);
				p.setQuantity(1);
				list.put(id, p);
			}
			else {
				if(p.getQuantity()+1 <= max) 	p.setQuantity(p.getQuantity()+1);
			}
		}
	}
	
	public void minus(Integer id) {
		Product p = list.get(id);
		if(p!= null) {
			if(p.getQuantity() == 1) this.remove(id);
			else {
				this.update(id, p.getQuantity() -1);
			}
		}
	}
	
	public void remove(Integer id) {
		list.remove(id);
	}
	
	public void update(Integer id, int quantity) {
		Product p = list.get(id);
		p.setQuantity(quantity);
		
	}
	public int getQuantity(Integer id) {
		Product p = list.get(id);
		if (p==null) return 0;
		return p.getQuantity();
	}
	
	public void addNum(Integer id,Integer num) {
		Product p = list.get(id);
		if (p==null) {
			p = productService.findByID(id);
			p.setQuantity(num);
			list.put(id, p);
		}
		else {
			p.setQuantity(p.getQuantity() + num);
		}
	}
	
	public void clear() {
		list.clear();
	}
	
	public int getCount() {
		int count = 0;
		Collection<Product> ps = this.getAllItems();
		for(Product p: ps) {
			count+= p.getQuantity();
		}
		
		return count;		
	}
	
	public Float getAmount() {
		Float amount = 0.0f;
		Collection<Product> ps = this.getAllItems();
		for(Product p: ps) {
			amount += p.getQuantity() *p.getUnitprice()* (1-p.getDiscount());
		}
		
		return amount;	
	}
	public Collection<Product> getAllItems() {
		return  list.values();
	}
}
