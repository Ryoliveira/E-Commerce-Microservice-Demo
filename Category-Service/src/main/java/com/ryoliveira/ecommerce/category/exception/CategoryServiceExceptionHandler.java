package com.ryoliveira.ecommerce.category.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

public class CategoryServiceExceptionHandler extends ResponseEntityExceptionHandler {


    private final String INCORRECT_REQUEST = "INCORRECT_REQUEST";

    @ExceptionHandler(CategoryNotFoundException.class)
    private ResponseEntity<ErrorResponse> handleNoCategoryFoundException(CategoryNotFoundException ex, WebRequest request){
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse errorResponse = new ErrorResponse(INCORRECT_REQUEST, details);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);

    }



}
