package com.doAn.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doAn.demo.entity.Bonus;
import com.doAn.demo.responsitory.BonusRespository;

@Service
public class BonusService {

	
	@Autowired
	private BonusRespository repos;
	
	public List<Bonus> findAll(){
		return repos.findAll();
	}
	
	public Bonus save(Bonus bonus) {
		return repos.save(bonus);
	}
	
	public Bonus findByID(Integer id) {
		return repos.findById(id).get();
	}
	
	public void deleteByID(Integer id) {
		repos.deleteById(id);
	}
}
