package com.anderfred.medical.clinic.service;

import com.anderfred.medical.clinic.domain.MedicalExam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MedicalExamService {
  List<MedicalExam> findAll(Long patientId);
  MedicalExam create(MedicalExam medicalExam);
  MedicalExam findById(Long id);
  MedicalExam update(MedicalExam medicalExam);
  void delete(Long id);
}
