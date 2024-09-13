package com.anderfred.medical.clinic.web.rest;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/doctor")
public class DoctorResource {
  private final Logger log = LoggerFactory.getLogger(DoctorResource.class);

  // TODO
  @PostMapping("/register")
  public ResponseEntity<Void> registerDoctor(@RequestBody String payload) {
    StopWatch stopWatch = StopWatch.createStarted();
    log.debug("START | Request to register Doctor by:[{}]", payload);
    log.debug("STOP | Register request time:[{}]ms", stopWatch.getTime());
    return ResponseEntity.ok().build();
  }

  // TODO
  @PutMapping("/")
  public ResponseEntity<Void> updateDoctor(@RequestBody String payload) {
    StopWatch stopWatch = StopWatch.createStarted();
    log.debug("START | Request to update Doctor by:[{}]", payload);
    log.debug("STOP | Update request time:[{}]ms", stopWatch.getTime());
    return ResponseEntity.ok().build();
  }

  // TODO
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> updateDoctor(@PathVariable(name = "id") Long id) {
    StopWatch stopWatch = StopWatch.createStarted();
    log.debug("START | Request to delete Doctor by:[{}]", id);
    log.debug("STOP | Delete request time:[{}]ms", stopWatch.getTime());
    return ResponseEntity.ok().build();
  }
}
