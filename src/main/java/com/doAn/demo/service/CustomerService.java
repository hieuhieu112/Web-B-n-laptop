package com.doAn.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doAn.demo.entity.Customers;
import com.doAn.demo.responsitory.CustomerRepository;

@Service
public class CustomerService {
	@Autowired
	private CustomerRepository repos;
	
	public Customers save(Customers customer) {
		return repos.save(customer);
	}
	
	public List<Customers> findAll(){
		return repos.findAll();
	}
	
	public Optional<Customers> findById(String id) {
		return repos.findById(id);
	}
	
	public void deleteByID(String id) {
		repos.deleteById(id);
	}
}
