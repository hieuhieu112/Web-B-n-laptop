package com.doAn.demo.entity;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "orders")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "MM/dd/yyyy")
	LocalDate orderdate;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "MM/dd/yyyy")
	LocalDate requiredate;
	Float amount = 0.0f;
	String receiver;
	String address;
	String description;
	Integer status;
	String numberphone;
//	String customerid;
	
	@ManyToOne
	@JoinColumn(name = "customerid")
	Customers customer;
	
	@OneToMany(mappedBy = "order",fetch = FetchType.EAGER)
	List<OrderDetail> orderdetail;
	
	public Order() {}
	public Order(Integer id, LocalDate orderdate, Float amount, String receiver, String address, String description,
			Integer status, String numberphone,LocalDate requiredate) {
		super();
		this.id = id;
		this.orderdate = orderdate;
		this.amount = amount;
		this.receiver = receiver;
		this.address = address;
		this.description = description;
		this.status = status;
		this.numberphone = numberphone;
		this.requiredate = requiredate;
	}
	
	
	public LocalDate getRequiredate() {
		return requiredate;
	}
	public void setRequiredate(LocalDate requiredate) {
		this.requiredate = requiredate;
	}
	
	public Customers getCustomer() {
		return customer;
	}
	public void setCustomer(Customers customer) {
		this.customer = customer;
	}
	public List<OrderDetail> getOrderdetail() {
		return orderdetail;
	}
	public void setOrderdetail(List<OrderDetail> orderdetail) {
		this.orderdetail = orderdetail;
	}
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	public LocalDate getOrderdate() {
		return orderdate;
	}
	public void setOrderdate(LocalDate orderdate) {
		this.orderdate = orderdate;
	}
	public Float getAmount() {
		return amount;
	}
	public void setAmount(Float amount) {
		this.amount = amount;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getNumberphone() {
		return numberphone;
	}
	public void setNumberphone(String numberphone) {
		this.numberphone = numberphone;
	}	
	
	
}
