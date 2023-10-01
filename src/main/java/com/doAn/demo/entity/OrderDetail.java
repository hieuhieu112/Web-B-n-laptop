package com.doAn.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity 
@Table(name =  "orderdetails")
public class OrderDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;
	
	Integer quantity;
	Integer unitprice;
	Float discount;
	
//	Integer ProductID;
//	Integer orderid;
	@ManyToOne
	@JoinColumn(name = "orderid")
	Order order;
	@ManyToOne
	@JoinColumn(name = "Productid")
	Product product;
	
	public OrderDetail() {}
	public OrderDetail(Integer id,  Integer quantity, Integer unitprice, Float discount) {
		super();
		this.id = id;		
		this.quantity = quantity;
		this.unitprice = unitprice;
		this.discount = discount;
	}
	
	
	
	
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Integer getUnitprice() {
		return unitprice;
	}
	public void setUnitprice(Integer unitprice) {
		this.unitprice = unitprice;
	}
	public Float getDiscount() {
		return discount;
	}
	public void setDiscount(Float discount) {
		this.discount = discount;
	}
	
	
}
