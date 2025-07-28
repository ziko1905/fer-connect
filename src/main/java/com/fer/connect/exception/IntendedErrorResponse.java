package com.fer.connect.exception;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IntendedErrorResponse {
  private final String path;
  private final String timestamp;
  private final int status;
  @JsonProperty("error")
  private final IntendedErrorResponseDetail intendedErrorResponseDetail;

  public IntendedErrorResponse(RestException restException, String path) {
    this.path = path;
    this.timestamp = restException.getExcpetionTimestamp();
    this.status = restException.getHttpStatusCode();
    this.intendedErrorResponseDetail = new IntendedErrorResponseDetail(restException.getErrorCode(),
        restException.getMessage());
  }

  public String getPath() {
    return path;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public int getStatus() {
    return status;
  }

  public IntendedErrorResponseDetail getIntendedErrorResponseDetail() {
    return intendedErrorResponseDetail;
  }
}
