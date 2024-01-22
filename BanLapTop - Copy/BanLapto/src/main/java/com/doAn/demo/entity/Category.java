package com.doAn.demo.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "theloai")
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(length = 45)
	private String namecategory;
	@Column(length = 45)
	private String namecategoryen;
	private String photo;
	
	@OneToMany(mappedBy = "category",fetch = FetchType.EAGER)
	List<Product> product;
	
	public Category() {}	

	public Category(Integer id, String namecategory, String namecategoryen,String image) {
		this.id = id;
		this.namecategory = namecategory;
		this.namecategoryen = namecategoryen;
		this.setPhoto(image);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNamecategory() {
		return namecategory;
	}

	public void setNamecategory(String namecategory) {
		this.namecategory = namecategory;
	}

	public String getNamecategoryen() {
		return namecategoryen;
	}

	public void setNamecategoryen(String namecategoryen) {
		this.namecategoryen = namecategoryen;
	}
	

	public List<Product> getProduct() {
		return product;
	}

	public void setProduct(List<Product> product) {
		this.product = product;
	}
	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}
	
	public Boolean checkEmptyField() {
		if(this.namecategory.equals("") || this.namecategoryen.equals("")) {
			return false;
		}
		return true;
	}
}
