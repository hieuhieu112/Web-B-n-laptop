package com.doAn.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doAn.demo.entity.Order;
import com.doAn.demo.entity.Receipt;
import com.doAn.demo.entity.ReceiptDetail;
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
	
	public List<ReceiptDetail> findByEmployee(String id) {
		return repos.findByEmployee(id);
	}
	
	public Float getTotalByMonth(Integer month, Integer year) {
		List<Receipt> list = repos.findByMonthYear(month, year);
		Float total = 0.0f;
		for(int i =0; i< list.size(); i++) {
			total += repos.getTotal(list.get(i).getId());
		}
		return total;
	}
	
	public List<Receipt> findByMonthYear(Integer month, Integer year){
		return repos.findByMonthYear(month, year);
	}
	
	public List<Receipt> findByKeyword(String keyword){
		return repos.findByKeyWord(keyword);
	}
	
	public Float getAmount(Integer id) {
		return repos.getTotal(id);
	}
}
