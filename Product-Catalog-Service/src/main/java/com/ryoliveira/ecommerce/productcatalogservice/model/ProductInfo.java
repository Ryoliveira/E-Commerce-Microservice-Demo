package com.ryoliveira.ecommerce.productcatalogservice.model;

import java.util.List;

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
	private List<Image> imgList;

}
