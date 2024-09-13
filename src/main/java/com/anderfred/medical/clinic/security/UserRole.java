package com.anderfred.medical.clinic.security;

public enum UserRole {
  DOCTOR("DOCTOR_ROLE"),
  PATIENT("PATIENT_ROLE");
  private String description;

  UserRole(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

  public UserRole setDescription(String description) {
    this.description = description;
    return this;
  }
}
