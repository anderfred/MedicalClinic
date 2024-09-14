package com.anderfred.medical.clinic.service;

import com.anderfred.medical.clinic.domain.user.Doctor;
import com.anderfred.medical.clinic.domain.user.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PatientService {
  Patient registerPatient(Patient patient);

  Patient updatePatient(Patient patient);

  void deletePatient(Long id);

  Page<Patient> findPage(Pageable pageable);

  Patient findById(Long id);
}
