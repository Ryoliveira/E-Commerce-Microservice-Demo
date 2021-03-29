package com.ryoliveira.ecommerce.productcatalogservice.util;

import java.io.IOException;
import java.util.Base64;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ryoliveira.ecommerce.productcatalogservice.model.Image;

@Service
public class ImageFileConverter {

	public Image createImageObject(int productId, MultipartFile imageFile, boolean isProductMainImage) {
		Image img = null;
		try {
			img = new Image(0, productId, isProductMainImage, imageFile.getOriginalFilename(), imageFile.getContentType(),
					Base64.getEncoder().encodeToString(imageFile.getBytes()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return img;
	}

}
