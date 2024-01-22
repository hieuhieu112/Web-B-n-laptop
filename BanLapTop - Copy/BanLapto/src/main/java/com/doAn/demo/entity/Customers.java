package com.doAn.demo.entity;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "khachhang")
public class Customers {
	
	@Id
	
	String id;
	String email;
	String pass;
	String fullname;
	String photo;
	String address;
	String numberphone;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "MM/dd/yyyy")
	LocalDate birthday;
	Boolean gender;
	Boolean activated;
	
	@OneToMany(mappedBy = "customer",fetch = FetchType.LAZY)
	List<Order> order;
	
	
	public Customers() {}
	public Customers(String id,String email, String pass, String fullname, String photo, String address,
			String numberphone, LocalDate birthday, Boolean gender, Boolean activated) {
		
		this.id = id;
		this.email = email;
		this.pass = pass;
		this.fullname = fullname;
		this.photo = photo;
		this.address = address;
		this.numberphone = numberphone;
		this.birthday = birthday;
		this.gender = gender;
		this.activated = activated;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getNumberphone() {
		return numberphone;
	}
	public void setNumberphone(String numberphone) {
		this.numberphone = numberphone;
	}
	public LocalDate getBirthday() {
		return birthday;
	}
	public void setBirthday(LocalDate birthday) {
		this.birthday = birthday;
	}
	public Boolean getGender() {
		return gender;
	}
	public void setGender(Boolean gender) {
		this.gender = gender;
	}
	public Boolean getActivated() {
		return activated;
	}
	public void setActivated(Boolean activated) {
		this.activated = activated;
	}
	
	
	
	public List<Order> getOrder() {
		return order;
	}
	public void setOrder(List<Order> order) {
		this.order = order;
	}
	public Boolean checkEmptyField() {
		System.out.println(this.id.equals("")|| this.numberphone.equals("")|| this.pass.equals("")|| this.getBirthday() == null);
		if(this.activated == null || this.gender == null||this.address.equals("")|| this.email.equals("")|| this.fullname.equals("")|| this.id.equals("")|| this.numberphone.equals("")|| this.pass.equals("")|| this.getBirthday() == null) {
			return true;
		}
		return false;
	}
	
	public Boolean checkRe() {
		
		if(this.gender == null||this.address.equals("")|| this.email.equals("")|| this.fullname.equals("")||  this.numberphone.equals("")|| this.pass.equals("")|| this.getBirthday() == null) {
			return true;
		}
		
		return false;
	}
	
	public Boolean checkDate() {
		if(!this.birthday.isBefore(LocalDate.now())) {
			return true;
		}
		
		return false;
	}
	
	public Boolean checkPhone() {
		if(numberphone.length() != 10) {
			return false;
		}
		
		if (numberphone.matches(".*[a-z].*")) { 
		    return false;
		}
		
		if(!numberphone.startsWith("0")) {
			return false;
		}
		
		return true;
	}
}
