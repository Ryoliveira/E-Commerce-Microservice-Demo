package com.ryoliveira.ecommerce.productcatalogservice.controller;

import java.util.Arrays;
import java.util.List;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
	private ResponseEntity<ProductInfo> saveProductInfo(@RequestPart("productInfo") String productInfoJsonString,
			@RequestPart(name="mainProductImage", required=true) MultipartFile mainProductImage, 
			@RequestPart(name="additionalFiles[]", required=false) List<MultipartFile> additionalFiles) {
		return new ResponseEntity<>(prodInfoService.saveProductInfo(productInfoJsonString, additionalFiles, mainProductImage), HttpStatus.OK);
	}

	
	//TODO ImagesToDelete is not being consumed properly
	@PutMapping(path="/productInfo", consumes= {"multipart/form-data"})
	private ResponseEntity<ProductInfo> updateProductInfo(@RequestPart("productInfo") String updatedProdInfoJsonString, 
			@RequestPart(name="mainProductImage", required=false) MultipartFile mainProductImage,
			@RequestPart(name="additionalImageFiles[]", required=false) List<MultipartFile> additionalImageFiles,
			@RequestPart(name="imagesToDelete[]", required=false) List<String> imagesToDelete) {
		return new ResponseEntity<>(prodInfoService.updateProductInfo(updatedProdInfoJsonString, mainProductImage, additionalImageFiles, imagesToDelete), HttpStatus.OK);
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
	private ResponseEntity<ProductInfoList> getAllProductsByCategoryId(@PathVariable("categoryId") int categoryId) {
		return new ResponseEntity<>(prodInfoService.getAllProductsByCategoryId(categoryId), HttpStatus.OK);
	}

	@GetMapping("/categories")
	private ResponseEntity<CategoryList> getAllCategories() {
		return new ResponseEntity<>(prodInfoService.getAllCategories(), HttpStatus.OK);
	}

}
