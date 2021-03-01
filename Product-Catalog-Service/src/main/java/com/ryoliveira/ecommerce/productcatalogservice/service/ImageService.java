package com.ryoliveira.ecommerce.productcatalogservice.service;

import org.springframework.web.multipart.MultipartFile;

import com.ryoliveira.ecommerce.productcatalogservice.model.Image;

public interface ImageService {
	
	Image createImageObject(int productId, MultipartFile imageFile);

}
