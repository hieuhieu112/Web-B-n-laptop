package com.doAn.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doAn.demo.entity.Producer;
import com.doAn.demo.responsitory.ProducerRepository;

@Service
public class ProducerService {

	
	@Autowired 
	private ProducerRepository repos;
	
	public List<Producer> findAll(){
		return repos.findAll();
	}
	
	public Producer findByID(Integer id) {
		return repos.findById(id).get();
	}
	
	public void deleteById(Integer id) {
		repos.deleteById(id);
	}
	
	public Producer save(Producer producer) {
		return repos.save(producer);
	}
}
