package com.doAn.demo.responsitory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.doAn.demo.entity.ReceiptDetail;

public interface ReceiptDetailRespository extends JpaRepository<ReceiptDetail, Integer>{
	@Query(value = "SELECT r from ReceiptDetail r WHERE r.receipt.id = :id")
	List<ReceiptDetail> findByReceipt(@Param("id")Integer receiptid);
	
	@Query(value = "SELECT r from ReceiptDetail r WHERE r.product.id = :id")
	List<ReceiptDetail> findByProduct(@Param("id")Integer productID);
}
