package com.anderfred.medical.clinic.service;

import com.anderfred.medical.clinic.domain.user.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DoctorService {
  Doctor registerDoctor(Doctor doctor);

  Doctor updateDoctor(Doctor doctor);

  void deleteDoctor(Long id);

  Page<Doctor> findPage(Pageable pageable);

  Doctor findById(Long id);
}
