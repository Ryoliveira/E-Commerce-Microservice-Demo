package com.ryoliveira.ecommerce.category.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;


@Data
@AllArgsConstructor
public class CategoryList {

    private List<Category> categoryList;

}
