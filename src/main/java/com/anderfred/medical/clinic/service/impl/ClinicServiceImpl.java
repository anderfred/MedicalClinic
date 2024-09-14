package com.anderfred.medical.clinic.service.impl;

import static com.anderfred.medical.clinic.domain.clinic.Clinic.INITIAL_CLINIC_ID;
import static java.util.Objects.isNull;

import com.anderfred.medical.clinic.domain.clinic.Clinic;
import com.anderfred.medical.clinic.exceptions.BaseException;
import com.anderfred.medical.clinic.exceptions.ClinicExceptionCode;
import com.anderfred.medical.clinic.repository.jpa.ClinicJpaRepository;
import com.anderfred.medical.clinic.service.ClinicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClinicServiceImpl implements ClinicService {
  private final Logger log = LoggerFactory.getLogger(ClinicServiceImpl.class);

  private final ClinicJpaRepository repository;

  public ClinicServiceImpl(ClinicJpaRepository repository) {
    this.repository = repository;
  }

  @Override
  @Transactional(readOnly = true)
  public Clinic findClinicById(Long id) {
    log.debug("Request to get Clinic by id:[{}]", id);
    Clinic clinic =
        repository
            .findById(isNull(id) ? INITIAL_CLINIC_ID : id)
            .orElseThrow(
                () -> new BaseException("Clinic not found", ClinicExceptionCode.ENTITY_NOT_FOUND));
    log.debug("Found Clinic : {}", clinic);
    return clinic;
  }

  @Override
  @Transactional(readOnly = true)
  public Clinic getDefaultClinic() {
    return repository
        .findById(INITIAL_CLINIC_ID)
        .orElseThrow(
            () -> new BaseException("Clinic not found", ClinicExceptionCode.ENTITY_NOT_FOUND));
  }

  @Override
  @Transactional
  public Clinic updateClinic(Clinic updated) {
    log.debug("Request to update Clinic:[{}]", updated);
    if (isNull(updated.getId())) {
      log.warn("Clinic id is null, will be set to initial value");
      updated.setId(INITIAL_CLINIC_ID);
    }
    Clinic persisted =
        repository
            .findById(updated.getId())
            .orElseThrow(
                () -> new BaseException("Clinic not found", ClinicExceptionCode.ENTITY_NOT_FOUND));
    persisted.update(updated);
    Clinic saved = repository.save(persisted);
    log.debug("Updated Clinic:[{}]", saved);
    return saved;
  }
}
