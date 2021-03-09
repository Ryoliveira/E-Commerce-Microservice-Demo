package com.ryoliveira.ecommerce.image.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
	
	@GetMapping("/image/{imageId}")
	private ResponseEntity<Image> getImage(@PathVariable("imageId") int imageId) {
		return new ResponseEntity<Image>(imageService.getImage(imageId), HttpStatus.OK);
	}
	
	@PutMapping("/image/{productId}")
	private ResponseEntity<Image> updateImage(@RequestBody Image updatedImg, @PathVariable("productId") int productId) {
		return new ResponseEntity<Image>(imageService.updateImage(updatedImg, productId), HttpStatus.OK);
	}
	
	@DeleteMapping("/image/{imageId}")
	private ResponseEntity<String> deleteImage(@PathVariable("imageId") int imageId){
		imageService.deleteImage(imageId);
		return new ResponseEntity<String>("Image with id: " + imageId + " has been deleted", HttpStatus.OK);
	}
	
	@DeleteMapping("/images/{productId}")
	private ResponseEntity<String> deleteImagesWithProductId(@PathVariable("productId") int productId){
		imageService.deleteImagesWithProductId(productId);
		return new ResponseEntity<String>("Images with product id: " + productId + " have been deleted", HttpStatus.OK);
	}
	
	@GetMapping("/images/{productId}")
	private ResponseEntity<List<Image>> getProductImages(@PathVariable("productId") int productId){
		return new ResponseEntity<>(imageService.getAllProductImages(productId), HttpStatus.OK);
	}
	

}
