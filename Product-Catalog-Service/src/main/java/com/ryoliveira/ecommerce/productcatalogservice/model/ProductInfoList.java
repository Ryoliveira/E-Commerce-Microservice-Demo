package com.ryoliveira.ecommerce.productcatalogservice.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductInfoList {
	private List<ProductInfo> productInfoList;
}
