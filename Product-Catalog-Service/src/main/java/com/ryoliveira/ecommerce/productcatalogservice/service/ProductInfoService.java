package com.ryoliveira.ecommerce.productcatalogservice.service;

import java.util.List;

import com.ryoliveira.ecommerce.productcatalogservice.model.ProductInfo;

public interface ProductInfoService {
	
	ProductInfo saveProductInfo(ProductInfo prodInfo);
	
	ProductInfo updateProductInfo(ProductInfo updatedProdInfo);
	
	void deleteProduct(int prodId);
	
	ProductInfo getProductInfo(int prodId);
	
	List<ProductInfo> getAllProducts();

}
