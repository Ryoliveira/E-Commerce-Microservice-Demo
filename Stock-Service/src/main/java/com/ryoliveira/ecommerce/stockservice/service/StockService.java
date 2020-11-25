package com.ryoliveira.ecommerce.stockservice.service;

import com.ryoliveira.ecommerce.stockservice.model.Stock;

public interface StockService {

	Stock saveStock(Stock stock);
	
	Stock findByProductId(int prodId);
	
	Stock updateStock(Stock stock);
	
	void deleteStock(int prodId);
}
