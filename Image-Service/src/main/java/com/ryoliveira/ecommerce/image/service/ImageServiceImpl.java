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
	public Image saveImage(MultipartFile file, int productId) {
		Image img = null;
		try {
			img = new Image(productId, file.getOriginalFilename(), file.getContentType(),
					Base64.getEncoder().encodeToString(file.getBytes()));
			imageRepo.save(img);
			LOGGER.info("image has been saved with id:" + productId);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return img;
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
	public Image updateImage(MultipartFile file, int productId) throws ImageNotFoundException {
		Optional<Image> optional = imageRepo.findById(productId);
		Image img = new Image();
		try {
			img = optional.orElseThrow(
					() -> new ImageNotFoundException("No Image with product id:" + productId + " was found"));
			img.setFileName(file.getOriginalFilename());
			img.setFileType(file.getContentType());
			img.setImage(Base64.getEncoder().encodeToString(file.getBytes()));
			imageRepo.save(img);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return img;
	}

}
