package com.doAn.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doAn.demo.entity.Admin;
import com.doAn.demo.responsitory.AdminRepository;

@Service
public class AdminService {
	@Autowired
	AdminRepository repos;
	
	public List<Admin> getAll(){
		return repos.findAll();
	}
	
	public Admin save(Admin admin) {
		return repos.save(admin);
	}
	
	public Optional<Admin> getByUsername(String username) {
		return repos.findById(username);
	}
	
	public void deleteByID(String username) {
		repos.deleteById(username);
		return; 
	}
}
