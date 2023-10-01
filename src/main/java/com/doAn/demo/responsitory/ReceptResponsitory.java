package com.doAn.demo.responsitory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.doAn.demo.entity.Receipt;
import com.doAn.demo.entity.ReceiptDetail;

public interface ReceptResponsitory extends JpaRepository<Receipt, Integer>{
	@Query(value = "SELECT r from Receipt r WHERE r.employee.id = :id")
	List<ReceiptDetail> findByEmployee(@Param("id")String employeeid);
	
	@Query(value = "SELECT r from Receipt r WHERE r.producer.id = :id")
	List<ReceiptDetail> findByProducer(@Param("id")Integer producerid);
	
	@Query(value = "SELECT r from Receipt r WHERE MONTH(dateimport) = :month AND Year(dateimport) = :year ORDER BY r.dateimport DESC")
	List<Receipt> findByMonthYear(@Param("month")Integer month, @Param("year") Integer year);
	
	@Query(value = "call shoplaptop.totalReceipt(:thang, :nam);", nativeQuery = true)
	Float getTotalByMonth(@Param("thang") Integer month,@Param("nam") Integer year);
}
