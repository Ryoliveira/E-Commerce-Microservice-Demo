package com.ryoliveira.ecommerce.productcatalogservice.controller;

import com.ryoliveira.ecommerce.productcatalogservice.model.CategoryList;
import com.ryoliveira.ecommerce.productcatalogservice.model.ProductInfo;
import com.ryoliveira.ecommerce.productcatalogservice.model.ProductInfoList;
import com.ryoliveira.ecommerce.productcatalogservice.service.ProductInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
