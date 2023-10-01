package com.doAn.demo.entity;

import java.time.LocalDate;
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
@Table(name = "contracts")
public class Contracts {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;
	String namecontract;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "MM/dd/yyyy")
	LocalDate starttime;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "MM/dd/yyyy")
	LocalDate endtime;
	Integer subsidize;
	Float insurrance;
	Float salary;
	Integer status;
	
//	String employeeid;
	@ManyToOne
	@JoinColumn(name = "employeeid")
	Employee employee;

	public Contracts() {
	}

	public Contracts(Integer id, String namecontract, LocalDate starttime, LocalDate endtime, Integer subsidize, Float insurrance,
			Float salary, Integer status) {
		super();
		this.id = id;
		this.namecontract = namecontract;
		this.starttime = starttime;
		this.endtime = endtime;
		this.subsidize = subsidize;
		this.insurrance = insurrance;
		this.salary = salary;
		this.status = status;
	}
	

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNamecontract() {
		return namecontract;
	}

	public void setNamecontract(String namecontract) {
		this.namecontract = namecontract;
	}

	public LocalDate getStarttime() {
		return starttime;
	}

	public void setStarttime(LocalDate starttime) {
		this.starttime = starttime;
	}

	public LocalDate getEndtime() {
		return endtime;
	}

	public void setEndtime(LocalDate endtime) {
		this.endtime = endtime;
	}

	public Integer getSubsidize() {
		return subsidize;
	}

	public void setSubsidize(Integer subsidize) {
		this.subsidize = subsidize;
	}

	public Float getInsurrance() {
		return insurrance;
	}

	public void setInsurrance(Float insurrance) {
		this.insurrance = insurrance;
	}

	public Float getSalary() {
		return salary;
	}

	public void setSalary(Float salary) {
		this.salary = salary;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Boolean checkEmptyField() {
		if (this.insurrance == null || this.namecontract == null
				|| this.namecontract.equals("") || this.status == null || this.subsidize == null || this.endtime == null|| this.starttime == null) {
			return true;
		}

		return false;
	}
	
	public Boolean checkTime() {
		LocalDate now = LocalDate.now();
		if(starttime.isBefore(now)) {
			return false;
		}
		if(starttime.isAfter(endtime)) {
			return false;
		}
		return true;
	}
}
