package com.fer.connect.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;

import com.fasterxml.jackson.core.JsonProcessingException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(RestException.class)
  public ResponseEntity<?> handleExceptions(RestException restException, ServletWebRequest request)
      throws JsonProcessingException {

    String path = request.getRequest().getRequestURI();
    IntendedErrorResponse responseJson = new IntendedErrorResponse(restException, path);

    return ResponseEntity.status(restException.getHttpStatusCode()).body(responseJson);
  }
}
