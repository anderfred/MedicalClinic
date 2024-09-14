package com.anderfred.medical.clinic.service;

import com.anderfred.medical.clinic.domain.user.Patient;

public interface PatientService {
  Patient registerPatient(Patient patient);

  Patient updatePatient(Patient patient);

  void deletePatient(Long id);
}
