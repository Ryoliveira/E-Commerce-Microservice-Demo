package com.ryoliveira.ecommerce.productservice.exception;

import lombok.Data;

import java.util.List;


@Data
public class ErrorResponse {

    private String message;
    private List<String> details;

    public ErrorResponse(String message, List<String> details) {
        super();
        this.message = message;
        this.details = details;
    }


}
