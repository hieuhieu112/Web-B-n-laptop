package com.doAn.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doAn.demo.entity.ReceiptDetail;
import com.doAn.demo.responsitory.ReceiptDetailRespository;

@Service
public class ReceiptDetailService {

	@Autowired
	private ReceiptDetailRespository repos;
	
	public List<ReceiptDetail> findAll() {
		return repos.findAll();
	}
	
	public Optional<ReceiptDetail> findByID(Integer id) {
		return repos.findById(id);
	}
	
	public ReceiptDetail save(ReceiptDetail receiptDetail) {
		return repos.save(receiptDetail);
	}
	
	public List<ReceiptDetail> findByReceipt(Integer id){
		return repos.findByReceipt(id);
	}
	
	public List<ReceiptDetail> findByProduct(Integer id){
		return repos.findByProduct(id);
	}
}
