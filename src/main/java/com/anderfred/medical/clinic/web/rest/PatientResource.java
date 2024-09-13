package com.anderfred.medical.clinic.web.rest;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/patient")
public class PatientResource {
  private final Logger log = LoggerFactory.getLogger(PatientResource.class);

  // TODO
  @PostMapping("/register")
  public ResponseEntity<Void> registerPatient(@RequestBody String payload) {
    StopWatch stopWatch = StopWatch.createStarted();
    log.debug("START | Request to register patient by:[{}]", payload);
    log.debug("STOP | Register request time:[{}]ms", stopWatch.getTime());
    return ResponseEntity.ok().build();
  }

  // TODO
  @PutMapping("/")
  public ResponseEntity<Void> updatePatient(@RequestBody String payload) {
    StopWatch stopWatch = StopWatch.createStarted();
    log.debug("START | Request to update patient by:[{}]", payload);
    log.debug("STOP | Update request time:[{}]ms", stopWatch.getTime());
    return ResponseEntity.ok().build();
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
