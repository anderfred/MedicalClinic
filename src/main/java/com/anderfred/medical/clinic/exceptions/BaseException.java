package com.anderfred.medical.clinic.exceptions;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class BaseException extends RuntimeException {
  private String message;
  private String code;

  public BaseException(String message) {
    super(message);
  }

  public BaseException(String message, Throwable cause) {
    super(message, cause);
  }

  public BaseException(String message, ClinicExceptionCode code, Throwable cause) {
    super(message, cause);
    this.code = code.name();
    this.message = message;
  }

  public BaseException(String message, ClinicExceptionCode code) {
    super(message);
    this.code = code.name();
    this.message = message;
  }

  public String getCode() {
    return code;
  }

  public BaseException setCode(String code) {
    this.code = code;
    return this;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
        .append("message", message)
        .append("code", code)
        .toString();
  }
}
