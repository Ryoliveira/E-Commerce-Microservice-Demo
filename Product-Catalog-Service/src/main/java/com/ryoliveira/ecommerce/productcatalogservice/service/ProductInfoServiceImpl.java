package com.ryoliveira.ecommerce.productcatalogservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryoliveira.ecommerce.productcatalogservice.model.*;
import com.ryoliveira.ecommerce.productcatalogservice.util.ImageFileConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductInfoServiceImpl implements ProductInfoService {

    private final String PRODUCT_SERVICE = "PRODUCT-SERVICE";
    private final String STOCK_SERVICE = "STOCK-SERVICE";
    private final String CATEGORY_SERVICE = "CATEGORY-SERVICE";
    private final String IMAGE_SERVICE = "IMAGE-SERVICE";

    private final String productUrl = String.format("http://%s/product", PRODUCT_SERVICE);
    private final String allProductsUrl = String.format("http://%s/products", PRODUCT_SERVICE);
    private final String stockUrl = String.format("http://%s/stock", STOCK_SERVICE);
    private final String categoryUrl = String.format("http://%s/category", CATEGORY_SERVICE);
    private final String allCategoriesUrl = String.format("http://%s/categories", CATEGORY_SERVICE);
    private final String imageUrl = String.format("http://%s/image", IMAGE_SERVICE);
    private final String allProductImagesUrl = String.format("http://%s/images", IMAGE_SERVICE);

    Logger LOGGER = LoggerFactory.getLogger(ProductInfoServiceImpl.class);
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ImageFileConverter imageFileConverter;

    @Override
    public ProductInfo saveProductInfo(String productInfoJsonString, List<MultipartFile> imageFiles, MultipartFile mainProductImage) {
        ProductInfo productInfo = mapJsonToProductInfo(productInfoJsonString);
        Product product = saveProduct(productInfo.getProduct());
        Stock stock = saveStock(product.getId(), productInfo.getStock());
        Category category = getProductCategory(product.getCategoryId());

        int productId = product.getId();

        //Save main image
        Image mainImage = saveMainProductImage(productId, mainProductImage);
        //save additional images
        List<Image> productImages = saveAdditionalImages(productId, imageFiles);

        return new ProductInfo(product, stock, category, mainImage, productImages);
    }

    @Override
    public ProductInfo updateProductInfo(String updatedProdInfoJsonString, MultipartFile mainProductImageFile, List<MultipartFile> additionalProductImageFiles) throws NoSuchElementException {
        ProductInfo productInfo = mapJsonToProductInfo(updatedProdInfoJsonString);
        int productId = productInfo.getProduct().getId();
        try {
            updateProduct(productInfo.getProduct());
            updateStock(productInfo.getStock());
            //Update Image using image-service if not null
            if (mainProductImageFile != null) {
                updateMainProductImage(productId, mainProductImageFile);
            }
            //Add additional product images if not null
            if (additionalProductImageFiles != null && additionalProductImageFiles.size() != 0) {
                saveAdditionalImages(productId, additionalProductImageFiles);
            }
            return getProductInfo(productInfo.getProduct().getId());
        } catch (HttpStatusCodeException e) {
            throw new NoSuchElementException(e.getMessage());
        }
    }

    @Override
    public void deleteProduct(int prodId) throws NoSuchElementException {
        String deleteProductUrl = this.productUrl + "/" + prodId;
        String deleteStockUrl = this.stockUrl + "/" + prodId;
        String deleteImagesUrl = this.allProductImagesUrl + "/" + prodId;

        try {
            //Delete Product using product service
            ResponseEntity<String> productServiceResponse = restTemplate.exchange(deleteProductUrl, HttpMethod.DELETE, null, String.class);
            LOGGER.info("Product-Service: " + productServiceResponse.getBody() + " | Status Code: " + productServiceResponse.getStatusCode());

            //Delete stock using stock service
            ResponseEntity<String> stockServiceResponse = restTemplate.exchange(deleteStockUrl, HttpMethod.DELETE, null, String.class);
            LOGGER.info("Stock-Service: " + stockServiceResponse.getBody() + " | Status Code: " + stockServiceResponse.getStatusCode());

            //Delete images using image service
            ResponseEntity<String> imageServiceResponse = restTemplate.exchange(deleteImagesUrl, HttpMethod.DELETE, null, String.class);
            LOGGER.info("Image-Service: " + imageServiceResponse.getBody() + " | Status Code: " + imageServiceResponse.getStatusCode());
        } catch (HttpStatusCodeException e) {
            throw new NoSuchElementException(e.getMessage());
        }
    }

    @Override
    public ProductInfo getProductInfo(int prodId) throws NoSuchElementException {
        try {
            Product product = getProduct(prodId);
            Stock stock = getProductStock(prodId);
            Category category = getProductCategory(product.getCategoryId());
            Image mainProductImage = getProductImages(product.getId(), true).get(0);
            List<Image> images = getProductImages(product.getId(), false);
            return new ProductInfo(product, stock, category, mainProductImage, images);
        } catch (HttpStatusCodeException e) {
            throw new NoSuchElementException(e.getMessage());
        }

    }

    @Override
    public ProductInfoList getAllProducts() {

        List<ProductInfo> productInfoList = new ArrayList<>();

        ResponseEntity<List<Product>> responseEntity = restTemplate.exchange(this.allProductsUrl,
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Product>>() {
                });
        List<Product> products = responseEntity.getBody();

        //Populate list with product/stock/category info
        if (products != null) {
            productInfoList = populateProductInfoList(products);
        }
        return new ProductInfoList(productInfoList);
    }

    @Override
    public CategoryList getAllCategories() {
        return restTemplate.getForObject(this.allCategoriesUrl, CategoryList.class);
    }


    @Override
    public ProductInfoList getAllProductsByCategoryId(int categoryId) {
        List<ProductInfo> productInfoList = new ArrayList<>();

        ResponseEntity<List<Product>> responseEntity = restTemplate.exchange(this.allProductsUrl + "/" + categoryId,
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Product>>() {
                });
        List<Product> products = responseEntity.getBody();
        //Populate list with product/stock/category info
        if (products != null) {
            productInfoList = populateProductInfoList(products);
        }
        return new ProductInfoList(productInfoList);
    }


    @Override
    public Product getProduct(int prodId) {
        return restTemplate.getForObject(this.productUrl + "/" + prodId, Product.class);
    }

    @Override
    public Stock getProductStock(int prodId) {
        String productStockUrl = this.stockUrl + "/" + prodId;
        ResponseEntity<Stock> response = restTemplate.getForEntity(productStockUrl, Stock.class);
        LOGGER.info("Stock retrieval code: " + response.getStatusCode());
        return response.getBody();
    }


    @Override
    public Category getProductCategory(int categoryId) {
        return restTemplate.getForObject(this.categoryUrl + "/" + categoryId, Category.class);
    }

    @Override
    public List<Image> getProductImages(int prodId, boolean productMainImageOnly) {
        String url = UriComponentsBuilder.fromHttpUrl(this.allProductImagesUrl + "/" + prodId).queryParam("productMainImageOnly", productMainImageOnly).toUriString();
        ResponseEntity<List<Image>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Image>>() {
        });
        return response.getBody();
    }

    @Override
    public Product saveProduct(Product product) {
        ResponseEntity<Product> response = restTemplate.postForEntity(this.productUrl, product, Product.class);
        LOGGER.info("Saved Product status code: " + response.getStatusCodeValue());
        return response.getBody();
    }

    @Override
    public Stock saveStock(int productId, Stock stock) {
        stock.setProductId(productId);
        ResponseEntity<Stock> response = restTemplate.postForEntity(this.stockUrl, stock, Stock.class);
        LOGGER.info("Saved Stock status code: " + response.getStatusCodeValue());
        return response.getBody();
    }

    @Override
    public List<Image> saveAdditionalImages(int productId, List<MultipartFile> imageFiles) {
        List<Image> savedImages = new ArrayList<>();
        for (MultipartFile imageFile : imageFiles) {
            Image imgToBeSaved = imageFileConverter.createImageObject(productId, imageFile, false);
            HttpEntity<Image> httpEntity = new HttpEntity<Image>(imgToBeSaved, null);
            ResponseEntity<Image> imgResponse = restTemplate.exchange(this.imageUrl, HttpMethod.POST, httpEntity, Image.class);
            Image savedImage = imgResponse.getBody();
            savedImages.add(savedImage);
        }
        return savedImages;
    }

    @Override
    public Image saveMainProductImage(int productId, MultipartFile mainProductImageFile) {
        Image mainProductImage = imageFileConverter.createImageObject(productId, mainProductImageFile, true);
        HttpEntity<Image> mainImageEntity = new HttpEntity<>(mainProductImage, null);
        ResponseEntity<Image> mainImageSaveResponse = restTemplate.exchange(this.imageUrl, HttpMethod.POST, mainImageEntity, Image.class);
        return mainImageSaveResponse.getBody();
    }

    @Override
    public ProductInfo mapJsonToProductInfo(String json) {
        ProductInfo productInfo = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            productInfo = mapper.readValue(json, ProductInfo.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return productInfo;
    }

    @Override
    public void updateMainProductImage(int productId, MultipartFile mainProductImageFile) {
        Image updatedImg = imageFileConverter.createImageObject(productId, mainProductImageFile, true);
        HttpEntity<Image> imageEntity = new HttpEntity<>(updatedImg, null);
        ResponseEntity<Image> imageServiceResponse = restTemplate.exchange(this.imageUrl + "/" + productId, HttpMethod.PUT, imageEntity, Image.class);
        LOGGER.info("Image-Service Response: Code -- " + imageServiceResponse.getStatusCodeValue() + " | Updated Image: File Name --" + imageServiceResponse.getBody().getFileName() + " | File Type: " + imageServiceResponse.getBody().getFileType());
    }

    @Override
    public void updateProduct(Product product) {
        HttpEntity<Product> productEntity = new HttpEntity<>(product, null);
        ResponseEntity<Product> productServiceResponse = restTemplate.exchange(this.productUrl, HttpMethod.PUT, productEntity, Product.class);
        LOGGER.info("Product-Service Response: Code -- " + productServiceResponse.getStatusCodeValue() + " | Updated Product: " + productServiceResponse.getBody().toString());
    }

    @Override
    public void updateStock(Stock stock) {
        HttpEntity<Stock> stockEntity = new HttpEntity<>(stock, null);
        ResponseEntity<Stock> stockServiceResponse = restTemplate.exchange(this.stockUrl, HttpMethod.PUT, stockEntity, Stock.class);
        LOGGER.info("Stock-Service Response: Code -- " + stockServiceResponse.getStatusCodeValue() + " | Updated Stock: " + stockServiceResponse.getBody().toString());
    }

    @Override
    public List<ProductInfo> populateProductInfoList(List<Product> products) {
        List<ProductInfo> productInfoList = new ArrayList<>();
        products.forEach(product -> {
            int productId = product.getId();
            Stock stock = getProductStock(productId);
            Category category = getProductCategory(product.getCategoryId());
            Image mainProductImage = getProductImages(productId, true).get(0);
            List<Image> images = getProductImages(productId, false);
            productInfoList.add(new ProductInfo(product, stock, category, mainProductImage, images));
        });
        return productInfoList;
    }


}
