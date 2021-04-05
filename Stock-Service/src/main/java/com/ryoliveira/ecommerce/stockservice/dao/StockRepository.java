package com.ryoliveira.ecommerce.stockservice.dao;

import com.ryoliveira.ecommerce.stockservice.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Integer> {

}
