package com.anderfred.medical.clinic.exceptions;

public class ErrorResponse {
  private String message;
  private String details;

  public ErrorResponse(String message, String details) {
    this.message = message;
    this.details = details;
  }

  public String getMessage() {
    return message;
  }

  public ErrorResponse setMessage(String message) {
    this.message = message;
    return this;
  }

  public String getDetails() {
    return details;
  }

  public ErrorResponse setDetails(String details) {
    this.details = details;
    return this;
  }
}
