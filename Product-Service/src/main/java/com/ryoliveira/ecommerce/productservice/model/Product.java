package com.ryoliveira.ecommerce.productservice.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="products")
public class Product {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@Column(name="name")
	private String name;
	@Column(name="description")
	private String description;
	@Column(name="category_id")
	private int categoryId;
	@Column(name="img")
	private String img;
	@Column(name="price")
	private Float price;
	

}
