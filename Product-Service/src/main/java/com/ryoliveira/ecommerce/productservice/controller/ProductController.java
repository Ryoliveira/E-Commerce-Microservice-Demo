package com.ryoliveira.ecommerce.productservice.controller;

import java.util.List;
import java.util.NoSuchElementException;

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

	@Autowired
	private ProductService prodService;

	@PostMapping("/product")
	private ResponseEntity<Product> saveProduct(@RequestBody Product product) {
		return new ResponseEntity<>(prodService.saveProduct(product), HttpStatus.OK);
	}

	@PutMapping("/product")
	private ResponseEntity<Product> updateProduct(@RequestBody Product product) {
		try {
			return new ResponseEntity<>(prodService.updateProduct(product), HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/product/{prodId}")
	private ResponseEntity<Product> getProduct(@PathVariable("prodId") int prodId) {
		try {
			return new ResponseEntity<>(prodService.findProductById(prodId), HttpStatus.OK);
		}catch(NoSuchElementException e) {
			// TODO Need to refactor to handle errors correctly
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/product/{prodId}")
	private ResponseEntity<String> removeProduct(@PathVariable("prodId") int prodId) {
		try {
			prodService.removeProduct(prodId);
			return new ResponseEntity<>("Product removed with id: " + prodId, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			// TODO Need to refactor to handle errors correctly
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
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
