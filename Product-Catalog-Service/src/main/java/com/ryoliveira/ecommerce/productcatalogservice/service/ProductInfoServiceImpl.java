package com.ryoliveira.ecommerce.productcatalogservice.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ryoliveira.ecommerce.productcatalogservice.model.Category;
import com.ryoliveira.ecommerce.productcatalogservice.model.CategoryList;
import com.ryoliveira.ecommerce.productcatalogservice.model.Product;
import com.ryoliveira.ecommerce.productcatalogservice.model.ProductInfo;
import com.ryoliveira.ecommerce.productcatalogservice.model.ProductInfoList;
import com.ryoliveira.ecommerce.productcatalogservice.model.Stock;

@Service
public class ProductInfoServiceImpl implements ProductInfoService {

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public ProductInfo saveProductInfo(ProductInfo prodInfo) {
		Product product = restTemplate.postForObject("http://PRODUCT-SERVICE/product", prodInfo.getProduct(),
				Product.class);
		prodInfo.getStock().setProductId(product.getId());
		Stock stock = restTemplate.postForObject("http://STOCK-SERVICE/stock", prodInfo.getStock(), Stock.class);
		Category category = restTemplate.getForObject("http://CATEGORY-SERVICE/category/" + product.getCategoryId(), Category.class);
		ProductInfo response = new ProductInfo(product, stock, category);
		return response;
	}

	@Override
	public ProductInfo updateProductInfo(ProductInfo updatedProdInfo) {
		restTemplate.put("http://PRODUCT-SERVICE/product", updatedProdInfo.getProduct());
		restTemplate.put("http://STOCK-SERVICE/stock", updatedProdInfo.getStock());
		ProductInfo prodInfo = getProductInfo(updatedProdInfo.getProduct().getId());
		return prodInfo;
	}

	@Override
	public void deleteProduct(int prodId) {
		restTemplate.delete("http://PRODUCT-SERVICE/product/"+prodId);
		restTemplate.delete("http://STOCK-SERVICE/stock/"+prodId);
	}

	@Override
	public ProductInfo getProductInfo(int prodId) {
		Product product = restTemplate.getForObject("http://PRODUCT-SERVICE/product/" + prodId, Product.class);
		Stock stock = restTemplate.getForObject("http://STOCK-SERVICE/stock/" + prodId, Stock.class);
		Category category = restTemplate.getForObject("http://CATEGORY-SERVICE/category/" + product.getCategoryId(), Category.class);
		return new ProductInfo(product, stock, category);
	}

	@Override
	public ProductInfoList getAllProducts() {

		List<ProductInfo> productInfoList = new ArrayList<>();

		ResponseEntity<List<Product>> responseEntity = restTemplate.exchange("http://PRODUCT-SERVICE/products",
				HttpMethod.GET, null, new ParameterizedTypeReference<List<Product>>() {
				});
		List<Product> products = responseEntity.getBody();

		//Populate list with product/stock/category info
		products.stream().forEach(product -> {
			Stock stock = restTemplate.getForObject("http://STOCK-SERVICE/stock/" + product.getId(), Stock.class);
			Category category = restTemplate.getForObject("http://CATEGORY-SERVICE/category/" + product.getCategoryId(), Category.class);
			productInfoList.add(new ProductInfo(product, stock, category));
		});

		return new ProductInfoList(productInfoList);
	}

	@Override
	public CategoryList getAllCategories() {
		return restTemplate.getForObject("http://CATEGORY-SERVICE/categories", CategoryList.class);
	}

}
