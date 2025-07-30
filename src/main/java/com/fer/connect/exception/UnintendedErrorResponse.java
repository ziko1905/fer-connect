package com.fer.connect.exception;

import java.time.Instant;

public class UnintendedErrorResponse {
  private String path;
  private int status;
  private Instant timestamp;

  public UnintendedErrorResponse(String path, int status) {
    this.path = path;
    this.status = status;
    this.timestamp = Instant.now();
  }

  public UnintendedErrorResponse() {

  }

  public String getPath() {
    return path;
  }

  public int getStatus() {
    return status;
  }

  public Instant getTimestamp() {
    return timestamp;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public void setTimestamp(Instant timestamp) {
    this.timestamp = timestamp;
  }
}
