package com.ryoliveira.ecommerce.productservice.dao;

import com.ryoliveira.ecommerce.productservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
	
	List<Product> findAllByCategoryId(int categoryId);

	List<Product> findAllByNameContaining(String name);

}
