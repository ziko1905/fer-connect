package com.fer.connect.exception;

import org.springframework.http.HttpStatus;

public enum RestErrorType {
  USER_EMAIL_EXISTS(
      "A user with that email already exists",
      "email_already_exists",
      HttpStatus.BAD_REQUEST);

  private final String message;
  private final String errorCode;
  private final HttpStatus httpStatus;

  RestErrorType(String defaultMessage, String defaultErrorCode, HttpStatus defaultHttpStatus) {
    this.message = defaultMessage;
    this.errorCode = defaultErrorCode;
    this.httpStatus = defaultHttpStatus;
  }

  public String getMessage() {
    return message;
  }

  public String getErrorCode() {
    return errorCode;
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }

  public int getHttpStatusCode() {
    return httpStatus.value();
  }
}
