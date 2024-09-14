package com.anderfred.medical.clinic.repository.jpa;

import com.anderfred.medical.clinic.domain.clinic.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClinicJpaRepository extends JpaRepository<Clinic, Long> {}
