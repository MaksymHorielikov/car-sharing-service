package com.example.carsharingservice.controller;

import com.example.carsharingservice.exception.AuthenticationException;
import com.example.carsharingservice.exception.InvalidJwtAuthenticationException;
import com.example.carsharingservice.model.Response;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class DefaultAdvice {
    @ExceptionHandler({Exception.class, RuntimeException.class, AuthenticationException.class,
            InvalidJwtAuthenticationException.class})
    public ResponseEntity<Response> handleException(Exception e) {
        Response response = new Response(LocalDateTime.now().toString(),
                e.getMessage(),
                HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
