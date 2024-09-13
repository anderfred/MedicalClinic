package com.anderfred.medical.clinic.service.impl;

import static java.util.Objects.isNull;

import com.anderfred.medical.clinic.domain.Patient;
import com.anderfred.medical.clinic.domain.UserState;
import com.anderfred.medical.clinic.exceptions.BaseException;
import com.anderfred.medical.clinic.exceptions.ClinicExceptionCode;
import com.anderfred.medical.clinic.repository.jpa.PatientJpaRepository;
import com.anderfred.medical.clinic.service.PatientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PatientServiceImpl implements PatientService {
  private final Logger log = LoggerFactory.getLogger(PatientServiceImpl.class);

  private final PatientJpaRepository repository;
  private final PasswordEncoder passwordEncoder;

  public PatientServiceImpl(PatientJpaRepository repository, PasswordEncoder passwordEncoder) {
    this.repository = repository;
    this.passwordEncoder = passwordEncoder;
  }

  // TODO Add check on only doctor is able to use apis

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
    persisted.delete();
    Patient updated = repository.save(persisted);
    log.debug("Patient:[{}], deleted", updated);
  }
}
