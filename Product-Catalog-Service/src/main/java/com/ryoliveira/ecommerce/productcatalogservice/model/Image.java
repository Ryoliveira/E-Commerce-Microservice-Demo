package com.ryoliveira.ecommerce.productcatalogservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Image {
	
	private int id;
	private int productId;
	private boolean isProductMainImage;
	private String fileName;
	private String fileType;
	private String image;

}
