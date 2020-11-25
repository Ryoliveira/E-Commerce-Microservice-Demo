package com.ryoliveira.ecommerce.productservice.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ryoliveira.ecommerce.productservice.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

}
