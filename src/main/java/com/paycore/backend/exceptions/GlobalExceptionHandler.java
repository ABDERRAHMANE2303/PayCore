package com.paycore.backend.exceptions;


import com.paycore.backend.dtos.ApiError;
import com.paycore.backend.exceptions.custom.InactiveResourceException;
import com.paycore.backend.exceptions.custom.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler  {


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFoundResource(RuntimeException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiError(HttpStatus.NOT_FOUND.value(), e.getMessage()));
    }

    @ExceptionHandler(InactiveResourceException.class)
    public ResponseEntity<ApiError> handleInactiveResource(InactiveResourceException e){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiError(HttpStatus.CONFLICT.value(), e.getMessage()));
    }





}
