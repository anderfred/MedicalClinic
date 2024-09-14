package com.anderfred.medical.clinic.service;

import com.anderfred.medical.clinic.domain.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AppointmentService {
  Appointment addAppointment(Appointment appointment, Long patientId);

  Appointment update(Appointment appointment);

  void delete(Long id);

  Appointment findAppointmentById(Long id);

  Page<Appointment> findActiveAppointments(Pageable pageable);

  void closeAppointments();
}
