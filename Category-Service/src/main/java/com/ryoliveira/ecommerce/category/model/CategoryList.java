package com.ryoliveira.ecommerce.category.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class CategoryList {
	
	private List<Category> categoryList;
	
}
