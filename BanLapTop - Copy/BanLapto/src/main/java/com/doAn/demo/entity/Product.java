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
@Table(name = "sanpham")
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;
	String nameproduct;
	String photo;
	Integer quantity = 0;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "MM/dd/yyyy")
	LocalDate productdate;
	Integer unitprice;
	Float discount =0.0f;
	String description;
	Integer views = 0;
	Boolean status;

//	Integer categoryid;
	@ManyToOne
	@JoinColumn(name = "categoryid")
	Category category;
//	Integer producerid;
	@ManyToOne
	@JoinColumn(name = "producerid")
	Producer producer;

	@OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
	List<OrderDetail> orderDetail;
	
	@OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
	List<ReceiptDetail> receiptDetail;

	public Product() {
	}

	public Product( String nameproduct, String photo, Integer quantity, LocalDate productdate, 
			Integer unitprice, Float discount, String description, Integer views,  Boolean status) {
		super();
		this.nameproduct = nameproduct;
		this.photo = photo;
		this.quantity = quantity;
		this.productdate = productdate;
		this.unitprice = unitprice;
		this.discount = discount;
		this.description = description;
		this.views = views;
		
		this.status = status;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNameproduct() {
		return nameproduct;
	}

	public void setNameproduct(String nameproduct) {
		this.nameproduct = nameproduct;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public LocalDate getProductdate() {
		return productdate;
	}

	public void setProductdate(LocalDate productdate) {
		this.productdate = productdate;
	}


	public Integer getUnitprice() {
		return unitprice;
	}

	public void setUnitprice(Integer unitprice) {
		this.unitprice = unitprice;
	}

	public Float getDiscount() {
		return discount;
	}

	public void setDiscount(Float discount) {
		this.discount = discount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getViews() {
		return views;
	}

	public void setViews(Integer views) {
		this.views = views;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Producer getProducer() {
		return producer;
	}

	public void setProducer(Producer producer) {
		this.producer = producer;
	}

	public List<OrderDetail> getOrderDetail() {
		return orderDetail;
	}

	public void addOrderDetail(OrderDetail orderDetail) {
		this.orderDetail.add(orderDetail);
	}

	public Boolean checkEmptyField() {
		if (this.status == null
				|| this.category == null || this.discount == null
				|| this.nameproduct.equals("") || this.producer == null || this.quantity == null
				||  this.unitprice == null || this.unitprice == 0.0f
				|| this.views == null || this.productdate == null) {
			return true;
		}

		return false;
	}
	
	public Product clone() {
		return new Product(nameproduct,photo,quantity,productdate,unitprice, discount,  description,  views,   status);
	}
}
