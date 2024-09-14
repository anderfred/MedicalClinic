package com.anderfred.medical.clinic.service.impl;

import com.anderfred.medical.clinic.domain.JasperTemplates;
import com.anderfred.medical.clinic.domain.MedicalExam;
import com.anderfred.medical.clinic.exceptions.BaseException;
import com.anderfred.medical.clinic.exceptions.ClinicExceptionCode;
import com.anderfred.medical.clinic.repository.jpa.MedicalExamJpaRepository;
import com.anderfred.medical.clinic.service.MedicalExamService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MedicalExamServiceImpl implements MedicalExamService {
  private static final Logger log = LoggerFactory.getLogger(MedicalExamServiceImpl.class);

  private final MedicalExamJpaRepository repository;
  private final PdfReportService pdfReportService;

  public MedicalExamServiceImpl(
      MedicalExamJpaRepository repository, PdfReportService pdfReportService) {
    this.repository = repository;
    this.pdfReportService = pdfReportService;
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

  @Override
  @Transactional(readOnly = true)
  public byte[] generateExamsPDF(Long patientId) {
    log.debug("Generating exams pdf for patient:[{}]", patientId);
    List<MedicalExam> exams = findAll(patientId);
    Map<String, Object> map = new HashMap<>();
    map.put("exams", exams);
    map.put("ReportTitle", "title");
    try {
      return pdfReportService.generatePdfReport(
          JasperTemplates.PATIENT_MEDICAL_EXAMS_REPORT.getPath(), map);
    } catch (JRException e) {
      e.printStackTrace();
      throw new BaseException("Error processing PDF", ClinicExceptionCode.INVALID_REQUEST);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
