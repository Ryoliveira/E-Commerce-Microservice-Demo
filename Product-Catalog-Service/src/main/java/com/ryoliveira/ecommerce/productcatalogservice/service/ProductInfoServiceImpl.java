package com.ryoliveira.ecommerce.productcatalogservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.ws.rs.core.UriBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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
	private String allProductImagesUrl = String.format("http://%s/images", IMAGE_SERVICE);
	
	
	Logger LOGGER = LoggerFactory.getLogger(ProductInfoServiceImpl.class);
	

	@Override
	public ProductInfo saveProductInfo(String productInfoJsonString, List<MultipartFile> imageFiles, MultipartFile mainProductImage) {
		ProductInfo productInfo = mapJsonToProductInfo(productInfoJsonString);
		Product product = saveProduct(productInfo.getProduct());
		Stock stock = saveStock(product.getId(), productInfo.getStock());
		Category category = getProductCategory(product.getCategoryId());
		
		//save images
		List<Image> productImages = saveImages(product.getId(), imageFiles, mainProductImage);
		
		Image mainImage = productImages.get(0);
		productImages.remove(0);
		
		ProductInfo savedProduct = new ProductInfo(product, stock, category, mainImage, productImages);
		return savedProduct;
	}

	
	//TODO Update method to work with list of images
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
//			if(imageFile != null) {				
//				Image updatedImg = imageService.createImageObject(productInfo.getProduct().getId(), imageFile);
//				HttpEntity<Image> imageEntity = new HttpEntity<>(updatedImg, null);
//				ResponseEntity<Image> imageServiceResponse = restTemplate.exchange(this.imageUrl + "/" + productInfo.getProduct().getId(), HttpMethod.PUT, imageEntity, Image.class);
//				LOGGER.info("Image-Service Response: Code -- " + imageServiceResponse.getStatusCodeValue() + " | Updated Image: File Name --" + imageServiceResponse.getBody().getFileName() + " | File Type: " + imageServiceResponse.getBody().getFileType());
//			}
			return getProductInfo(productInfo.getProduct().getId());
		}catch(HttpStatusCodeException e) {
			throw new NoSuchElementException(e.getMessage());
		}
	}

	@Override
	public void deleteProduct(int prodId) throws NoSuchElementException{
		String deleteProductUrl = this.productUrl + "/" + prodId;
		String deleteStockUrl = this.stockUrl + "/" + prodId;
		String deleteImagesUrl = this.allProductImagesUrl + "/" + prodId;
		
		try {
			//Delete Product using product service
			ResponseEntity<String> productServiceResponse = restTemplate.exchange(deleteProductUrl, HttpMethod.DELETE, null, String.class);
			LOGGER.info("Product-Service: " + productServiceResponse.getBody() + " | Status Code: " + productServiceResponse.getStatusCode());
			
			//Delete stock using stock service
			ResponseEntity<String> stockServiceResponse = restTemplate.exchange(deleteStockUrl, HttpMethod.DELETE, null, String.class);
			LOGGER.info("Stock-Service: " + stockServiceResponse.getBody() + " | Status Code: " + stockServiceResponse.getStatusCode());
			
			//Delete images using image service
			ResponseEntity<String> imageServiceResponse = restTemplate.exchange(deleteImagesUrl, HttpMethod.DELETE, null, String.class);
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
			Image mainProductImage = getProductImages(product.getId(), true).get(0);
			List<Image> images = getProductImages(product.getId(), false);
			return new ProductInfo(product, stock, category, mainProductImage, images);
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
			Image mainProductImage = getProductImages(product.getId(), true).get(0);
			List<Image> images = getProductImages(product.getId(), false);
			productInfoList.add(new ProductInfo(product, stock, category, mainProductImage, images));
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
			Image mainProductImage = getProductImages(product.getId(), true).get(0);
			List<Image> images = getProductImages(product.getId(), false);
			productInfoList.add(new ProductInfo(product, stock, category, mainProductImage, images));
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
	public List<Image> getProductImages(int prodId, boolean productMainImageOnly) {
		String url = UriComponentsBuilder.fromHttpUrl(this.allProductImagesUrl + "/" + prodId).queryParam("productMainImageOnly", productMainImageOnly).toUriString();
		ResponseEntity<List<Image>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Image>>(){});
		return response.getBody();
	}
	
	
	
//	public ProductInfo getProductInfoFallBack(int prodId, Exception e) {
//		return new ProductInfo(new Product(), new Stock(), new Category(), new Image());
//	}
	
	
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
	public List<Image> saveImages(int productId, List<MultipartFile> imageFiles, MultipartFile mainProductImageFile) {
		
		List<Image> savedImages = new ArrayList<>();
		
		
		// Save main product image
		Image mainProductImage = imageService.createImageObject(productId, mainProductImageFile, true);
		HttpEntity<Image> mainImageEntity = new HttpEntity<>(mainProductImage, null);
		ResponseEntity<Image> mainImageSaveResponse = restTemplate.exchange(this.imageUrl, HttpMethod.POST, mainImageEntity, Image.class);
		savedImages.add(mainImageSaveResponse.getBody());
		
		// Save product addtional images
		for(MultipartFile imageFile : imageFiles) {
			Image imgToBeSaved = imageService.createImageObject(productId, imageFile, false);
			HttpEntity<Image> httpEntity = new HttpEntity<Image>(imgToBeSaved, null);
			ResponseEntity<Image> imgResponse = restTemplate.exchange(this.imageUrl, HttpMethod.POST, httpEntity, Image.class);
			Image savedImage = imgResponse.getBody();
			savedImages.add(savedImage);
		}
		return savedImages;
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
