package com.ryoliveira.ecommerce.productcatalogservice.controller;

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
import org.springframework.web.bind.annotation.RestController;

import com.ryoliveira.ecommerce.productcatalogservice.model.CategoryList;
import com.ryoliveira.ecommerce.productcatalogservice.model.ProductInfo;
import com.ryoliveira.ecommerce.productcatalogservice.model.ProductInfoList;
import com.ryoliveira.ecommerce.productcatalogservice.service.ProductInfoService;

@RestController
@RequestMapping("/inventory")
public class ProductInfoController {

	@Autowired
	private ProductInfoService prodInfoService;

	@PostMapping("/productInfo")
	private ProductInfo saveProductInfo(@RequestBody ProductInfo productInfo) {
		return prodInfoService.saveProductInfo(productInfo);
	}

	@PutMapping("/productInfo")
	private ProductInfo updateProductInfo(@RequestBody ProductInfo updatedProdInfo) {
		return prodInfoService.updateProductInfo(updatedProdInfo);
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
	private ProductInfoList getAllProducts() {
		return prodInfoService.getAllProducts();
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
