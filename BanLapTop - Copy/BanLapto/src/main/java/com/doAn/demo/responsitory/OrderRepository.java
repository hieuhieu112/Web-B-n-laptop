package com.doAn.demo.responsitory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.doAn.demo.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {
	
	//select pat.tapa from User us join us.priceAlertsTapas pat
	@Query(value = "SELECT o from Order o WHERE o.customer.id = :id and o.type = 1 ORDER BY o.orderdate DESC")
	List<Order> findOrderByCustomer(@Param("id")String customerID);
	
	@Query(value = "SELECT o from Order o WHERE o.customer.id = :id and o.type = 0 ORDER BY o.orderdate DESC")
	Order findCartByCustomer(@Param("id")String customerID);
	
	@Query(value = "SELECT o from Order o WHERE o.address LIKE :keyword OR o.numberphone LIKE :keyword OR o.receiver LIKE :keyword OR o.customer.id LIKE :keyword OR o.customer.fullname LIKE :keyword and o.type = 1")
	List<Order> findByKeyWord(@Param("keyword") String keyword);
	
	@Query(value = "SELECT o from Order o WHERE o.type = :id ORDER BY o.orderdate DESC")
	List<Order> findByType(@Param("id")Integer type );
	 
	 @Query(value = "SELECT o from Order o WHERE MONTH(orderdate) = :thang  and   Year(orderdate) = :nam and status = 3 and o.type = 1 ORDER BY o.orderdate DESC")
	List<Order> findByMonthYear(@Param("thang")Integer month, @Param("nam") Integer year );
	 
	 @Query(value = "SELECT  sum(unitprice * quantity * (1-discount))  AS Amount FROM   OrderDetail WHERE   order.id = :id")
	 Float findAmountByID(@Param("id") Integer IDOrder );
	 
	
}
