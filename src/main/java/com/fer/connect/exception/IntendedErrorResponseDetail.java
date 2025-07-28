package com.fer.connect.exception;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IntendedErrorResponseDetail {
  private final String message;
  @JsonProperty("code")
  private final String errorCode;

  public IntendedErrorResponseDetail(String errorCode, String message) {
    this.errorCode = errorCode;
    this.message = message;
  }

  public String getErrorCode() {
    return errorCode;
  }

  public String getMessage() {
    return message;
  }
}
