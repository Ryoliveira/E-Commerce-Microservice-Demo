package com.ryoliveira.ecommerce.productservice.controller;

import com.ryoliveira.ecommerce.productservice.model.Product;
import com.ryoliveira.ecommerce.productservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class ProductController {

    @Autowired
    private ProductService prodService;

    @PostMapping(path = "/product")
    private ResponseEntity<Product> saveProduct(@RequestBody Product product) {
        return new ResponseEntity<>(prodService.saveProduct(product), HttpStatus.OK);
    }

    @PutMapping(path = "/product")
    private ResponseEntity<Product> updateProduct(@RequestBody Product product) {
        return new ResponseEntity<>(prodService.updateProduct(product), HttpStatus.OK);
    }

    @GetMapping(path = "/product/{prodId}")
    private ResponseEntity<Product> getProduct(@PathVariable("prodId") int prodId) {
        return new ResponseEntity<>(prodService.findProductById(prodId), HttpStatus.OK);
    }

    @DeleteMapping(path = "/product/{prodId}")
    private ResponseEntity<String> removeProduct(@PathVariable("prodId") int prodId) {
        prodService.removeProduct(prodId);
        return new ResponseEntity<>("Product removed with id: " + prodId, HttpStatus.OK);
    }

    @GetMapping(path = "/products")
    private ResponseEntity<List<Product>> getAllProducts() {
        return new ResponseEntity<>(prodService.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/products/{categoryId}")
    private ResponseEntity<List<Product>> getAllProductsByCategoryId(@PathVariable("categoryId") int categoryId) {
        return new ResponseEntity<>(prodService.findAllByCategoryId(categoryId), HttpStatus.OK);
    }

    @GetMapping(path = "/products/search/{name}")
    private ResponseEntity<List<Product>> getProductsThatContainSearchQuery(@PathVariable("name") String name,
                                                                            @RequestParam("searchCategoryId") int searchCategoryId) {
        return new ResponseEntity<>(prodService.findAllByNameContaining(name, searchCategoryId), HttpStatus.OK);
    }
}
