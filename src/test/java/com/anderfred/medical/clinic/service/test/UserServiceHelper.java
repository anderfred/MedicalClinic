package com.anderfred.medical.clinic.service.test;

import com.anderfred.medical.clinic.domain.user.Doctor;
import com.anderfred.medical.clinic.domain.user.Patient;
import com.anderfred.medical.clinic.service.DoctorService;
import com.anderfred.medical.clinic.service.DoctorServiceIT;
import com.anderfred.medical.clinic.service.PatientService;
import com.anderfred.medical.clinic.service.PatientServiceIT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceHelper {
  @Autowired DoctorService doctorService;

  @Autowired PatientService patientService;

  @Transactional
  public Doctor createPersistedDoctor() {
    Doctor doctor = DoctorServiceIT.generateDoctor();
    return doctorService.registerDoctor(doctor);
  }

  @Transactional
  public Patient createPersistedPatient() {
    Patient patient = PatientServiceIT.generatePatient();
    return patientService.registerPatient(patient);
  }
}
