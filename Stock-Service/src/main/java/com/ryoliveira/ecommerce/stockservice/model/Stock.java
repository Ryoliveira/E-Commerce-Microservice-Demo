package com.ryoliveira.ecommerce.stockservice.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "stock")
public class Stock {

    @Id
    @Column(name = "product_id")
    private int productId;
    @Column(name = "stock")
    private int stock;
}
