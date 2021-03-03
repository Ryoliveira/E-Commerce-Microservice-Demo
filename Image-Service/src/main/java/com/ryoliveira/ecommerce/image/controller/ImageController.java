package com.ryoliveira.ecommerce.image.controller;

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
	
	@Autowired
	private ImageService imageService;
	
	@PostMapping("/image")
	private ResponseEntity<Image> saveImage(@RequestBody Image img) {
		return new ResponseEntity<Image>(imageService.saveImage(img), HttpStatus.OK);
	}
	
	@GetMapping("/image/{productId}")
	private ResponseEntity<Image> getImage(@PathVariable("productId") int productId) {
		return new ResponseEntity<Image>(imageService.getImage(productId), HttpStatus.OK);
	}
	
	@PutMapping("/image/{productId}")
	private ResponseEntity<Image> updateImage(@RequestBody Image updatedImg, @PathVariable("productId") int productId) {
		return new ResponseEntity<Image>(imageService.updateImage(updatedImg, productId), HttpStatus.OK);
	}
	

}
