package com.ryoliveira.ecommerce.productcatalogservice.service;

import java.util.List;

import com.ryoliveira.ecommerce.productcatalogservice.model.ProductInfo;
import com.ryoliveira.ecommerce.productcatalogservice.model.ProductInfoList;

public interface ProductInfoService {
	
	ProductInfo saveProductInfo(ProductInfo prodInfo);
	
	ProductInfo updateProductInfo(ProductInfo updatedProdInfo);
	
	void deleteProduct(int prodId);
	
	ProductInfo getProductInfo(int prodId);

	ProductInfoList getAllProducts();

}
