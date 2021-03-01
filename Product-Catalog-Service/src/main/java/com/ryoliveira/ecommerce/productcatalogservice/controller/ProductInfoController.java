package com.ryoliveira.ecommerce.productcatalogservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryoliveira.ecommerce.productcatalogservice.model.CategoryList;
import com.ryoliveira.ecommerce.productcatalogservice.model.ProductInfo;
import com.ryoliveira.ecommerce.productcatalogservice.model.ProductInfoList;
import com.ryoliveira.ecommerce.productcatalogservice.service.ProductInfoService;

@RestController
@RequestMapping("/inventory")
public class ProductInfoController {
	
	Logger LOGGER = LoggerFactory.getLogger(ProductInfoController.class);

	@Autowired
	private ProductInfoService prodInfoService;

	@PostMapping(path="/productInfo", consumes= {"multipart/form-data"})
	private ResponseEntity<ProductInfo> saveProductInfo(@RequestPart("productInfo") String productInfoJsonString, @RequestPart("file") MultipartFile imageFile) {
		return new ResponseEntity<>(prodInfoService.saveProductInfo(productInfoJsonString, imageFile), HttpStatus.OK);
	}

	@PutMapping("/productInfo")
	private ResponseEntity<ProductInfo> updateProductInfo(@RequestPart("productInfo") String updatedProdInfoJsonString, @RequestPart("file") MultipartFile imageFile) {
		return new ResponseEntity<>(prodInfoService.updateProductInfo(updatedProdInfoJsonString, imageFile), HttpStatus.OK);
	}

	@GetMapping("/productInfo/{prodId}")
	private ResponseEntity<ProductInfo> getProductInfo(@PathVariable("prodId") int prodId) {
		return new ResponseEntity<>(prodInfoService.getProductInfo(prodId), HttpStatus.OK);
	}

	@DeleteMapping("/productInfo/{prodId}")
	private ResponseEntity<String> deleteProduct(@PathVariable("prodId") int prodId) {
		prodInfoService.deleteProduct(prodId);
		return new ResponseEntity<>("Product with id: " + prodId + " successfully deleted", HttpStatus.OK);
	}

	@GetMapping("/products")
	private ResponseEntity<ProductInfoList> getAllProducts() {
		return new ResponseEntity<>(prodInfoService.getAllProducts(), HttpStatus.OK);
	}

	@GetMapping("/products/{categoryId}")
	private ProductInfoList getAllProductsByCategoryId(@PathVariable("categoryId") int categoryId) {
		return prodInfoService.getAllProductsByCategoryId(categoryId);
	}

	@GetMapping("/categories")
	private CategoryList getAllCategories() {
		return prodInfoService.getAllCategories();
	}

}
