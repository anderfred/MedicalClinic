package com.anderfred.medical.clinic.service.impl;

import com.anderfred.medical.clinic.domain.Appointment;
import com.anderfred.medical.clinic.exceptions.BaseException;
import com.anderfred.medical.clinic.exceptions.ClinicExceptionCode;
import com.anderfred.medical.clinic.repository.jpa.AppointmentJpaRepository;
import com.anderfred.medical.clinic.repository.jpa.DoctorJpaRepository;
import com.anderfred.medical.clinic.security.CustomAuthenticationToken;
import com.anderfred.medical.clinic.service.AppointmentService;
import com.anderfred.medical.clinic.service.ClinicService;
import com.anderfred.medical.clinic.util.MappingUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AppointmentServiceImpl implements AppointmentService {
  private static final Logger log = LoggerFactory.getLogger(AppointmentServiceImpl.class);

  private final AppointmentJpaRepository repository;
  private final ClinicService clinicService;
  private final DoctorJpaRepository doctorJpaRepository;
  private final ObjectMapper mapper;

  public AppointmentServiceImpl(
      AppointmentJpaRepository repository,
      DoctorJpaRepository doctorJpaRepository,
      ObjectMapper mapper,
      ClinicService clinicService) {
    this.repository = repository;
    this.doctorJpaRepository = doctorJpaRepository;
    this.mapper = mapper;
    this.clinicService = clinicService;
  }

  @Override
  @Transactional
  public Appointment addAppointment(Appointment appointment, Long patientId) {
    log.debug("Request to create Appointment :[{}]", appointment);
    appointment.initBeforeCreate(
        extractCurrentDoctorId(), patientId, clinicService.getDefaultClinic());
    Appointment persisted = repository.save(appointment);
    Appointment copy = MappingUtil.copy(mapper, persisted);
    log.debug("Appointment created:[{}]", copy);
    return copy;
  }

  @Override
  @Transactional
  public Appointment update(Appointment appointment) {
    log.debug("Request to update Appointment :[{}]", appointment);
    Appointment persisted =
        repository
            .findFullById(appointment.getId())
            .orElseThrow(
                () -> new BaseException("Entity not found", ClinicExceptionCode.ENTITY_NOT_FOUND));
    persisted.update(appointment, extractCurrentDoctorId(), clinicService.getDefaultClinic());
    Appointment updated = repository.save(persisted);
    log.debug("Appointment updated:[{}]", updated);
    return updated;
  }

  @Override
  @Transactional
  public void delete(Long id) {
    log.debug("Request to delete Appointment :[{}]", id);
    Appointment appointment =
        repository
            .findById(id)
            .orElseThrow(
                () -> new BaseException("Entity not found", ClinicExceptionCode.ENTITY_NOT_FOUND));
    appointment.delete();
    repository.save(appointment);
    log.debug("Appointment deleted:[{}]", id);
  }

  @Override
  @Transactional(readOnly = true)
  public Appointment findAppointmentById(Long id) {
    log.debug("Request to get Appointment :[{}]", id);
    Appointment appointment =
        repository
            .findFullById(id)
            .orElseThrow(
                () -> new BaseException("Entity not found", ClinicExceptionCode.ENTITY_NOT_FOUND));
    log.debug("Appointment found:[{}]", appointment);
    return appointment;
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Appointment> findActiveAppointments(Pageable pageable) {
    log.debug("Request to get active appointments :[{}]", pageable);
    Page<Appointment> found = repository.findActivePage(pageable);
    log.debug("Appointments found:[{}]", found.getTotalElements());
    return found;
  }

  private Long extractCurrentDoctorId() {
    Long actorId =
        ((CustomAuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
            .getActorId();
    doctorJpaRepository
        .findById(actorId)
        .orElseThrow(
            () -> new BaseException("Entity not found", ClinicExceptionCode.ENTITY_NOT_FOUND));
    return actorId;
  }
}
