package com.ryoliveira.ecommerce.productcatalogservice.service;

import com.ryoliveira.ecommerce.productcatalogservice.model.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductInfoService {

    ProductInfo saveProductInfo(String productInfoJsonString, List<MultipartFile> imageFiles, MultipartFile mainProductImage);

    ProductInfo updateProductInfo(String updatedProdInfoJsonString, MultipartFile mainProductImageFile, List<MultipartFile> additionalProductImageFiles);

    void deleteProduct(int prodId);

    ProductInfo getProductInfo(int prodId);

    Product getProduct(int prodId);

    Stock getProductStock(int prodId);

    Category getProductCategory(int categoryId);

    List<Image> getProductImages(int prodId, boolean productMainImageOnly);

    ProductInfoList getAllProducts();

    CategoryList getAllCategories();

    ProductInfoList getAllProductsByCategoryId(int categoryId);

    ProductInfoList getAllProductsContainingName(String name, int categoryId);

    Product saveProduct(Product product);

    Stock saveStock(int productId, Stock stock);

    List<Image> saveAdditionalImages(int productId, List<MultipartFile> imageFiles);

    Image saveMainProductImage(int productId, MultipartFile mainProductImageFile);

    void updateMainProductImage(int productId, MultipartFile mainProductImageFile);

    void updateProduct(Product product);

    void updateStock(Stock stock);

    List<ProductInfo> populateProductInfoList(List<Product> products);


}
