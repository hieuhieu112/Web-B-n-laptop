package com.doAn.demo.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "producers")
public class Producer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;
	String nameproducer;
	String logo;
	String address;
	String email;
	String numberphone;

	@OneToMany(mappedBy = "producer", fetch = FetchType.EAGER)
	List<Product> Product;
	
	@OneToMany(mappedBy = "producer",fetch = FetchType.EAGER)
	List<Receipt> receipt;

	public Producer() {
	}

	public Producer(Integer id, String nameproducer, String logo, String address, String email, String numberphone) {
		super();
		this.id = id;
		this.nameproducer = nameproducer;
		this.logo = logo;
		this.address = address;
		this.email = email;
		this.numberphone = numberphone;
	}

	public List<Receipt> getReceipt() {
		return receipt;
	}

	public void setReceipt(List<Receipt> receipt) {
		this.receipt = receipt;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNameproducer() {
		return nameproducer;
	}

	public void setNameproducer(String nameproducer) {
		this.nameproducer = nameproducer;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNumberphone() {
		return numberphone;
	}

	public void setNumberphone(String numberphone) {
		this.numberphone = numberphone;
	}

	public Boolean checkEmptyField() {
		if (this.address == null || this.address.equals("") || this.email == null || this.email.equals("")
				|| this.nameproducer == null || this.nameproducer.equals("") || this.numberphone == null
				|| this.numberphone.equals("")) {
			return true;
		}

		return false;
	}

	public List<Product> getProduct() {
		return Product;
	}

	public void setProduct(List<Product> product) {
		Product = product;
	}
}
