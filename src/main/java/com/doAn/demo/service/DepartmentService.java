package com.doAn.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doAn.demo.entity.Departments;
import com.doAn.demo.responsitory.DepartmentRepository;

@Service
public class DepartmentService {

	@Autowired
	private DepartmentRepository repos;
	
	public List<Departments> findAll(){
		return repos.findAll();
	}
	
	public Departments findByID(String id) {
		return repos.findById(id).get();
	}
	
	public void deleteByID (String id) {
		repos.deleteById(id);
	}
	
	public Departments save(Departments de) {
		return repos.save(de);
	}
}
