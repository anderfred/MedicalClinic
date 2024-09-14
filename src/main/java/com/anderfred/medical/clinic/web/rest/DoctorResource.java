package com.anderfred.medical.clinic.web.rest;

import com.anderfred.medical.clinic.domain.user.Doctor;
import com.anderfred.medical.clinic.domain.user.Patient;
import com.anderfred.medical.clinic.service.DoctorService;
import jakarta.servlet.ServletRequest;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.print.Doc;

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

  @GetMapping("/{id}")
  public ResponseEntity<Doctor> getById(@PathVariable Long id) {
    StopWatch stopWatch = StopWatch.createStarted();
    log.debug("START | Request to get doctor by id:[{}]", id);
    Doctor created = doctorService.findById(id);
    log.debug("STOP | Get doctor by id time:[{}]ms", stopWatch.getTime());
    return ResponseEntity.ok(created);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> updateDoctor(@PathVariable(name = "id") Long id) {
    StopWatch stopWatch = StopWatch.createStarted();
    log.debug("START | Request to delete Doctor by:[{}]", id);
    doctorService.deleteDoctor(id);
    log.debug("STOP | Delete request time:[{}]ms", stopWatch.getTime());
    return ResponseEntity.ok().build();
  }

  @GetMapping("/page")
  public ResponseEntity<Page<Doctor>> findPage(Pageable pageable) {
    StopWatch stopWatch = StopWatch.createStarted();
    log.debug("START | Request to get page:[{}] of Doctors", pageable);
    Page<Doctor> doctors = doctorService.findPage(pageable);
    log.debug("STOP | Request to get doctors page:[{}], time:[{}]ms",doctors.getTotalElements(), stopWatch.getTime());
    return ResponseEntity.ok(doctors);
  }
}
