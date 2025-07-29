package com.fer.connect.exception;

import java.time.Instant;

public abstract class RestException extends RuntimeException {
  private final Instant exceptionTimestamp;
  private final RestErrorType restErrorType;

  public RestException(RestErrorType restErrorType) {
    super(restErrorType.getMessage());
    this.restErrorType = restErrorType;
    this.exceptionTimestamp = Instant.now();
  }

  public Instant getExcpetionTimestamp() {
    return exceptionTimestamp;
  }

  public String getMessage() {
    return restErrorType.getMessage();
  }

  public String getErrorCode() {
    return restErrorType.getErrorCode();
  }

  public int getHttpStatusCode() {
    return restErrorType.getHttpStatusCode();
  }
}
