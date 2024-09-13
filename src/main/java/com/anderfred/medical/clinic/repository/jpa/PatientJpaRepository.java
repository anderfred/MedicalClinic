package com.anderfred.medical.clinic.repository.jpa;

import com.anderfred.medical.clinic.domain.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientJpaRepository extends JpaRepository<Doctor, Long> {}
