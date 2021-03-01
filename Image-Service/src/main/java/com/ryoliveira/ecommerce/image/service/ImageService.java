package com.ryoliveira.ecommerce.image.service;

import com.ryoliveira.ecommerce.image.model.Image;

public interface ImageService {
	
	Image saveImage(Image img);
	
	Image getImage(int productId);
	
	Image updateImage(Image img, int productId);
	

}
