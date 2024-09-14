package com.anderfred.medical.clinic.web.rest;

import com.anderfred.medical.clinic.domain.clinic.Clinic;
import com.anderfred.medical.clinic.service.ClinicService;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/clinic")
public class ClinicResource {
  private final Logger log = LoggerFactory.getLogger(ClinicResource.class);

  private final ClinicService clinicService;

  public ClinicResource(ClinicService clinicService) {
    this.clinicService = clinicService;
  }

  @GetMapping("/{id}")
  public ResponseEntity<Clinic> findClinicById(@PathVariable Long id) {
    StopWatch stopWatch = StopWatch.createStarted();
    log.debug("START | Request to get clinic info:[{}]", id);
    Clinic clinic = clinicService.findClinicById(id);
    log.debug("STOP | Request to get clinic info:[{}]. time:[{}]ms", clinic, stopWatch.getTime());
    return ResponseEntity.ok(clinic);
  }

  @PutMapping("/")
  public ResponseEntity<Clinic> updateClinic(@RequestBody Clinic clinic) {
    StopWatch stopWatch = StopWatch.createStarted();
    log.debug("START | Request to update clinic info:[{}]", clinic);
    Clinic updated = clinicService.updateClinic(clinic);
    log.debug(
        "STOP | Request to update clinic info:[{}]. time:[{}]ms", updated, stopWatch.getTime());
    return ResponseEntity.ok(updated);
  }
}
