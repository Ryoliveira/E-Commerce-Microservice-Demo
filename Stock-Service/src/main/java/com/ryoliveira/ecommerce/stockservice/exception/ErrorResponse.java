package com.ryoliveira.ecommerce.stockservice.exception;

import java.util.List;

import lombok.Data;

@Data
public class ErrorResponse {
	
	public ErrorResponse(String message, List<String> details) {
        super();
        this.message = message;
        this.details = details;
    }
  
    private String message;
    private List<String> details;
 
}
