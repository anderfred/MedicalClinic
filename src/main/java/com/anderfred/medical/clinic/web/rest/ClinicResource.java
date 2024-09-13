package com.anderfred.medical.clinic.web.rest;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/clinic")
public class ClinicResource {
  private final Logger log = LoggerFactory.getLogger(ClinicResource.class);

  // TODO get clinic info

  // TODO edit info

  @GetMapping("/test")
  public ResponseEntity<Void> doctorLogin() {
    StopWatch stopWatch = StopWatch.createStarted();
    log.debug("START | test to doctor login");
    return ResponseEntity.ok().build();
  }
}
