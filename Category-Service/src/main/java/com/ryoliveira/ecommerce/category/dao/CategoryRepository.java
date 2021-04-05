package com.ryoliveira.ecommerce.category.dao;

import com.ryoliveira.ecommerce.category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    List<Category> findAllByOrderByName();

    Optional<String> findByNameIgnoreCase(String name);

}
