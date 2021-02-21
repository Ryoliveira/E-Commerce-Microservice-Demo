package com.ryoliveira.ecommerce.productcatalogservice.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.ryoliveira.ecommerce.productcatalogservice.model.Category;
import com.ryoliveira.ecommerce.productcatalogservice.model.CategoryList;
import com.ryoliveira.ecommerce.productcatalogservice.model.Image;
import com.ryoliveira.ecommerce.productcatalogservice.model.Product;
import com.ryoliveira.ecommerce.productcatalogservice.model.ProductInfo;
import com.ryoliveira.ecommerce.productcatalogservice.model.ProductInfoList;
import com.ryoliveira.ecommerce.productcatalogservice.model.Stock;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;

@Service
public class ProductInfoServiceImpl implements ProductInfoService {
	
	private final String PRODUCT_SERVICE = "PRODUCT-SERVICE";
	private final String STOCK_SERVICE = "STOCK-SERVICE";
	private final String CATEGORY_SERVICE = "CATEGORY-SERVICE";
	private final String IMAGE_SERVICE = "IMAGE-SERVICE";
	
	private final String BREAKER_NAME = "productInfoServiceBreaker";
	private final String RATE_LIMITER_NAME = "productInfoRateLimiter";

	@Autowired
	private RestTemplate restTemplate;
	
	private String productUrl = String.format("http://%s/product", PRODUCT_SERVICE);
	private String allProductsUrl = String.format("http://%s/products", PRODUCT_SERVICE);
	private String stockUrl = String.format("http://%s/stock", STOCK_SERVICE);
	private String categoryUrl = String.format("http://%s/category", CATEGORY_SERVICE);
	private String allCategoriesUrl = String.format("http://%s/categories", CATEGORY_SERVICE);;
	private String imageUrl = String.format("http://%s/image", IMAGE_SERVICE);
	
	
	Logger LOGGER = LoggerFactory.getLogger(ProductInfoServiceImpl.class);
	

	@Override
	public ProductInfo saveProductInfo(ProductInfo prodInfo) {
		Product product = saveProduct(prodInfo.getProduct());
		Stock stock = saveStock(product.getId(), prodInfo.getStock());
		Category category = getProductCategory(product.getCategoryId());
		ProductInfo response = new ProductInfo(product, stock, category, new Image());
		return response;
	}

	@Override
	public ProductInfo updateProductInfo(ProductInfo updatedProdInfo, MultipartFile imgFile) {
		restTemplate.put(this.productUrl, updatedProdInfo.getProduct());
		restTemplate.put(this.stockUrl, updatedProdInfo.getStock());
		return getProductInfo(updatedProdInfo.getProduct().getId());
	}

	@Override
	public void deleteProduct(int prodId) {
		String deleteProductUrl = this.productUrl + "/" + prodId;
		String deleteStockUrl = this.stockUrl + "/" + prodId;
		
		//Delete Product using product service
		ResponseEntity<String> productServiceResponse = restTemplate.exchange(deleteProductUrl, HttpMethod.DELETE, null, String.class);
		String productResponseMsg = productServiceResponse.getBody();
		LOGGER.info(productResponseMsg + " | Status Code: " + productServiceResponse.getStatusCode());
		
		//Delete stock using stock service
		ResponseEntity<String> stockServiceResponse = restTemplate.exchange(deleteStockUrl, HttpMethod.DELETE, null, String.class);
		String stockResponseMsg = stockServiceResponse.getBody();
		LOGGER.info(stockResponseMsg + " | Status Code: " + stockServiceResponse.getStatusCode()) ;
	}

	@CircuitBreaker(name=BREAKER_NAME, fallbackMethod="getProductInfoFallBack")
    //@RateLimiter(name=RATE_LIMITER_NAME, fallbackMethod="getProductRateLimiterFallback")
	@Override
	public ProductInfo getProductInfo(int prodId) {
		Product product = getProduct(prodId);
		Stock stock = getProductStock(prodId);
		// What if product comes back null?
		Category category = getProductCategory(product.getCategoryId());
		Image img = getProductImage(product.getId());
		return new ProductInfo(product, stock, category, img);
	}

