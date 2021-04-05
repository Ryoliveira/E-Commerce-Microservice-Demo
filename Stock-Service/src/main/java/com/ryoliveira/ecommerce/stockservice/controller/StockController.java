package com.ryoliveira.ecommerce.stockservice.controller;

import com.ryoliveira.ecommerce.stockservice.model.Stock;
import com.ryoliveira.ecommerce.stockservice.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class StockController {

    @Autowired
    private StockService stockService;

    @PostMapping(path = "/stock")
    private ResponseEntity<Stock> saveStock(@RequestBody Stock stock) {
        return new ResponseEntity<>(stockService.saveStock(stock), HttpStatus.OK);
    }

    @GetMapping(path = "/stock/{prodId}")
    private ResponseEntity<Stock> getStockByProductId(@PathVariable("prodId") int prodId) {
        return new ResponseEntity<>(stockService.findByProductId(prodId), HttpStatus.OK);
    }

    @PutMapping(path = "/stock")
    private ResponseEntity<Stock> updateStock(@RequestBody Stock UpdatedStock) {
        return new ResponseEntity<>(stockService.updateStock(UpdatedStock), HttpStatus.OK);
    }

    @DeleteMapping(path = "/stock/{prodId}")
    private ResponseEntity<String> deleteStock(@PathVariable("prodId") int prodId) {
        stockService.deleteStock(prodId);
        return new ResponseEntity<>("Stock deleted with product id: " + prodId, HttpStatus.OK);
    }

}
