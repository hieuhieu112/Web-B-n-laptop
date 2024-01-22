package com.doAn.demo.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doAn.demo.entity.Contracts;
import com.doAn.demo.responsitory.ContractRepository;

@Service
public class ContractService {

	@Autowired
	private ContractRepository repos;
	
	public Optional<Contracts> findByID(Integer id) {
		return repos.findById(id);		
	}
	
	public List<Contracts> findAll(){
		return repos.findAll();
	}
	
	public void deleteByID(Integer id){
		repos.deleteById(id);
	}
	
	public Contracts save(Contracts contract) {
		return repos.save(contract);
	}
	public List<Contracts> findByEmployeeID(String employeeid){
		return repos.findByEmployee(employeeid);
	}
	
	public List<Contracts> findByKeyWord(String keyword){
		return repos.findByKeyWord(keyword);
	}
}
