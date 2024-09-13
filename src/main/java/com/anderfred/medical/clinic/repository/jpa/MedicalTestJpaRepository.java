package com.anderfred.medical.clinic.repository.jpa;

import com.anderfred.medical.clinic.domain.MedicalTest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicalTestJpaRepository extends JpaRepository<MedicalTest, Long> {}
