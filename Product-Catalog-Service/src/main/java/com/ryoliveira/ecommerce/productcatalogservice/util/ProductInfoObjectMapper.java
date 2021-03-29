package com.ryoliveira.ecommerce.productcatalogservice.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryoliveira.ecommerce.productcatalogservice.model.ProductInfo;
import org.springframework.stereotype.Service;

@Service
public class ProductInfoObjectMapper {

    public ProductInfo mapToProductInfoObject(String json) {
        ProductInfo productInfo = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            productInfo = mapper.readValue(json, ProductInfo.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return productInfo;
    }
}
