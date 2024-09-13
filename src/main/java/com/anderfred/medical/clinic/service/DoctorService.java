package com.anderfred.medical.clinic.service;

import com.anderfred.medical.clinic.domain.Doctor;

public interface DoctorService {
  Doctor registerDoctor(Doctor doctor);

  Doctor updateDoctor(Doctor doctor);

  void deleteDoctor(Long id);
}
