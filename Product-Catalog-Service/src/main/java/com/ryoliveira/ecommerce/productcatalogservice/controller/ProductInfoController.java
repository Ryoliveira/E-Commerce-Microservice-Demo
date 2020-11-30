package com.ryoliveira.ecommerce.productcatalogservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ryoliveira.ecommerce.productcatalogservice.model.ProductInfo;
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
	private ProductInfo getProductInfo(@PathVariable("prodId") int prodId) {
		return prodInfoService.getProductInfo(prodId);	
	}
	
	@DeleteMapping("/productInfo/{prodId}")
	private void deleteProduct(@PathVariable("prodId") int prodId) {
		prodInfoService.deleteProduct(prodId);
	}
	
	@GetMapping("/products")
	private List<ProductInfo> getAllProducts() {
		return prodInfoService.getAllProducts();
	}

}
