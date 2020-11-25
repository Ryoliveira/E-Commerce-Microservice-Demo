package com.ryoliveira.ecommerce.stockservice.service;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ryoliveira.ecommerce.stockservice.dao.StockRepository;
import com.ryoliveira.ecommerce.stockservice.model.Stock;

@Service
public class StockServiceImpl implements StockService {
	
	Logger LOGGER = LoggerFactory.getLogger(StockServiceImpl.class);
	
	@Autowired
	private StockRepository stockRepo;

	@Override
	public Stock saveStock(Stock stock) {
		return stockRepo.save(stock);
	}

	@Override
	public Stock findByProductId(int prodId) {
		Optional<Stock> optional = stockRepo.findById(prodId);
		Stock stock = new Stock();
		try {
			stock = optional.orElseThrow(() -> new NoSuchElementException("No Stock with id: " + prodId));
		}catch(NoSuchElementException e) {
			LOGGER.error(e.getMessage());
		}
		return stock;
	}

	@Override
	public Stock updateStock(Stock stock) {
		Optional<Stock> optional = stockRepo.findById(stock.getProductId());
		Stock originalStock = new Stock();
		try {
			originalStock = optional.orElseThrow(() -> new NoSuchElementException("No Stock with id: " + stock.getProductId()));
			originalStock.setStock(stock.getStock());
			stockRepo.save(originalStock);
		}catch(NoSuchElementException e) {
			LOGGER.error(e.getMessage());
		}
		return stock;
	}

	@Override
	public void deleteStock(int prodId) {
		stockRepo.deleteById(prodId);
	}

}
