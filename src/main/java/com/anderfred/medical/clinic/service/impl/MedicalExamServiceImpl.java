package com.anderfred.medical.clinic.service.impl;

import com.anderfred.medical.clinic.domain.MedicalExam;
import com.anderfred.medical.clinic.exceptions.BaseException;
import com.anderfred.medical.clinic.exceptions.ClinicExceptionCode;
import com.anderfred.medical.clinic.repository.jpa.MedicalExamJpaRepository;
import com.anderfred.medical.clinic.service.MedicalExamService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MedicalExamServiceImpl implements MedicalExamService {
  private final MedicalExamJpaRepository repository;

  public MedicalExamServiceImpl(MedicalExamJpaRepository repository) {
    this.repository = repository;
  }

  @Override
  public List<MedicalExam> findAll(Long patientId) {
    return repository.findAllByPatient(patientId);
  }

  @Override
  public MedicalExam create(MedicalExam medicalExam) {
    return repository.save(medicalExam);
  }

  @Override
  public MedicalExam findById(Long id) {
    return repository
        .findById(id)
        .orElseThrow(
            () -> new BaseException("Entity not found", ClinicExceptionCode.ENTITY_NOT_FOUND));
  }

  @Override
  public MedicalExam update(MedicalExam medicalExam) {
    return repository.save(medicalExam);
  }

  @Override
  public void delete(Long id) {
    repository.deleteById(id);
  }
}
