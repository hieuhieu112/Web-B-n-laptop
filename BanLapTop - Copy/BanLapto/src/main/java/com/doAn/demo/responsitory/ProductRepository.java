package com.doAn.demo.responsitory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.doAn.demo.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer>{
	
	@Query(value = "SELECT p from Product p WHERE p.category.id = :id")
	List<Product> findByProduct(@Param("id")Integer categoryid);
	
	@Query(value = "SELECT p from Product p WHERE p.nameproduct LIKE :keyword OR p.category.namecategory LIKE :keyword OR p.category.namecategoryen LIKE :keyword")
	List<Product> findByKeyWord(@Param("keyword") String keyword);
	
	@Query(value = "SELECT p from Product p WHERE p.producer.id = :id")
	List<Product> findByProducer(@Param("id")Integer producerid);
	
	
	@Query(value = "SELECT p from Product p WHERE p.views > 0 ORDER BY p.views DESC")
	List<Product> findByView();
	
	@Query(value = "SELECT p from Product p ORDER BY p.productdate DESC")
	List<Product> findByDate();
	
	@Query(value = "SELECT p from Product p  WHERE p.discount > 0 ORDER BY p.discount DESC")
	List<Product> findByDiscount();
	
	@Query(value = "SELECT p from Product p  ORDER BY SIZE(p.orderDetail) DESC")
	List<Product> findByBestSell();
	
	@Query(value = "SELECT d.product.nameproduct ,"
			+ "SUM(d.quantity) as quan,"
			+ "SUM(d.quantity * (1- d.discount) * d.unitprice)"
			+ "FROM OrderDetail d GROUP BY d.product.id "
			+ "ORDER BY SUM(d.quantity) DESC")
	List<Object[]> revenueByProduct();
}
