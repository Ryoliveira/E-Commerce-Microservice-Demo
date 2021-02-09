package com.ryoliveira.ecommerce.image.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ryoliveira.ecommerce.image.model.Image;
import com.ryoliveira.ecommerce.image.service.ImageService;

@RestController
public class ImageController {
	
	@Autowired
	private ImageService imageService;
	
	@PostMapping("/image/{productId}")
	private Image saveImage(@RequestParam("file") MultipartFile file, @PathVariable("productId") int productId) {
		return imageService.saveImage(file, productId);
	}
	
	@GetMapping("/image/{productId}")
	private Image getImage(@PathVariable("productId") int productId) {
		return imageService.getImage(productId);
	}
	
	@PutMapping("/image/{productId}")
	private Image updateImage(@RequestParam("file") MultipartFile file, @PathVariable("productId") int productId) {
		return imageService.updateImage(file, productId);
	}
	

}
