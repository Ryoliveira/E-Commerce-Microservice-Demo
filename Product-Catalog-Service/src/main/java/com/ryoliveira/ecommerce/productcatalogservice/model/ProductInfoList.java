package com.ryoliveira.ecommerce.productcatalogservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ProductInfoList {
    private List<ProductInfo> productInfoList;
}
