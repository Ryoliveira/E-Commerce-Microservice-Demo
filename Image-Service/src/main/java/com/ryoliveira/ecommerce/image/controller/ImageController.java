package com.ryoliveira.ecommerce.image.controller;

import com.ryoliveira.ecommerce.image.model.Image;
import com.ryoliveira.ecommerce.image.service.ImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ImageController {

    @Autowired
    private ImageService imageService;

    @PostMapping(path = "/image")
    private ResponseEntity<Image> saveImage(@RequestBody Image img) {
        return new ResponseEntity<Image>(imageService.saveImage(img), HttpStatus.OK);
    }

    @GetMapping(path = "/image/{imageId}")
    private ResponseEntity<Image> getImage(@PathVariable("imageId") int imageId) {
        return new ResponseEntity<Image>(imageService.getImage(imageId), HttpStatus.OK);
    }

    @PutMapping(path = "/image/{productId}")
    private ResponseEntity<Image> updateProductMainImage(@RequestBody Image updatedImg, @PathVariable("productId") int productId) {
        return new ResponseEntity<Image>(imageService.updateImage(updatedImg, productId), HttpStatus.OK);
    }

    @DeleteMapping(path = "/image/{imageId}")
    private ResponseEntity<String> deleteImage(@PathVariable("imageId") int imageId) {
        imageService.deleteImage(imageId);
        return new ResponseEntity<String>("Image with id: " + imageId + " has been deleted", HttpStatus.OK);
    }

    @DeleteMapping(path = "/images/{productId}")
    private ResponseEntity<String> deleteImagesWithProductId(@PathVariable("productId") int productId) {
        imageService.deleteImagesWithProductId(productId);
        return new ResponseEntity<String>("Images with product id: " + productId + " have been deleted", HttpStatus.OK);
    }

    @GetMapping(path = "/images/{productId}")
    private ResponseEntity<List<Image>> getProductImages(@PathVariable("productId") int productId, @RequestParam("productMainImageOnly") boolean productMainImageOnly) {
        return new ResponseEntity<>(imageService.getProductImages(productId, productMainImageOnly), HttpStatus.OK);
    }

    @PutMapping(path = "/images")
    private ResponseEntity<String> deleteImagesById(@RequestBody List<Integer> imageIdsToDelete) {
        imageService.deleteImagesByIds(imageIdsToDelete);
        return new ResponseEntity<String>("", HttpStatus.OK);
    }

}
