package com.ryoliveira.ecommerce.category.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ryoliveira.ecommerce.category.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
	
	List<Category> findAllByOrderByName();
	
	Optional<String> findByNameIgnoreCase(String name);

}
