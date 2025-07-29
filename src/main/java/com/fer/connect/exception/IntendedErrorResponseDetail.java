package com.fer.connect.exception;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IntendedErrorResponseDetail {
  private String message;
  @JsonProperty("code")
  private String errorCode;

  public IntendedErrorResponseDetail(String errorCode, String message) {
    this.errorCode = errorCode;
    this.message = message;
  }

  public IntendedErrorResponseDetail() {

  }

  public String getErrorCode() {
    return errorCode;
  }

  public String getMessage() {
    return message;
  }
}
