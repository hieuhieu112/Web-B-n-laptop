package com.doAn.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doAn.demo.entity.Role;
import com.doAn.demo.responsitory.RoleResponsitory;

@Service
public class RoleServices {
	@Autowired
	RoleResponsitory repos;
	
	public Role save(Role role) {
		return repos.save(role);
	}
	
	public Optional<Role> findByID(Integer id) {
		return repos.findById(id);
	}
	
	public List<Role> findAll(){
		return repos.findAll();
	}
	
	public void deleteByID(Integer id) {
		repos.deleteById(id);
	}
}
