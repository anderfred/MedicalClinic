package com.anderfred.medical.clinic.repository.jpa;

import com.anderfred.medical.clinic.domain.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentJpaRepository extends JpaRepository<Appointment, Long> {}
