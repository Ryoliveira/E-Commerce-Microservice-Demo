package com.ryoliveira.ecommerce.productservice.controller;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ryoliveira.ecommerce.productservice.model.Product;
import com.ryoliveira.ecommerce.productservice.service.ProductService;

@RestController
public class ProductController {

	Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

	@Autowired
	private ProductService prodService;

	@PostMapping("/product")
	private ResponseEntity<Product> saveProduct(@RequestBody Product product) {
		return new ResponseEntity<>(prodService.saveProduct(product), HttpStatus.OK);
	}

	@PutMapping("/product")
	private ResponseEntity<Product> updateProduct(@RequestBody Product product) {
		return new ResponseEntity<>(prodService.updateProduct(product), HttpStatus.OK);
	}

	@GetMapping("/product/{prodId}")
	private ResponseEntity<Product> getProduct(@PathVariable("prodId") int prodId) {
		return new ResponseEntity<>(prodService.findProductById(prodId), HttpStatus.OK);
	}

	@DeleteMapping("/product/{prodId}")
	private ResponseEntity<String> removeProduct(@PathVariable("prodId") int prodId) {
		prodService.removeProduct(prodId);
		return new ResponseEntity<>("Product removed with id: " + prodId, HttpStatus.OK);
	}

	@GetMapping("/products")
	private List<Product> getAllProducts() {
		return prodService.findAll();
	}

	@GetMapping("/products/{categoryId}")
	private List<Product> getAllProductsByCategoryId(@PathVariable("categoryId") int categoryId) {
		return prodService.findAllByCategoryId(categoryId);
	}

}
