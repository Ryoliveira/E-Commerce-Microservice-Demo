package com.ryoliveira.ecommerce.image.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ryoliveira.ecommerce.image.model.Image;
import com.ryoliveira.ecommerce.image.service.ImageService;

@RestController
public class ImageController {
	
	Logger LOGGER = LoggerFactory.getLogger(ImageController.class);
	
	@Autowired
	private ImageService imageService;
	
	@PostMapping("/image")
	private ResponseEntity<Image> saveImage(@RequestBody Image img) {
		return new ResponseEntity<Image>(imageService.saveImage(img), HttpStatus.OK);
	}
	
	@GetMapping("/image/{productId}")
	private Image getImage(@PathVariable("productId") int productId) {
		return imageService.getImage(productId);
	}
	
	@PutMapping("/image/{productId}")
	private Image updateImage(@RequestBody Image updatedImg, @PathVariable("productId") int productId) {
		return imageService.updateImage(updatedImg, productId);
	}
	

}
