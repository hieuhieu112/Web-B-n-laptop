package com.doAn.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doAn.demo.entity.EmployeeTypes;
import com.doAn.demo.responsitory.EmployeeTypeRepository;

@Service
public class EmployeeTypeService {

	
	@Autowired
	private EmployeeTypeRepository repos;
	
	public List<EmployeeTypes> findAll(){
		return repos.findAll();
	}
	
	public void deleteById(String id) {
		repos.deleteById(id);
	}
	
	public EmployeeTypes findByID(String id) {
		return repos.findById(id).get();
	}
	
	public EmployeeTypes save(EmployeeTypes employeeType) {
		return repos.save(employeeType);
	}
}
