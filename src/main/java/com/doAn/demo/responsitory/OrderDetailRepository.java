package com.doAn.demo.responsitory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.doAn.demo.entity.OrderDetail;
import com.doAn.demo.entity.Product;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer>{
	@Query(value = "SELECT o from OrderDetail o WHERE o.order.id = :id")
	List<OrderDetail> findByOrder(@Param("id")Integer orderID );
	
	@Query(value = "SELECT o from OrderDetail o WHERE o.product.id = :id")
	List<OrderDetail> findByProduct(@Param("id")Integer productID);
	
	@Query(value = "SELECT o from OrderDetail o WHERE o.product.category.id = :id")
	List<OrderDetail> findByCategory(@Param("id")Integer categoryID );
	
	@Query(value = "SELECT d.product.category.id ,"
			+ "SUM(d.quantity),"
			+ "SUM(d.quantity * (1- d.discount) * d.unitprice)"
			+ "FROM OrderDetail d GROUP BY d.product.category.id")
	List<Object[]> revenueByCategory();
}
