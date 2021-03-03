package com.ryoliveira.ecommerce.productcatalogservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryoliveira.ecommerce.productcatalogservice.model.Category;
import com.ryoliveira.ecommerce.productcatalogservice.model.CategoryList;
import com.ryoliveira.ecommerce.productcatalogservice.model.Image;
import com.ryoliveira.ecommerce.productcatalogservice.model.Product;
import com.ryoliveira.ecommerce.productcatalogservice.model.ProductInfo;
import com.ryoliveira.ecommerce.productcatalogservice.model.ProductInfoList;
import com.ryoliveira.ecommerce.productcatalogservice.model.Stock;

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
	
	@Autowired
	private ImageService imageService;
	
	private String productUrl = String.format("http://%s/product", PRODUCT_SERVICE);
	private String allProductsUrl = String.format("http://%s/products", PRODUCT_SERVICE);
	private String stockUrl = String.format("http://%s/stock", STOCK_SERVICE);
	private String categoryUrl = String.format("http://%s/category", CATEGORY_SERVICE);
	private String allCategoriesUrl = String.format("http://%s/categories", CATEGORY_SERVICE);;
	private String imageUrl = String.format("http://%s/image", IMAGE_SERVICE);
	
	
	Logger LOGGER = LoggerFactory.getLogger(ProductInfoServiceImpl.class);
	

	@Override
	public ProductInfo saveProductInfo(String productInfoJsonString, MultipartFile imageFile) {
		ProductInfo productInfo = mapJsonToProductInfo(productInfoJsonString);
		Product product = saveProduct(productInfo.getProduct());
		Stock stock = saveStock(product.getId(), productInfo.getStock());
		Category category = getProductCategory(product.getCategoryId());
		
		//save image
		Image img = imageService.createImageObject(product.getId(), imageFile);
		Image productImg = saveImage(product.getId(), img);
		
		
		ProductInfo savedProduct = new ProductInfo(product, stock, category, productImg);
		return savedProduct;
	}

	@Override
	public ProductInfo updateProductInfo(String updatedProdInfoJsonString, MultipartFile imageFile) throws NoSuchElementException{
		ProductInfo productInfo = mapJsonToProductInfo(updatedProdInfoJsonString);
		try {
			//Update Product using product-service
			HttpEntity<Product> productEntity = new HttpEntity<>(productInfo.getProduct(), null);
			ResponseEntity<Product> productServiceResponse = restTemplate.exchange(this.productUrl, HttpMethod.PUT, productEntity, Product.class);
			LOGGER.info("Product-Service Response: Code -- " + productServiceResponse.getStatusCodeValue() + " | Updated Product: " + productServiceResponse.getBody().toString());
			//Update Stock using stock-service
			HttpEntity<Stock> stockEntity = new HttpEntity<>(productInfo.getStock(), null);
			ResponseEntity<Stock> stockServiceResponse = restTemplate.exchange(this.stockUrl, HttpMethod.PUT, stockEntity, Stock.class);
			LOGGER.info("Stock-Service Response: Code -- " + stockServiceResponse.getStatusCodeValue() + " | Updated Stock: " + stockServiceResponse.getBody().toString());
			//Update Image using image-service if not null
			if(imageFile != null) {				
				Image updatedImg = imageService.createImageObject(productInfo.getProduct().getId(), imageFile);
				HttpEntity<Image> imageEntity = new HttpEntity<>(updatedImg, null);
				ResponseEntity<Image> imageServiceResponse = restTemplate.exchange(this.imageUrl + "/" + productInfo.getProduct().getId(), HttpMethod.PUT, imageEntity, Image.class);
				LOGGER.info("Image-Service Response: Code -- " + imageServiceResponse.getStatusCodeValue() + " | Updated Image: File Name --" + imageServiceResponse.getBody().getFileName() + " | File Type: " + imageServiceResponse.getBody().getFileType());
			}
			return getProductInfo(productInfo.getProduct().getId());
		}catch(HttpStatusCodeException e) {
			throw new NoSuchElementException(e.getMessage());
		}
	}

	@Override
	public void deleteProduct(int prodId) throws NoSuchElementException{
		String deleteProductUrl = this.productUrl + "/" + prodId;
		String deleteStockUrl = this.stockUrl + "/" + prodId;
		String deleteImageUrl = this.imageUrl + "/" + prodId;
		
		try {
			//Delete Product using product service
			ResponseEntity<String> productServiceResponse = restTemplate.exchange(deleteProductUrl, HttpMethod.DELETE, null, String.class);
			LOGGER.info("Product-Service: " + productServiceResponse.getBody() + " | Status Code: " + productServiceResponse.getStatusCode());
			
			//Delete stock using stock service
			ResponseEntity<String> stockServiceResponse = restTemplate.exchange(deleteStockUrl, HttpMethod.DELETE, null, String.class);
			LOGGER.info("Stock-Service: " + stockServiceResponse.getBody() + " | Status Code: " + stockServiceResponse.getStatusCode());
			
			//Delete image using image service
			ResponseEntity<String> imageServiceResponse = restTemplate.exchange(deleteImageUrl, HttpMethod.DELETE, null, String.class);
			LOGGER.info("Image-Service: " + imageServiceResponse.getBody() + " | Status Code: " + imageServiceResponse.getStatusCode());
		}catch(HttpStatusCodeException e) {
			throw new NoSuchElementException(e.getMessage());
		}
	}

	//@CircuitBreaker(name=BREAKER_NAME, fallbackMethod="getProductInfoFallBack")
    //@RateLimiter(name=RATE_LIMITER_NAME, fallbackMethod="getProductRateLimiterFallback")
	@Override
	public ProductInfo getProductInfo(int prodId) throws NoSuchElementException {
		try {
			Product product = getProduct(prodId);
			Stock stock = getProductStock(prodId);
			Category category = getProductCategory(product.getCategoryId());
			Image img = getProductImage(product.getId());
			return new ProductInfo(product, stock, category, img);
		}catch(HttpStatusCodeException e) {
			throw new NoSuchElementException(e.getMessage());
		}
	
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

	
	//@RateLimiter(name=RATE_LIMITER_NAME, fallbackMethod="getAllProductsRateLimiterFallback")
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
	
	@Override
	public Image saveImage(int productId, Image image) {
		HttpHeaders headers = new HttpHeaders();
		
		//Image file is not being sent properly to image service!!!!
		
		HttpEntity<Image> httpEntity = new HttpEntity<Image>(image, null);
		
		ResponseEntity<Image> imgResponse = restTemplate.exchange(this.imageUrl, HttpMethod.POST, httpEntity, Image.class);
		
		Image img = imgResponse.getBody();
		
		return img;
	}
	
	@Override
	public ProductInfo mapJsonToProductInfo(String json) {
		ProductInfo productInfo = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			 productInfo = mapper.readValue(json, ProductInfo.class);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return productInfo;
		
		
	}
	
	

	



}
