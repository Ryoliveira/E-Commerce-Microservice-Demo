package com.ryoliveira.ecommerce.category.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ryoliveira.ecommerce.category.model.Category;
import com.ryoliveira.ecommerce.category.model.CategoryList;
import com.ryoliveira.ecommerce.category.service.CategoryService;

@RestController
public class CategoryController {
	
	@Autowired
	private CategoryService categoryService;
	
	@PostMapping(path = "/category")
	private Category saveCategory(@RequestBody Category category) {
		return categoryService.saveCategory(category);
	}
	
	@GetMapping(path = "/category/{id}")
	private Category getCategoryById(@PathVariable("id") int categoryId) {
		return categoryService.findById(categoryId);
	}
	
	
	@PutMapping(path = "/category")
	private Category updateCategory(@RequestBody Category updatedCategory) {
		return categoryService.updateCategory(updatedCategory);
	}
	
	@DeleteMapping(path = "/category/{id}")
	private void deleteCategory(@PathVariable("id") int categoryId) {
		categoryService.deleteCategory(categoryId);
	}
	
	@GetMapping(path = "/categories")
	private CategoryList getCategories() {
		return categoryService.findAll();
	}
	
	@GetMapping(path = "/category/name/{categoryName}")
	private boolean isCategoryNameInDatabase(@PathVariable("categoryName") String categoryName) {
		return categoryService.isCategoryNamePresent(categoryName);
	}
	
	
}
