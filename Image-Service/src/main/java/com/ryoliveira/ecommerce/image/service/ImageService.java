package com.ryoliveira.ecommerce.image.service;

import org.springframework.web.multipart.MultipartFile;

import com.ryoliveira.ecommerce.image.model.Image;

public interface ImageService {
	
	Image saveImage(MultipartFile file, int productId);
	
	Image getImage(int productId);
	
	Image updateImage(MultipartFile file, int productId);
	

}
