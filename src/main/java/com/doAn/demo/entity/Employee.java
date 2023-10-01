package com.doAn.demo.entity;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "employees")
public class Employee {

	@Id
	String id;
	String email;
	String pass;
	String nameemployee;
	Boolean gender;
	String photo;
	String address;
	String numberphone;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "mm/dd/yyyy")
	LocalDate birthday;
	Boolean access;
	Boolean activated;
//	String departmentid;
//	String employeetypeid;

	@ManyToOne
	@JoinColumn(name = "departmentid")
	Departments department;
	@ManyToOne
	@JoinColumn(name = "employeetypeid")
	EmployeeTypes empolyeetype;

	@OneToMany(mappedBy = "employee",fetch = FetchType.EAGER)
	List<Contracts> contract;
	
	@OneToMany(mappedBy = "employee",fetch = FetchType.EAGER)
	List<Receipt> receipt;

	public Employee() {
	}

	public Employee(String id, String email, String pass, String nameemployee, Boolean gender, String photo,
			String address, String numberphone, LocalDate birthday, Boolean access, Boolean activated, String departmentid,
			String employeetypeid) {
		super();
		this.id = id;
		this.email = email;
		this.pass = pass;
		this.nameemployee = nameemployee;
		this.gender = gender;
		this.photo = photo;
		this.address = address;
		this.numberphone = numberphone;
		this.birthday = birthday;
		this.access = access;
		this.activated = activated;

	}

	public Departments getDepartment() {
		return department;
	}

	public void setDepartment(Departments department) {
		this.department = department;
	}

	public EmployeeTypes getEmpolyeetype() {
		return empolyeetype;
	}

	public void setEmpolyeetype(EmployeeTypes empolyeetype) {
		this.empolyeetype = empolyeetype;
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

	public String getNameemployee() {
		return nameemployee;
	}

	public void setNameemployee(String nameemployee) {
		this.nameemployee = nameemployee;
	}

	public Boolean getGender() {
		return gender;
	}

	public void setGender(Boolean gender) {
		this.gender = gender;
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

	public Boolean getAccess() {
		return access;
	}

	public void setAccess(Boolean access) {
		this.access = access;
	}

	public Boolean getActivated() {
		return activated;
	}

	public void setActivated(Boolean activated) {
		this.activated = activated;
	}
	
	public boolean checkAge() {
		LocalDate n = LocalDate.now();
		if(n.getYear() - birthday.getYear() > 18) {
			return true;
		}
		if(n.getYear() - birthday.getYear() == 18) {
			if(n.getMonthValue() < birthday.getMonthValue()) {
				return false;
			}
			if(n.getMonthValue() == birthday.getMonthValue()) {
				if(n.getDayOfMonth() < birthday.getDayOfMonth()) {
					return false;
				}
				return true;
			}
			
			return true;
		}
		
		return false;
	}

	public Boolean checkEmptyField() {
		if (this.activated == null || this.access == null || this.address.equals("") || this.department == null
				|| this.email.equals("") || this.empolyeetype == null || this.gender == null || this.id.equals("")
				|| this.nameemployee.equals("") || this.numberphone.equals("") || this.pass.equals("") ||this.birthday == null) {
			return false;
		}
		return true;
	}
}
