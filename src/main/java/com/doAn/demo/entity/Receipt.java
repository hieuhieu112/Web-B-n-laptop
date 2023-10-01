package com.doAn.demo.entity;

import java.time.LocalDate;
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
@Table(name = "receipt")
public class Receipt {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "mm/dd/yyyy")
	LocalDate dateimport;
	Float total;
	
//	String id_employees;
//	Integer id_producers;
	
	@ManyToOne
	@JoinColumn(name = "id_admin")
	Admin admin;
	
	@ManyToOne
	@JoinColumn(name = "id_employees")
	Employee employee;
	
	@ManyToOne
	@JoinColumn(name = "id_producers")
	Producer producer;
	
	@OneToMany(mappedBy = "receipt",fetch = FetchType.EAGER)
	List<ReceiptDetail> receiptDetail;
	
	public Receipt() {}


	public Receipt(Integer id, LocalDate dateimport, Float total, Employee employee, Producer producer) {
		super();
		this.id = id;
		this.dateimport = dateimport;
		this.total = total;
		this.employee = employee;
		this.producer = producer;
	}


	public Integer getId() {
		return id;
	}


	public List<ReceiptDetail> getReceiptDetail() {
		return receiptDetail;
	}


	public void setReceiptDetail(List<ReceiptDetail> receiptDetail) {
		this.receiptDetail = receiptDetail;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public LocalDate getDateimport() {
		return dateimport;
	}


	public void setDateimport(LocalDate dateimport) {
		this.dateimport = dateimport;
	}


	public Float getTotal() {
		return total;
	}


	public void setTotal(Float total) {
		this.total = total;
	}


	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	public Producer getProducer() {
		return producer;
	}
	public void setProducer(Producer producer) {
		this.producer = producer;
	}
	public Admin getAdmin() {
		return admin;
	}
	public void setAdmin(Admin admin) {
		this.admin = admin;
	}

	
}
