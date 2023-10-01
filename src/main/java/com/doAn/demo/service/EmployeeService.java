package com.doAn.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doAn.demo.entity.Employee;
import com.doAn.demo.responsitory.EmployeeRepository;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeRepository repos;
	
	public List<Employee> findAll(){
		return repos.findAll();
	}
	
	public Employee save(Employee employee) {
		return repos.save(employee);
	}
	
	public Optional<Employee> findByID(String id) {
		return repos.findById(id);
	}
	
	public void deleteById(String id) {
		repos.deleteById(id);
	}
	
	public List<Employee> findByDepartment(String departmentID){
		return repos.findByDepartment(departmentID);
	}
	
	public List<Employee> findByType(String TypeID){
		return repos.findByType(TypeID);
	}
}
