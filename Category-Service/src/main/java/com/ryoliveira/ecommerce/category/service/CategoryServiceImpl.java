package com.ryoliveira.ecommerce.category.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ryoliveira.ecommerce.category.dao.CategoryRepository;
import com.ryoliveira.ecommerce.category.model.Category;
import com.ryoliveira.ecommerce.category.model.CategoryList;

@Service
public class CategoryServiceImpl implements CategoryService {
	
	Logger LOGGER = LoggerFactory.getLogger(CategoryServiceImpl.class);
	
	@Autowired
	private CategoryRepository categoryRepo;

	@Override
	public CategoryList findAll() {
		List<Category> categoryList = categoryRepo.findAll();
		return new CategoryList(categoryList);
	}

	@Override
	public Category findById(int categoryId) {
		Category category = new Category();
		
		try {
			Optional<Category> optional = categoryRepo.findById(categoryId);
			category = optional.orElseThrow(() -> new NoSuchElementException());
		}catch(NoSuchElementException e) {
			LOGGER.warn("No Category with id: " + categoryId);
		}
		return category;
	}

	@Override
	public void deleteCategory(int categoryId) {
		try {
			Optional<Category> optional = categoryRepo.findById(categoryId);
			Category category = optional.orElseThrow(() -> new NoSuchElementException());
			categoryRepo.delete(category);
		}catch(NoSuchElementException e) {
			LOGGER.warn("No Category with id: " + categoryId);
		}
	}

	@Override
	public Category updateCategory(Category updatedCategory) {
		Category category = new Category();
		
		try {
			Optional<Category> optional = categoryRepo.findById(updatedCategory.getCategoryId());
			category = optional.orElseThrow(() -> new NoSuchElementException());
			if(updatedCategory.getName() != null) {
				category.setName(updatedCategory.getName());
			}
			categoryRepo.save(category);
		}catch(NoSuchElementException e) {
			LOGGER.warn("No Category with id: " + updatedCategory.getCategoryId());
		}
		return category;
	}

	@Override
	public Category saveCategory(Category category) {
		return categoryRepo.save(category);
	}

}
