package com.doAn.demo.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "admin")
public class Admin {

	@Id
	String username;
	String pass;
	String fullname;
	Integer access;
	String email;
	
	@OneToMany(mappedBy = "admin", fetch = FetchType.EAGER)
	List<Bonus> Bonus;
	@OneToMany(mappedBy = "admin", fetch = FetchType.EAGER)
	List<Receipt> receipt;
	
	public Admin() {}
	public Admin(String username, String pass, String fullname, Integer access,String Email) {
		super();
		this.username = username;
		this.pass = pass;
		this.fullname = fullname;
		this.access = access;
		this.email = Email;
	}
	
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public Integer getAccess() {
		return access;
	}
	public void setAccess(Integer access) {
		this.access = access;
	}
	public List<Bonus> getBonus() {
		return Bonus;
	}
	public void setBonus(List<Bonus> bonus) {
		Bonus = bonus;
	}
	public List<Receipt> getReceipt() {
		return receipt;
	}
	public void setReceipt(List<Receipt> receipt) {
		this.receipt = receipt;
	}
	
}
