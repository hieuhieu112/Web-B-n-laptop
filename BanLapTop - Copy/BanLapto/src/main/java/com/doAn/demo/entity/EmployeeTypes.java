package com.doAn.demo.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name =  "loainhanvien")
public class EmployeeTypes {
	
	@Id
	String id;
	String nameemployeetype;
	
	@OneToMany(mappedBy = "empolyeetype",fetch = FetchType.EAGER)
	List<Employee> Employee;
	
	public EmployeeTypes() {}
	
	public EmployeeTypes(String id, String nameemployeetype) {
		super();
		this.id = id;
		this.nameemployeetype = nameemployeetype;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNameemployeetype() {
		return nameemployeetype;
	}

	public void setNameemployeetype(String nameemployeetype) {
		this.nameemployeetype = nameemployeetype;
	}
	
	public Boolean checkEmptyField() {
		
		if(this.id == null|| this.nameemployeetype.equals("")) {
			return false;
		}
		
		return true;
	}
}
