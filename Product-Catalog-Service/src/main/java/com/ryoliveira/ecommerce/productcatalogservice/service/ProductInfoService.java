package com.ryoliveira.ecommerce.productcatalogservice.service;

import com.ryoliveira.ecommerce.productcatalogservice.model.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductInfoService {

    ProductInfo saveProductInfo(String productInfoJsonString, List<MultipartFile> imageFiles, MultipartFile mainProductImage);

    ProductInfo updateProductInfo(String updatedProdInfoJsonString, MultipartFile mainProductImageFile, List<MultipartFile> additionalProductImageFiles);

    void deleteProduct(int prodId);

    ProductInfo getProductInfo(int prodId);

    ProductInfoList getAllProducts();

    CategoryList getAllCategories();

    ProductInfoList getAllProductsByCategoryId(int categoryId);

    ProductInfoList getAllProductsContainingName(String name, int categoryId);



}
