package com.farhutdinov.springboot2.springboot2jpacrudexample.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDate;
import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {
  private LocalDate date = LocalDate.now();
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request){

        ErrorDetails errorDetails = new ErrorDetails(date,ex.getMessage(),request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?>  globalExceptionHandler(Exception ex,WebRequest request){
        ErrorDetails errorDetails = new ErrorDetails(date,ex.getMessage(),request.getDescription(false));
        return new ResponseEntity<>(errorDetails,HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
