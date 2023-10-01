package com.doAn.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="receiptdetail")
public class ReceiptDetail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;
	Integer quantity;
	Integer unitprice;
	
//	Integer ID_Products;
	@ManyToOne
	@JoinColumn(name = "id_products")
	Product product;
//	String ID_Recept;
	@ManyToOne
	@JoinColumn(name = "id_recept")
	Receipt receipt;
	
	
	
	public ReceiptDetail() {}
	
	public ReceiptDetail(Integer id, Integer quantity, Integer unitPrice, Product product, Receipt receipt) {
		super();
		this.id = id;
		this.quantity = quantity;
		this.unitprice = unitPrice;
		this.product = product;
		this.receipt = receipt;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Integer getUnitprice() {
		return unitprice;
	}
	public void setUnitprice(Integer unitPrice) {
		this.unitprice = unitPrice;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public Receipt getReceipt() {
		return receipt;
	}
	public void setReceipt(Receipt receipt) {
		this.receipt = receipt;
	}
	
	
}
