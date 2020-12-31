package com.ryoliveira.ecommerce.productcatalogservice.service;

import com.ryoliveira.ecommerce.productcatalogservice.model.CategoryList;
import com.ryoliveira.ecommerce.productcatalogservice.model.ProductInfo;
import com.ryoliveira.ecommerce.productcatalogservice.model.ProductInfoList;

public interface ProductInfoService {
	
	ProductInfo saveProductInfo(ProductInfo prodInfo);
	
	ProductInfo updateProductInfo(ProductInfo updatedProdInfo);
	
	void deleteProduct(int prodId);
	
	ProductInfo getProductInfo(int prodId);

	ProductInfoList getAllProducts();
	
	CategoryList getAllCategories();

}
