package com.ryoliveira.ecommerce.image.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ryoliveira.ecommerce.image.dao.ImageRepository;
import com.ryoliveira.ecommerce.image.exception.ImageNotFoundException;
import com.ryoliveira.ecommerce.image.model.Image;

@Service
public class ImageServiceImpl implements ImageService {

	Logger LOGGER = LoggerFactory.getLogger(ImageServiceImpl.class);

	@Autowired
	private ImageRepository imageRepo;

	@Override
	public Image saveImage(Image img) {
		Image savedImage = imageRepo.save(img);
		LOGGER.info(String.format("Saved Photo ==== | Filename: %s | File Type: %s | Main Product Image: %b", savedImage.getFileName(), savedImage.getFileType(), savedImage.isProductMainImage()));
		return savedImage;
	}

	@Override
	public Image getImage(int imageId) {
		Optional<Image> optional = imageRepo.findById(imageId);
		Image img = optional
				.orElseThrow(() -> new ImageNotFoundException("No Image with id:" + imageId + " was found"));
		LOGGER.info(String.format("Pulling Photo with id: %d | Filename: %s | File Type: %s", img.getProductId(), img.getFileName(), img.getFileType()));
		return img;
	}

	@Override
	public Image updateImage(Image updatedImg, int imageId) throws ImageNotFoundException {
		Optional<Image> optional = imageRepo.findById(imageId);
		Image img = new Image();

		img = optional
				.orElseThrow(() -> new ImageNotFoundException("No Image with product id:" + imageId + " was found"));
		//Log original img data
		LOGGER.info(String.format("Original Image Data ==== Filename: %s | File Type: %s", img.getFileName(), img.getFileType()));
		
		//Update img data
		img.setFileName(updatedImg.getFileName());
		img.setFileType(updatedImg.getFileType());
		img.setImage(updatedImg.getImage());
		imageRepo.save(img);
		
		//Log updated img data
		LOGGER.info(String.format("Updated Image Data ==== Filename: %s | File Type: %s", img.getFileName(), img.getFileType()));
		return img;
	}
	
	@Override
	public void deleteImage(int imageId) {
		imageRepo.deleteByProductId(imageId);
		LOGGER.info(String.format("Delete image with id: %d", imageId));
	}

	@Override
	public void deleteImagesWithProductId(int productId) {
		List<Image> deletedImages = imageRepo.deleteByProductId(productId);
		
		deletedImages.stream().forEach(image -> LOGGER.info("Deleted image with id: " + image.getId() + " -- " + image.getFileName()));
		
		LOGGER.info(String.format("Deleted images with product id: %d", productId));
	}

	@Override
	public List<Image> getProductImages(int productId, boolean productMainImageOnly) throws ImageNotFoundException {
		Optional<List<Image>> optionalList = imageRepo.findByProductIdAndIsProductMainImage(productId, productMainImageOnly);
		List<Image> productImages = optionalList.orElseThrow(()->  new ImageNotFoundException("No Images with product id:" + productId + " was found"));
		productImages.stream().forEach(productImage -> {
			LOGGER.info(String.format("Pulled Photo ==== | Filename: %s | File Type: %s | Main Product Image: %b", productImage.getFileName(), productImage.getFileType(), productImage.isProductMainImage()));
		});
		return productImages;
	}

}
