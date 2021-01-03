package com.ryoliveira.ecommerce.productservice.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ryoliveira.ecommerce.productservice.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
	
	List<Product> findAllByCategoryId(int categoryId);

}
