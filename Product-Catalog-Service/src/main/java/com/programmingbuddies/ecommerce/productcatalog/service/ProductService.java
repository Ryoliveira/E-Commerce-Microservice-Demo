package com.programmingbuddies.ecommerce.productcatalog.service;

import java.util.List;

import com.programmingbuddies.ecommerce.productcatalog.model.Product;

public interface ProductService {
	
	Product saveProduct(Product product);
	
	Product updateProduct(Product updatedProduct);
	
	void removeProduct(int prodId); //TODO determine if removing by id is better than by object reference
	
	Product findProductById(int prodId);
	
	List<Product> findAll();
	
}
