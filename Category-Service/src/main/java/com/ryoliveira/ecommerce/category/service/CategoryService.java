package com.ryoliveira.ecommerce.category.service;

import com.ryoliveira.ecommerce.category.model.Category;
import com.ryoliveira.ecommerce.category.model.CategoryList;

public interface CategoryService {
	
	CategoryList findAll();
	
	Category saveCategory(Category category);
	
	Category findById(int categoryId);
	
	void deleteCategory(int categoryId);
	
	Category updateCategory(Category updatedCategory);
	
	boolean isCategoryNamePresent(String categoryName);
	

}
