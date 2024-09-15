package com.anderfred.medical.clinic.web.rest;

import com.anderfred.medical.clinic.domain.Appointment;
import com.anderfred.medical.clinic.service.AppointmentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/appointment")
public class AppointmentResource {
  private static final Logger log = LoggerFactory.getLogger(AppointmentResource.class);

  private final AppointmentService appointmentService;
  private final ObjectMapper objectMapper;

  public AppointmentResource(AppointmentService appointmentService, ObjectMapper objectMapper) {
    this.appointmentService = appointmentService;
    this.objectMapper = objectMapper;
  }

  @GetMapping("{id}")
  @Secured("DOCTOR_ROLE")
  public ResponseEntity<Appointment> getAppointment(@PathVariable Long id) {
    log.debug("REST request to get Appointment by id:[{}]", id);
    Appointment appointment = appointmentService.findAppointmentById(id);
    log.debug("Found appointment[{}]", appointment);
    return ResponseEntity.ok(appointment);
  }

  @PostMapping
  @Secured("DOCTOR_ROLE")
  public ResponseEntity<Appointment> createAppointment(
      @RequestBody Appointment appointment,
      @RequestParam(value = "patientId", required = true) Long patientId) {
    log.debug("REST request to create Appointment :[{}], patientId:[{}]", appointment, patientId);
    Appointment created = appointmentService.addAppointment(appointment, patientId);
    log.debug("Created Appointment [{}]", created);
    return ResponseEntity.ok(created);
  }

  @PutMapping
  @Secured("DOCTOR_ROLE")
  public ResponseEntity<Appointment> updateAppointment(@RequestBody Appointment appointment) throws JsonProcessingException {
    log.debug("REST request to update Appointment :[{}]", appointment);
    Appointment update = appointmentService.update(appointment);
    log.debug("Updated Appointment [{}]", update);
    String s = objectMapper.writeValueAsString(update);
    log.debug("Value:[{}]", s);
    return ResponseEntity.ok(update);
  }

  @GetMapping("/active")
  @Secured("DOCTOR_ROLE")
  public ResponseEntity<Page<Appointment>> getAppointmentsActive(Pageable pageable) {
    log.debug("REST request to getAppointmentsActive");
    Page<Appointment> activeAppointments = appointmentService.findActiveAppointments(pageable);
    log.debug("Active Appointments [{}]", activeAppointments);
    return ResponseEntity.ok(activeAppointments);
  }

  @DeleteMapping("/{id}")
  @Secured("DOCTOR_ROLE")
  public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
    log.debug("REST request to delete Appointment :[{}]", id);
    appointmentService.delete(id);
    log.debug("Appointment deleted");
    return ResponseEntity.ok().build();
  }
}
