package com.doAn.demo.responsitory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import com.doAn.demo.entity.Order;
import com.doAn.demo.entity.Product;

public interface OrderRepository extends JpaRepository<Order, Integer> {
	@Query(value = "SELECT o from Order o WHERE o.customer.id = :id ORDER BY o.orderdate DESC")
	List<Order> findByCustomer(@Param("id")String customerID );
	
	@Query(value = "SELECT o from Order o WHERE o.address LIKE :keyword OR o.numberphone LIKE :keyword OR o.receiver LIKE :keyword OR o.customer.id LIKE :keyword OR o.customer.fullname LIKE :keyword")
	List<Order> findByKeyWord(@Param("keyword") String keyword);
	
	 @Query(value = "call shoplaptop.totalOrder(:thang, :nam);", nativeQuery = true)
	 Float getTotalByMonth(@Param("thang") Integer month,@Param("nam") Integer year);
	 
	 @Query(value = "SELECT o from Order o WHERE MONTH(orderdate) = :thang  and   Year(orderdate) = :nam and status = 3 ORDER BY o.orderdate DESC")
	List<Order> findByMonthYear(@Param("thang")Integer month, @Param("nam") Integer year );
}
