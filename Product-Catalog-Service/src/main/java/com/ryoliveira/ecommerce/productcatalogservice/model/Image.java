package com.ryoliveira.ecommerce.productcatalogservice.model;

import lombok.Data;

@Data
public class Image {
	
	private int productId;
	private String fileName;
	private String fileType;
	private String image;

}
