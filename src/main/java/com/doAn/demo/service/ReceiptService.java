package com.doAn.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doAn.demo.entity.Receipt;
import com.doAn.demo.responsitory.ReceptResponsitory;

@Service
public class ReceiptService {

	@Autowired
	private ReceptResponsitory repos;
	
	public Receipt save(Receipt receipt) {
		return repos.save(receipt);
	}
	
	public Optional<Receipt> findByID(Integer id) {
		return repos.findById(id);
	}
	
	public List<Receipt> findAll(){
		return repos.findAll();
	}
	
	public void deleteByID(Integer id) {
		repos.deleteById(id);
	}
	
	public Float getTotalByMonth(Integer month, Integer year) {
		return repos.getTotalByMonth(month, year);
	}
	
	public List<Receipt> findByMonthYear(Integer month, Integer year){
		return repos.findByMonthYear(month, year);
	}
}
