package com.fer.connect.exception;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IntendedErrorResponse {
  private String path;
  private Instant timestamp;
  private int status;
  @JsonProperty("error")
  private IntendedErrorResponseDetail intendedErrorResponseDetail;

  public IntendedErrorResponse(RestException restException, String path) {
    this.path = path;
    this.timestamp = restException.getExcpetionTimestamp();
    this.status = restException.getHttpStatusCode();
    this.intendedErrorResponseDetail = new IntendedErrorResponseDetail(restException.getErrorCode(),
        restException.getMessage());
  }

  public IntendedErrorResponse(String path, String timestamp, String status, String message, String errorCode) {
    this.path = path;
    this.timestamp = Instant.parse(timestamp);
    this.status = Integer.parseInt(status);
    this.intendedErrorResponseDetail = new IntendedErrorResponseDetail(errorCode, message);
  }

  public IntendedErrorResponse() {
  }

  public String getPath() {
    return path;
  }

  public Instant getTimestamp() {
    return timestamp;
  }

  public int getStatus() {
    return status;
  }

  public IntendedErrorResponseDetail getIntendedErrorResponseDetail() {
    return intendedErrorResponseDetail;
  }
}
