package com.anderfred.medical.clinic.service.impl;

import static java.util.Objects.isNull;

import com.anderfred.medical.clinic.domain.audit.ActionType;
import com.anderfred.medical.clinic.domain.audit.EntityType;
import com.anderfred.medical.clinic.domain.user.Patient;
import com.anderfred.medical.clinic.domain.user.UserState;
import com.anderfred.medical.clinic.exceptions.BaseException;
import com.anderfred.medical.clinic.exceptions.ClinicExceptionCode;
import com.anderfred.medical.clinic.repository.jpa.AppointmentJpaRepository;
import com.anderfred.medical.clinic.repository.jpa.PatientJpaRepository;
import com.anderfred.medical.clinic.service.PatientService;
import com.anderfred.medical.clinic.util.MappingUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PatientServiceImpl implements PatientService {
  private static final Logger log = LoggerFactory.getLogger(PatientServiceImpl.class);

  private final PatientJpaRepository repository;
  private final PasswordEncoder passwordEncoder;
  private final AppointmentJpaRepository appointmentJpaRepository;
  private final AuditService auditService;

  public PatientServiceImpl(
      PatientJpaRepository repository,
      PasswordEncoder passwordEncoder,
      AppointmentJpaRepository appointmentJpaRepository,
      AuditService auditService) {
    this.repository = repository;
    this.passwordEncoder = passwordEncoder;
    this.appointmentJpaRepository = appointmentJpaRepository;
    this.auditService = auditService;
  }

  @Override
  @Transactional
  public Patient registerPatient(Patient patient) {
    log.debug("Request to register patient:[{}]", patient);
    String email = patient.getEmail();
    repository
        .findByEmail(email)
        .ifPresent(
            found -> {
              log.error("Email:[{}] already exists", email);
              throw new BaseException("Email already exists", ClinicExceptionCode.INVALID_EMAIL);
            });
    patient.setPassword(passwordEncoder.encode(patient.getPassword()));
    Patient persisted = repository.save(patient);
    log.debug("Patient:[{}], registered", persisted);
    auditService.createAuditRecord(EntityType.PATIENT, ActionType.CREATE, persisted.getId());
    return persisted;
  }

  @Override
  @Transactional
  public Patient updatePatient(Patient patient) {
    log.debug("Request to update patient:[{}]", patient);
    if (isNull(patient.getId())) {
      log.error("Patient id is null");
      throw new BaseException("Patient id is null");
    }
    Patient persisted =
        repository
            .findById(patient.getId())
            .orElseThrow(
                () -> new BaseException("Patient not found", ClinicExceptionCode.ENTITY_NOT_FOUND));
    if (!patient.getEmail().equals(persisted.getEmail())
        && repository.findByEmail(patient.getEmail()).isPresent()) {
      log.error("Email:[{}] already exists", patient.getEmail());
      throw new BaseException("Email already exists", ClinicExceptionCode.INVALID_EMAIL);
    }
    persisted.update(patient);
    Patient updated = repository.save(persisted);
    log.debug("Patient:[{}], updated", updated);
    auditService.createAuditRecord(EntityType.PATIENT, ActionType.UPDATE, persisted.getId());
    return updated;
  }

  @Override
  @Transactional
  public void deletePatient(Long id) {
    log.debug("Request to delete patient:[{}]", id);
    if (isNull(id)) {
      log.error("Patient id is null");
      throw new BaseException("Patient id cannot be empty");
    }
    Patient persisted =
        repository
            .findById(id)
            .orElseThrow(
                () -> new BaseException("Patient not found", ClinicExceptionCode.ENTITY_NOT_FOUND));
    if (UserState.DELETED.equals(persisted.getState())) {
      log.error("Patient already deleted");
      throw new BaseException("Patient state already deleted", ClinicExceptionCode.INVALID_REQUEST);
    }
    boolean hardDelete = appointmentJpaRepository.findIdsByPatientId(persisted.getId()).isEmpty();
    if (hardDelete) {
      log.warn("Patient:[{}], will be deleted completely", persisted);
      repository.delete(persisted);
    } else {
      persisted.delete();
      Patient updated = repository.save(persisted);
      log.debug("Patient:[{}], deleted", updated);
    }
    auditService.createAuditRecord(EntityType.PATIENT, ActionType.DELETE, persisted.getId());
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Patient> findPage(Pageable pageable) {
    log.debug("Request to get Doctors page of:[{}]", pageable);
    Page<Patient> list = repository.findPageNameSorted(pageable);
    log.debug("Found Doctors page:[{}]", list.getTotalElements());
    return list;
  }

  @Override
  @Transactional(readOnly = true)
  public Patient findById(Long id) {
    log.debug("Request to get Patient:[{}]", id);
    Patient patient =
        repository
            .findById(id)
            .orElseThrow(
                () -> new BaseException("Patient not found", ClinicExceptionCode.ENTITY_NOT_FOUND));
    log.debug("Found patient:[{}]", patient);
    return patient;
  }
}
