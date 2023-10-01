package com.doAn.demo.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "bonus")
public class Bonus {
	
	@Id  @GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;
	
	
	Float amount;
	String reason;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "MM/dd/yyyy")
	LocalDate datewrite;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "MM/dd/yyyy")
	LocalDate datestart;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "MM/dd/yyyy")
	LocalDate dateend;
//	String employeeid;
	
	@ManyToOne
	@JoinColumn(name = "AdminID")
	Admin admin;


	public Bonus() {}
	
	public Bonus(Integer id,  Boolean type, Float amount, String reason, LocalDate datewrite,LocalDate datestart,Admin admin,	LocalDate dateend) {
		super();
		this.id = id;
		
		this.amount = amount;
		this.reason = reason;
		this.datewrite = datewrite;
		this.datestart=datestart;
		this.dateend = dateend;
		this.admin = admin;
	}
	
	
	public LocalDate getDatestart() {
		return datestart;
	}

	public void setDatestart(LocalDate datestart) {
		this.datestart = datestart;
	}

	public LocalDate getDateend() {
		return dateend;
	}

	public void setDateend(LocalDate dateend) {
		this.dateend = dateend;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Float getAmount() {
		return amount;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public LocalDate getDatewrite() {
		return datewrite;
	}

	public void setDatewrite(LocalDate datewrite) {
		this.datewrite = datewrite;
	}
	public Admin getAdmin() {
		return admin;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}

	public Boolean checkEmptyField() {
		if(this.amount == null || this.amount == 0.0f || this.admin == null|| this.reason.equals("")) {
			return false;
		}
		return true;
	}
}
