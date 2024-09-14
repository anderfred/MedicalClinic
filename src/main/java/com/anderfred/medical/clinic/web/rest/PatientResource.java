package com.anderfred.medical.clinic.web.rest;

import com.anderfred.medical.clinic.domain.user.Patient;
import com.anderfred.medical.clinic.service.PatientService;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/patient")
public class PatientResource {
  private final Logger log = LoggerFactory.getLogger(PatientResource.class);

  private final PatientService patientService;

  public PatientResource(PatientService patientService) {
    this.patientService = patientService;
  }

  @PostMapping("/register")
  @Secured("DOCTOR_ROLE")
  public ResponseEntity<Patient> registerPatient(@RequestBody Patient patient) {
    StopWatch stopWatch = StopWatch.createStarted();
    log.debug("START | Request to register patient by:[{}]", patient);
    Patient created = patientService.registerPatient(patient);
    log.debug("STOP | Register request time:[{}]ms", stopWatch.getTime());
    return ResponseEntity.ok(created);
  }

  @GetMapping("/{id}")
  @Secured("DOCTOR_ROLE")
  public ResponseEntity<Patient> getById(@PathVariable Long id) {
    StopWatch stopWatch = StopWatch.createStarted();
    log.debug("START | Request to get patient by id:[{}]", id);
    Patient created = patientService.findById(id);
    log.debug("STOP | Get patient by id time:[{}]ms", stopWatch.getTime());
    return ResponseEntity.ok(created);
  }

  @PutMapping("/")
  @Secured("DOCTOR_ROLE")
  public ResponseEntity<Patient> updatePatient(@RequestBody Patient patient) {
    StopWatch stopWatch = StopWatch.createStarted();
    log.debug("START | Request to update patient by:[{}]", patient);
    Patient updated = patientService.updatePatient(patient);
    log.debug("STOP | Update request time:[{}]ms", stopWatch.getTime());
    return ResponseEntity.ok(updated);
  }

  @DeleteMapping("/{id}")
  @Secured("DOCTOR_ROLE")
  public ResponseEntity<Void> deletePatient(@PathVariable(name = "id") Long id) {
    StopWatch stopWatch = StopWatch.createStarted();
    log.debug("START | Request to delete patient by:[{}]", id);
    patientService.deletePatient(id);
    log.debug("STOP | Delete request time:[{}]ms", stopWatch.getTime());
    return ResponseEntity.ok().build();
  }

  @GetMapping("/page")
  @Secured("DOCTOR_ROLE")
  public ResponseEntity<Page<Patient>> findPage(Pageable pageable) {
    StopWatch stopWatch = StopWatch.createStarted();
    log.debug("START | Request to get page:[{}] of Patients", pageable);
    Page<Patient> patients = patientService.findPage(pageable);
    log.debug(
        "STOP | Request to get patients page:[{}], time:[{}]ms",
        patients.getTotalElements(),
        stopWatch.getTime());
    return ResponseEntity.ok(patients);
  }
}
