package com.programmingbuddies.ecommerce.productcatalog.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.programmingbuddies.ecommerce.productcatalog.model.Product;
import com.programmingbuddies.ecommerce.productcatalog.service.ProductService;

@RestController
public class ProductController {
	
	@Autowired
	private ProductService prodService;
	
	@PostMapping("/product")
	private Product saveProduct(@RequestBody Product product) {
		return prodService.saveProduct(product);
	}
	
	@PutMapping("/product")
	private Product updateProduct(@RequestBody Product product) {
		return prodService.updateProduct(product);
	}
	
	@GetMapping("/product/{prodId}")
	private Product getProduct(@PathVariable("prodId") int prodId) {
		return prodService.findProductById(prodId);
	}
	
	@DeleteMapping("/product/{prodId}")
	private void removeProduct(@PathVariable("prodId") int prodId) {
		prodService.removeProduct(prodId);
	}
	
	@GetMapping("/products")
	private List<Product> getAllProducts(){
		return prodService.findAll();
	}
	
	

}
