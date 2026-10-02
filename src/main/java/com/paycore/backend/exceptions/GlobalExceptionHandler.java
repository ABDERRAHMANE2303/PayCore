package com.paycore.backend.exceptions;


import com.paycore.backend.dtos.ApiError;
import com.paycore.backend.exceptions.custom.CustomerNotFoundException;
import com.paycore.backend.exceptions.custom.MerchantNotFoundException;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler  {


    @ExceptionHandler({CustomerNotFoundException.class, MerchantNotFoundException.class})
    public ResponseEntity<ApiError> HandleNotFound(RuntimeException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiError(HttpStatus.NOT_FOUND.value(), e.getMessage()));
    }





}
