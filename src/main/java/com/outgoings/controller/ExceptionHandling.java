package com.outgoings.controller;

import com.outgoings.exception.ExceptionResponseEntity;
import com.outgoings.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandling {

    @ExceptionHandler
    protected ResponseEntity<ExceptionResponseEntity> userNotFoundException(UserNotFoundException exc){
        ExceptionResponseEntity response = new ExceptionResponseEntity();
        response.setMessage(exc.getMessage());
        response.setTimestamp(System.currentTimeMillis());
        response.setStatusCode(HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler

    ResponseEntity<ExceptionResponseEntity> otherException(NullPointerException exc){
        ExceptionResponseEntity response = new ExceptionResponseEntity();
        response.setMessage(exc.getMessage());
        response.setTimestamp(System.currentTimeMillis());
        response.setStatusCode(HttpStatus.BAD_REQUEST.value());
        exc.printStackTrace();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}

