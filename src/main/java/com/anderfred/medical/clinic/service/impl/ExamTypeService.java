package com.anderfred.medical.clinic.service.impl;

import com.anderfred.medical.clinic.domain.ExamType;
import com.anderfred.medical.clinic.exceptions.BaseException;
import com.anderfred.medical.clinic.exceptions.ClinicExceptionCode;
import com.anderfred.medical.clinic.repository.jpa.ExamTypeJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExamTypeService {
  private final ExamTypeJpaRepository repository;

  public ExamTypeService(ExamTypeJpaRepository repository) {
    this.repository = repository;
  }

  @Transactional
  public ExamType create(ExamType examType) {
    return repository.save(examType);
  }

  @Transactional
  public ExamType update(ExamType examType) {
    return repository.save(examType);
  }

  @Transactional(readOnly = true)
  public ExamType findById(Long id) {
    return repository
        .findById(id)
        .orElseThrow(
            () -> new BaseException("Exam type not found", ClinicExceptionCode.ENTITY_NOT_FOUND));
  }
}
