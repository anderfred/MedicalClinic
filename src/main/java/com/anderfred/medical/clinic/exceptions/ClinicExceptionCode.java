package com.anderfred.medical.clinic.exceptions;

public enum ClinicExceptionCode {
  MAPPING_ERROR("Mapping error"),
  INVALID_EMAIL("Invalid email address"),
  ENTITY_NOT_FOUND("Entity not found"),
  INVALID_REQUEST("Invalid request"),;

  private String code;

  ClinicExceptionCode(String code) {
    this.code = code;
  }

  public String getCode() {
    return code;
  }

  public ClinicExceptionCode setCode(String code) {
    this.code = code;
    return this;
  }
}
