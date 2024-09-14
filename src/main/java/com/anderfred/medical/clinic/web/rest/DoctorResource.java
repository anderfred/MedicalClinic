package com.anderfred.medical.clinic.web.rest;

import com.anderfred.medical.clinic.domain.user.Doctor;
import com.anderfred.medical.clinic.service.DoctorService;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/doctor")
public class DoctorResource {
  private final Logger log = LoggerFactory.getLogger(DoctorResource.class);

  private final DoctorService doctorService;

  public DoctorResource(DoctorService doctorService) {
    this.doctorService = doctorService;
  }

  @PostMapping("/register")
  public ResponseEntity<Doctor> registerDoctor(@RequestBody Doctor doctor) {
    StopWatch stopWatch = StopWatch.createStarted();
    log.debug("START | Request to register Doctor by:[{}]", doctor);
    Doctor created = doctorService.registerDoctor(doctor);
    log.debug("STOP | Register request time:[{}]ms", stopWatch.getTime());
    return ResponseEntity.ok(created);
  }

  @PutMapping("/")
  public ResponseEntity<Doctor> updateDoctor(@RequestBody Doctor doctor) {
    StopWatch stopWatch = StopWatch.createStarted();
    log.debug("START | Request to update Doctor by:[{}]", doctor);
    Doctor updated = doctorService.updateDoctor(doctor);
    log.debug("STOP | Update request time:[{}]ms", stopWatch.getTime());
    return ResponseEntity.ok(updated);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> updateDoctor(@PathVariable(name = "id") Long id) {
    StopWatch stopWatch = StopWatch.createStarted();
    log.debug("START | Request to delete Doctor by:[{}]", id);
    doctorService.deleteDoctor(id);
    log.debug("STOP | Delete request time:[{}]ms", stopWatch.getTime());
    return ResponseEntity.ok().build();
  }
}
