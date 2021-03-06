package com.ryoliveira.ecommerce.productcatalogservice.model;

import lombok.Data;

@Data
public class Product {

    private int id;
    private String name;
    private String description;
    private int categoryId;
    private Float price;

}
