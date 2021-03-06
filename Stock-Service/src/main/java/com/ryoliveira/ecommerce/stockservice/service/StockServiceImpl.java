package com.ryoliveira.ecommerce.stockservice.service;

import com.ryoliveira.ecommerce.stockservice.dao.StockRepository;
import com.ryoliveira.ecommerce.stockservice.exception.StockNotFoundException;
import com.ryoliveira.ecommerce.stockservice.model.Stock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class StockServiceImpl implements StockService {

    Logger LOGGER = LoggerFactory.getLogger(StockServiceImpl.class);

    @Autowired
    private StockRepository stockRepo;

    @Override
    public Stock saveStock(Stock stock) {
        Stock savedStock = stockRepo.save(stock);
        LOGGER.info("Saved Stock: " + savedStock.toString());
        return savedStock;
    }

    @Override
    public Stock findByProductId(int prodId) {
        Optional<Stock> optional = stockRepo.findById(prodId);
        Stock stock = optional.orElseThrow(() -> new StockNotFoundException("No stock found with product id: " + prodId));
        LOGGER.info("Stock Pulled -- " + stock.toString());
        return stock;
    }

    @Override
    public Stock updateStock(Stock stock) {
        Optional<Stock> optional = stockRepo.findById(stock.getProductId());
        Stock originalStock = optional
                .orElseThrow(() -> new StockNotFoundException("No stock found with product id: " + stock.getProductId()));
        LOGGER.info("Original Stock -- " + originalStock.toString());
        originalStock.setStock(stock.getStock());
        LOGGER.info("Updated Stock -- " + stockRepo.findById(stock.getProductId()).get().toString());
        return stockRepo.save(originalStock);
    }

    @Override
    public void deleteStock(int prodId) throws NoSuchElementException {
        try {
            stockRepo.deleteById(prodId);
            LOGGER.info("Stock Deleted -- ID: " + prodId);
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage());
            throw new StockNotFoundException("No stock found with product id: " + prodId);
        }
    }

}
