package com.ryoliveira.ecommerce.productservice.service;

import java.util.List;

import com.ryoliveira.ecommerce.productservice.model.Product;

public interface ProductService {
	
	Product saveProduct(Product product);
	
	Product updateProduct(Product updatedProduct);
	
	void removeProduct(int prodId); //TODO determine if removing by id is better than by object reference
	
	Product findProductById(int prodId);	
	
	List<Product> findAll();
	
	List<Product> findAllByCategoryId(int categoryId);
	
}
