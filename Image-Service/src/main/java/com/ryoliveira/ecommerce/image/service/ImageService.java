package com.ryoliveira.ecommerce.image.service;

import java.util.List;

import com.ryoliveira.ecommerce.image.model.Image;

public interface ImageService {
	
	Image saveImage(Image img);
	
	Image getImage(int productId);
	
	Image updateImage(Image img, int imageId);
	
	void deleteImage(int imageId);
	
	void deleteImagesByIds(List<Integer> imageIds);
	
	void deleteImagesWithProductId(int productId);
	
	List<Image> getProductImages(int productId, boolean productMainImageOnly);
	

	

}
