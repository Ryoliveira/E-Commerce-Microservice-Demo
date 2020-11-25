package com.ryoliveira.ecommerce.stockservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ryoliveira.ecommerce.stockservice.model.Stock;
import com.ryoliveira.ecommerce.stockservice.service.StockService;

@RestController
public class StockController {
	
	@Autowired
	private StockService stockService;
	
	
	@PostMapping("/stock")
	private Stock saveStock(@RequestBody Stock stock) {
		return stockService.saveStock(stock);
	}
	
	@GetMapping("/stock/{prodId}")
	private Stock getStockByProductId(@PathVariable("prodId") int prodId) {
		return stockService.findByProductId(prodId);
	}
	
	@PutMapping("/stock")
	private Stock updateStock(@RequestBody Stock UpdatedStock) {
		return stockService.updateStock(UpdatedStock);
	}
	
	@DeleteMapping("/stock/{prodId}")
	private void deleteStock(@PathVariable("prodId") int prodId) {
		stockService.deleteStock(prodId);
	}

}
