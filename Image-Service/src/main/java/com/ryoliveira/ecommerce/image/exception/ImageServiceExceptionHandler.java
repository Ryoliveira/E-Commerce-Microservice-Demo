package com.ryoliveira.ecommerce.image.exception;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class ImageServiceExceptionHandler extends ResponseEntityExceptionHandler{

	Logger LOGGER = LoggerFactory.getLogger(ImageServiceExceptionHandler.class);
	
	private final String INCORRECT_REQUEST = "INCORRECT_REQUEST";

	@ExceptionHandler(ImageNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleImageNotFoundException(ImageNotFoundException ex, WebRequest request){
		List<String> details = new ArrayList<>();
		details.add(ex.getLocalizedMessage());
		ErrorResponse error = new ErrorResponse(INCORRECT_REQUEST, details);
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}
	
}
