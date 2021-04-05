package com.ryoliveira.ecommerce.productcatalogservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductInfo {

    private Product product;
    private Stock stock;
    private Category category;
    private Image mainProductImage;
    private List<Image> imgList;

}
