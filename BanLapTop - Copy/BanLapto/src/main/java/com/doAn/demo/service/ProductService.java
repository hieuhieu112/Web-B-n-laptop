package com.doAn.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.doAn.demo.entity.Product;
import com.doAn.demo.responsitory.ProductRepository;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repos;
	
	public List<Product> findByCategory(Integer categoryID) {		
		return repos.findByProduct(categoryID);
	}
	
	public List<Product> findByKeyword(String keyword){
		return repos.findByKeyWord(keyword);
	}
	
		
	public List<Product> getMostInterest(){
		return repos.findByView();
	}
	public List<Product> getNewest(){
		return repos.findByDate();
	}
	public List<Product> getMostDiscount(){
		return repos.findByDiscount();
	}
	public List<Product> getBestSell(){
		return repos.findByBestSell();
	}
	public List<Product> findByProducer(Integer producerid){
		return repos.findByProducer(producerid);
	}
	
	
	public Product findByID(Integer id) {
		return repos.findById(id).get();
	}
	public List<Product> findAll(){
		return repos.findAll();
	}
	public void save(Product p) {
		repos.save(p);
	}
	
	public void deleteById(Integer id) {
		repos.deleteById(id);
	}
	
	public List<Object[]> revenueByProduct(){
		return repos.revenueByProduct();
	}
}
