package com.ryoliveira.ecommerce.stockservice.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ryoliveira.ecommerce.stockservice.model.Stock;

public interface StockRepository extends JpaRepository<Stock, Integer> {

}
