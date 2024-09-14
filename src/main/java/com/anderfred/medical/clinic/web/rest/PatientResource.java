package com.anderfred.medical.clinic.web.rest;

import com.anderfred.medical.clinic.domain.user.Patient;
import com.anderfred.medical.clinic.service.PatientService;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
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
  public ResponseEntity<Patient> registerPatient(@RequestBody Patient patient) {
    StopWatch stopWatch = StopWatch.createStarted();
    log.debug("START | Request to register patient by:[{}]", patient);
    Patient created = patientService.registerPatient(patient);
    log.debug("STOP | Register request time:[{}]ms", stopWatch.getTime());
    return ResponseEntity.ok(created);
  }

  @PutMapping("/")
  public ResponseEntity<Patient> updatePatient(@RequestBody Patient patient) {
    StopWatch stopWatch = StopWatch.createStarted();
    log.debug("START | Request to update patient by:[{}]", patient);
    Patient updated = patientService.updatePatient(patient);
    log.debug("STOP | Update request time:[{}]ms", stopWatch.getTime());
    return ResponseEntity.ok(updated);
  }

  // TODO
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> updatePatient(@PathVariable(name = "id") Long id) {
    StopWatch stopWatch = StopWatch.createStarted();
    log.debug("START | Request to delete patient by:[{}]", id);
    log.debug("STOP | Delete request time:[{}]ms", stopWatch.getTime());
    return ResponseEntity.ok().build();
  }
}
