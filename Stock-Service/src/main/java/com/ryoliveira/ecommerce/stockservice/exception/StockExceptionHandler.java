package com.ryoliveira.ecommerce.stockservice.exception;

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
public class StockExceptionHandler extends ResponseEntityExceptionHandler{
	
	Logger EXCEPTION_LOGGER = LoggerFactory.getLogger(StockExceptionHandler.class);
	
	private final String INCORRECT_REQUEST = "INCORRECT_REQUEST";
	
	@ExceptionHandler(StockNotFoundException.class)
	public final ResponseEntity<ErrorResponse> handleStockNotFoundException(StockNotFoundException ex, WebRequest request){
		List<String> details = new ArrayList<>();
		details.add(ex.getLocalizedMessage());
		EXCEPTION_LOGGER.error(ex.getMessage());
		ErrorResponse error = new ErrorResponse(INCORRECT_REQUEST, details);
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}

}
