package com.ryoliveira.ecommerce.productcatalogservice.service;

import com.ryoliveira.ecommerce.productcatalogservice.model.Category;
import com.ryoliveira.ecommerce.productcatalogservice.model.CategoryList;
import com.ryoliveira.ecommerce.productcatalogservice.model.Product;
import com.ryoliveira.ecommerce.productcatalogservice.model.ProductInfo;
import com.ryoliveira.ecommerce.productcatalogservice.model.ProductInfoList;
import com.ryoliveira.ecommerce.productcatalogservice.model.Stock;

public interface ProductInfoService {
	
	ProductInfo saveProductInfo(ProductInfo prodInfo);
	
	ProductInfo updateProductInfo(ProductInfo updatedProdInfo);
	
	void deleteProduct(int prodId);
	
	ProductInfo getProductInfo(int prodId);
	
	Product getProduct(int prodId);
	
	Stock getProductStock(int prodId);
	
	Category getProductCategory(int categoryId);

	ProductInfoList getAllProducts();
	
	CategoryList getAllCategories();
	
	ProductInfoList getAllProductsByCategoryId(int categoryId);

}
