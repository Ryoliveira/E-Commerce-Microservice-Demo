package com.ryoliveira.ecommerce.productcatalogservice.exception;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ProductInfoExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(NoSuchElementException.class)
	public final ResponseEntity<String> handleProductNotFoundException(NoSuchElementException e, WebRequest request){
		return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
	}
	

}
