package com.anderfred.medical.clinic.web.rest;

import com.anderfred.medical.clinic.domain.MedicalExam;
import com.anderfred.medical.clinic.service.MedicalExamService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/medical-exam")
public class MedicalExamResource {
  private static final Logger log = LoggerFactory.getLogger(MedicalExamResource.class);

  private final MedicalExamService medicalExamService;

  public MedicalExamResource(MedicalExamService medicalExamService) {
    this.medicalExamService = medicalExamService;
  }

  // TODO pdf generation by jasper soft

/*  @GetMapping
  @Secured("PATIENT_ROLE")
  public ResponseEntity<Void> generatePDF() {}*/

  @GetMapping
  @Secured("PATIENT_ROLE")
  public ResponseEntity<List<MedicalExam>> findMedicalExams(
      @RequestParam("patientId") Long patientId) {
    log.debug("Request to get all exams by patient:[{}]", patientId);
    List<MedicalExam> all = medicalExamService.findAll(patientId);
    log.debug("Found:[{}]", all.size());
    return ResponseEntity.ok(all);
  }
}
