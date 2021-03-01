package com.ryoliveira.ecommerce.image.service;

import java.io.IOException;
import java.util.Base64;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
		return imageRepo.save(img);
	}

	@Override
	public Image getImage(int productId) {
		Optional<Image> optional = imageRepo.findById(productId);
		Image img = new Image();
		img = optional
				.orElseThrow(() -> new ImageNotFoundException("No Image with product id:" + productId + " was found"));
		return img;
	}

	@Override
	public Image updateImage(Image updatedImg, int productId) throws ImageNotFoundException {
		Optional<Image> optional = imageRepo.findById(productId);
		Image img = new Image();

		img = optional
				.orElseThrow(() -> new ImageNotFoundException("No Image with product id:" + productId + " was found"));
		img.setFileName(updatedImg.getFileName());
		img.setFileType(updatedImg.getFileType());
		img.setImage(updatedImg.getImage());
		imageRepo.save(img);

		return img;
	}

}