    //@RateLimiter(name=RATE_LIMITER_NAME, fallbackMethod="getAllProductsRateLimiterFallback")
	@Override
	public ProductInfoList getAllProducts() {

		List<ProductInfo> productInfoList = new ArrayList<>();

		ResponseEntity<List<Product>> responseEntity = restTemplate.exchange(this.allProductsUrl,
				HttpMethod.GET, null, new ParameterizedTypeReference<List<Product>>() {
				});
		List<Product> products = responseEntity.getBody();

		//Populate list with product/stock/category info
		products.stream().forEach(product -> {
			Stock stock = getProductStock(product.getId());
			Category category = getProductCategory(product.getCategoryId());
			Image img = getProductImage(product.getId());
			productInfoList.add(new ProductInfo(product, stock, category, img));
		});

		return new ProductInfoList(productInfoList);
	}

	@Override
	public CategoryList getAllCategories() {
		return restTemplate.getForObject(this.allCategoriesUrl, CategoryList.class);
	}

	
	@RateLimiter(name=RATE_LIMITER_NAME, fallbackMethod="getAllProductsRateLimiterFallback")
	@Override
	public ProductInfoList getAllProductsByCategoryId(int categoryId) {
		List<ProductInfo> productInfoList = new ArrayList<>();

		ResponseEntity<List<Product>> responseEntity = restTemplate.exchange(this.allProductsUrl + "/" + categoryId,
				HttpMethod.GET, null, new ParameterizedTypeReference<List<Product>>() {
				});
		List<Product> products = responseEntity.getBody();

		//Populate list with product/stock/category info
		products.stream().forEach(product -> {
			Stock stock = getProductStock(product.getId());
			Category category = getProductCategory(product.getCategoryId());
			Image img = getProductImage(product.getId());
			productInfoList.add(new ProductInfo(product, stock, category, img));
		});

		return new ProductInfoList(productInfoList);
	}


	@Override
	public Product getProduct(int prodId) {
		return restTemplate.getForObject(this.productUrl + "/" + prodId, Product.class);
	}

	@Override
	public Stock getProductStock(int prodId) {
		String productStockUrl = this.stockUrl + "/" + prodId;
		ResponseEntity<Stock> response = restTemplate.getForEntity(productStockUrl, Stock.class);
		LOGGER.info("Stock retrieval code: " + response.getStatusCode());
		Stock stock = response.getBody();
		return stock;
	}

	
	@Override
	public Category getProductCategory(int categoryId) {
		return restTemplate.getForObject(this.categoryUrl + "/" + categoryId, Category.class);
	}
	
	@Override
	public Image getProductImage(int prodId) {
		return restTemplate.getForObject(this.imageUrl + "/" + prodId, Image.class);
	}
	
	
	public ProductInfo getProductInfoFallBack(int prodId, Exception e) {
		return new ProductInfo(new Product(), new Stock(), new Category(), new Image());
	}
	
	
    public ProductInfo getProductRateLimiterFallback(Throwable t) {
    	throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS);
    }
    
    public ProductInfoList getAllProductsRateLimiterFallback(Throwable t) {
    	throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS); 
    }

	@Override
	public Product saveProduct(Product product) {
		ResponseEntity<Product> response = restTemplate.postForEntity(this.productUrl, product, Product.class);
		LOGGER.info("Saved Product status code: " + response.getStatusCodeValue());
		Product savedProduct = response.getBody();
		return savedProduct;
	}

	@Override
	public Stock saveStock(int productId, Stock stock) {
		stock.setProductId(productId);
		ResponseEntity<Stock> response = restTemplate.postForEntity(this.stockUrl, stock, Stock.class);
		LOGGER.info("Saved Stock status code: " + response.getStatusCodeValue());
		Stock savedStock = response.getBody();
		return savedStock;
	}

	



}
