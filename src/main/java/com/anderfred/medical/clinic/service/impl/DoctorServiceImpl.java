package com.anderfred.medical.clinic.service.impl;

import static java.util.Objects.isNull;

import com.anderfred.medical.clinic.domain.audit.ActionType;
import com.anderfred.medical.clinic.domain.audit.EntityType;
import com.anderfred.medical.clinic.domain.user.Doctor;
import com.anderfred.medical.clinic.domain.user.UserState;
import com.anderfred.medical.clinic.exceptions.BaseException;
import com.anderfred.medical.clinic.exceptions.ClinicExceptionCode;
import com.anderfred.medical.clinic.repository.jpa.DoctorJpaRepository;
import com.anderfred.medical.clinic.service.DoctorService;
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
public class DoctorServiceImpl implements DoctorService {
  private static final Logger log = LoggerFactory.getLogger(DoctorServiceImpl.class);

  private final DoctorJpaRepository repository;
  private final PasswordEncoder passwordEncoder;
  private final ObjectMapper mapper;
  private final AuditService auditService;

  public DoctorServiceImpl(
      DoctorJpaRepository repository, PasswordEncoder passwordEncoder, ObjectMapper mapper, AuditService auditService) {
    this.repository = repository;
    this.passwordEncoder = passwordEncoder;
    this.mapper = mapper;
    this.auditService = auditService;
  }

  @Override
  @Transactional
  public Doctor registerDoctor(Doctor doctor) {
    log.debug("Request to register doctor:[{}]", doctor);
    String email = doctor.getEmail();
    repository
        .findByEmail(email)
        .ifPresent(
            found -> {
              log.error("Email:[{}] already exists", email);
              throw new BaseException("Email already exists", ClinicExceptionCode.INVALID_EMAIL);
            });
    doctor.setPassword(passwordEncoder.encode(doctor.getPassword()));
    Doctor persisted = repository.save(doctor);
    log.debug("Doctor:[{}], registered", persisted);
    auditService.createAuditRecord(EntityType.DOCTOR, ActionType.CREATE, persisted.getId());
    return persisted;
  }

  @Override
  @Transactional
  public Doctor updateDoctor(Doctor doctor) {
    log.debug("Request to update doctor:[{}]", doctor);
    if (isNull(doctor.getId())) {
      log.error("Doctor id is null");
      throw new BaseException("Doctor id is null");
    }
    Doctor persisted =
        repository
            .findById(doctor.getId())
            .orElseThrow(
                () -> new BaseException("Doctor not found", ClinicExceptionCode.ENTITY_NOT_FOUND));
    if (!doctor.getEmail().equals(persisted.getEmail())
        && repository.findByEmail(doctor.getEmail()).isPresent()) {
      log.error("Email:[{}] already exists", doctor.getEmail());
      throw new BaseException("Email already exists", ClinicExceptionCode.INVALID_EMAIL);
    }
    persisted.update(doctor);
    Doctor updated = repository.save(persisted);
    log.debug("Doctor:[{}], updated", updated);
    auditService.createAuditRecord(EntityType.DOCTOR, ActionType.UPDATE, persisted.getId());
    return updated;
  }

  @Override
  @Transactional
  public void deleteDoctor(Long id) {
    log.debug("Request to delete doctor:[{}]", id);
    if (isNull(id)) {
      log.error("Doctor id is null");
      throw new BaseException("Doctor id cannot be empty");
    }
    Doctor persisted =
        repository
            .findById(id)
            .orElseThrow(
                () -> new BaseException("Doctor not found", ClinicExceptionCode.ENTITY_NOT_FOUND));
    if (UserState.DELETED.equals(persisted.getState())) {
      log.error("Doctor already deleted");
      throw new BaseException("Doctor state already deleted", ClinicExceptionCode.INVALID_REQUEST);
    }
    persisted.delete();
    Doctor updated = repository.save(persisted);
    auditService.createAuditRecord(EntityType.DOCTOR, ActionType.DELETE, persisted.getId());
    log.debug("Doctor:[{}], deleted", updated);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<Doctor> findPage(Pageable pageable) {
    log.debug("Request to get Doctors page of:[{}]", pageable);
    Page<Doctor> list = repository.findPageNameSorted(pageable);
    log.debug("Found Doctors page:[{}]", list.getTotalElements());
    return list;
  }

  @Override
  @Transactional(readOnly = true)
  public Doctor findById(Long id) {
    log.debug("Request to get Patient:[{}]", id);
    Doctor doctor =
        repository
            .findById(id)
            .orElseThrow(
                () -> new BaseException("Doctor not found", ClinicExceptionCode.ENTITY_NOT_FOUND));
    log.debug("Found patient:[{}]", doctor);
    return doctor;
  }
}
