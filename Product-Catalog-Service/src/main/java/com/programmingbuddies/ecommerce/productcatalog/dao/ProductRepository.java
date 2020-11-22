package com.programmingbuddies.ecommerce.productcatalog.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.programmingbuddies.ecommerce.productcatalog.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

}
