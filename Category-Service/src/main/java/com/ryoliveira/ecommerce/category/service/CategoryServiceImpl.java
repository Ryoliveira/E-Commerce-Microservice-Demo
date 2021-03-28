package com.ryoliveira.ecommerce.category.service;

import com.ryoliveira.ecommerce.category.dao.CategoryRepository;
import com.ryoliveira.ecommerce.category.exception.CategoryNotFoundException;
import com.ryoliveira.ecommerce.category.model.Category;
import com.ryoliveira.ecommerce.category.model.CategoryList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepo;

    @Override
    public CategoryList findAll() {
        List<Category> categoryList = categoryRepo.findAllByOrderByName();
        return new CategoryList(categoryList);
    }

    @Override
    public Category findById(int categoryId) {
        Optional<Category> optional = categoryRepo.findById(categoryId);
        return optional.orElseThrow(() -> new CategoryNotFoundException("No Category with id: " + categoryId));
    }

    @Override
    public void deleteCategory(int categoryId) {
        Optional<Category> optional = categoryRepo.findById(categoryId);
        Category category = optional.orElseThrow(() -> new CategoryNotFoundException("No Category with id: " + categoryId));
        categoryRepo.delete(category);
    }

    @Override
    public Category updateCategory(Category updatedCategory) {
        int categoryId = updatedCategory.getCategoryId();
        Optional<Category> optional = categoryRepo.findById(categoryId);
        Category category = optional.orElseThrow(() -> new CategoryNotFoundException("No Category with id: " + categoryId));
        if (updatedCategory.getName() != null) {
            category.setName(updatedCategory.getName());
        }
        return categoryRepo.save(category);
    }

    @Override
    public Category saveCategory(Category category) {
        return categoryRepo.save(category);
    }

    @Override
    public boolean isCategoryNamePresent(String categoryName) {
        Optional<String> optionalName = categoryRepo.findByNameIgnoreCase(categoryName);
        return optionalName.isPresent();
    }
}
