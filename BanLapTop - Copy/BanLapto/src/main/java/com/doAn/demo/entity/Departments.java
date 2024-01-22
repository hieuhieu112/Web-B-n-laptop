package com.doAn.demo.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "phongban")
public class Departments {
	@Id
	String id;
	String namedepartment;
	
	@OneToMany(mappedBy = "department",fetch = FetchType.EAGER)
	List<Employee> employee;
	
	public Departments() {}
	
	public Departments(String id, String namedepartment) {
		super();
		this.id = id;
		this.namedepartment = namedepartment;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNamedepartment() {
		return namedepartment;
	}

	public void setNamedepartment(String namedepartment) {
		this.namedepartment = namedepartment;
	}	
	
	public Boolean checkEmptyField() {
		
		if(this.id == null|| this.id.equals("")|| this.namedepartment == null|| this.namedepartment.contentEquals("")) {
			return true;
		}
		
		return false;
	}
}
