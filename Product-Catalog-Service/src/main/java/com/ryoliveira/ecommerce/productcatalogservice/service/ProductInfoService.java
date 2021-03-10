package com.ryoliveira.ecommerce.productcatalogservice.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ryoliveira.ecommerce.productcatalogservice.model.Category;
import com.ryoliveira.ecommerce.productcatalogservice.model.CategoryList;
import com.ryoliveira.ecommerce.productcatalogservice.model.Image;
import com.ryoliveira.ecommerce.productcatalogservice.model.Product;
import com.ryoliveira.ecommerce.productcatalogservice.model.ProductInfo;
import com.ryoliveira.ecommerce.productcatalogservice.model.ProductInfoList;
import com.ryoliveira.ecommerce.productcatalogservice.model.Stock;

public interface ProductInfoService {
	
	ProductInfo saveProductInfo(String productInfoJsonString, List<MultipartFile> imageFiles, MultipartFile mainProductImage);
	
	ProductInfo updateProductInfo(String updatedProdInfoJsonString, MultipartFile imageFile);
	
	void deleteProduct(int prodId);
	
	ProductInfo getProductInfo(int prodId);
	
	Product getProduct(int prodId);
	
	Stock getProductStock(int prodId);
	
	Category getProductCategory(int categoryId);
	
	List<Image> getProductImages(int prodId, boolean productMainImageOnly);

	ProductInfoList getAllProducts();
	
	CategoryList getAllCategories();
	
	ProductInfoList getAllProductsByCategoryId(int categoryId);
	
	Product saveProduct(Product product);
	
	Stock saveStock(int productId, Stock stock);
	
	List<Image> saveImages(int productId, List<MultipartFile> imageFiles, MultipartFile mainProductImage);
	
	ProductInfo mapJsonToProductInfo(String json);
	
	
	
	

}
