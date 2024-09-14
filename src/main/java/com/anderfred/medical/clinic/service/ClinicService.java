package com.anderfred.medical.clinic.service;

import com.anderfred.medical.clinic.domain.clinic.Clinic;

public interface ClinicService {
  Clinic findClinicById(Long id);
  Clinic updateClinic(Clinic clinic);
}
