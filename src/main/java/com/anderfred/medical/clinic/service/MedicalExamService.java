package com.anderfred.medical.clinic.service;

import com.anderfred.medical.clinic.domain.MedicalExam;

import java.util.List;

public interface MedicalExamService {
  List<MedicalExam> findAll(Long patientId);
  MedicalExam create(MedicalExam medicalExam);
  MedicalExam findById(Long id);
  MedicalExam update(MedicalExam medicalExam);
  void delete(Long id);
  byte[] generateExamsPDF(Long patientId);
}
