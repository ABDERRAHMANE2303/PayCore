package com.paycore.backend.exceptions;


import com.paycore.backend.dtos.responses.ApiErrorResponse;
import com.paycore.backend.exceptions.custom.InactiveResourceException;
import com.paycore.backend.exceptions.custom.InvalidPaymentStatusException;
import com.paycore.backend.exceptions.custom.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler  {


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFoundResource(ResourceNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage()));
    }

    @ExceptionHandler(InactiveResourceException.class)
    public ResponseEntity<ApiErrorResponse> handleInactiveResource(InactiveResourceException e){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiErrorResponse(HttpStatus.CONFLICT.value(), e.getMessage()));
    }


    @ExceptionHandler(InvalidPaymentStatusException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidPaymentStatus(InvalidPaymentStatusException e){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiErrorResponse(HttpStatus.CONFLICT.value(), e.getMessage()));
    }
}
