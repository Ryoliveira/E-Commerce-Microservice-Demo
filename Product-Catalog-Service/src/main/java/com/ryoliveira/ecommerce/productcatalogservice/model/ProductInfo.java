package com.ryoliveira.ecommerce.productcatalogservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductInfo {
	
	private Product product;
	private Stock stock;
	private Category category;

}
