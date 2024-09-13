package com.anderfred.medical.clinic.web.rest;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/auth")
public class AuthResource {
  private final Logger log = LoggerFactory.getLogger(AuthResource.class);

  // TODO
  @PostMapping("/doctor-login")
  public ResponseEntity<Void> doctorLogin(@RequestBody String payload) {
    StopWatch stopWatch = StopWatch.createStarted();
    log.debug("START | Request to doctor login by:[{}]", payload);
    log.debug("STOP | Doctor login request time:[{}]ms", stopWatch.getTime());
    return ResponseEntity.ok().build();
  }

  // TODO
  @PostMapping("/patient-login")
  public ResponseEntity<Void> patientLogin(@RequestBody String payload) {
    StopWatch stopWatch = StopWatch.createStarted();
    log.debug("START | Request to patient login by:[{}]", payload);
    log.debug("STOP | Patient login request time:[{}]ms", stopWatch.getTime());
    return ResponseEntity.ok().build();
  }
}
