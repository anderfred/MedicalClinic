package com.anderfred.medical.clinic.exceptions;

public class AccessDeniedException extends BaseException {

  public AccessDeniedException(String message) {
    super(message);
  }

  public AccessDeniedException(String message, Throwable cause) {
    super(message, cause);
  }

  public AccessDeniedException(String message, ClinicExceptionCode code, Throwable cause) {
    super(message, code, cause);
  }

  public AccessDeniedException(String message, ClinicExceptionCode code) {
    super(message, code);
  }
}
