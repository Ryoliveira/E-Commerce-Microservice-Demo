package com.ryoliveira.ecommerce.image.exception;

public class ImageNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public ImageNotFoundException(String msg) {
		super(msg);
	}

}
