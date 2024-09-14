package com.anderfred.medical.clinic.domain;

public enum JasperTemplates {
  PATIENT_MEDICAL_EXAMS_REPORT("/templates/patient-medical-exams-report.jrxml");

  private String path;

  JasperTemplates(String path) {
    this.path = path;
  }

  public String getPath() {
    return path;
  }

  public JasperTemplates setPath(String path) {
    this.path = path;
    return this;
  }
}
