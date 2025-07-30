package com.fer.connect.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;

import com.fasterxml.jackson.core.JsonProcessingException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(RestException.class)
  public ResponseEntity<?> handleIntendedExceptions(RestException restException, ServletWebRequest request)
      throws JsonProcessingException {

    String path = request.getRequest().getRequestURI();
    IntendedErrorResponse responseJson = new IntendedErrorResponse(restException, path);

    return ResponseEntity.status(restException.getHttpStatusCode()).body(responseJson);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> handleUnintendedExceptions(
      Exception exception, ServletWebRequest request) {
    String path = request.getRequest().getRequestURI();
    UnintendedErrorResponse responseJson = new UnintendedErrorResponse(path, 400);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseJson);
  }
}
