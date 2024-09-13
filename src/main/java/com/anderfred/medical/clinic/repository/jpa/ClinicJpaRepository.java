package com.anderfred.medical.clinic.repository.jpa;

import com.anderfred.medical.clinic.domain.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClinicJpaRepository extends JpaRepository<Clinic, Long> {}
