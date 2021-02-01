package com.ryoliveira.ecommerce.productcatalogservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductInfo {
	
	private Product product;
	private Stock stock;
	private Category category;

}
