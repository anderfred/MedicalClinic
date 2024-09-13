package com.anderfred.medical.clinic.web.rest;

import com.anderfred.medical.clinic.domain.User;
import com.anderfred.medical.clinic.domain.auth.AuthRequest;
import com.anderfred.medical.clinic.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
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

  private final AuthService authService;

  public AuthResource(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/doctor-login")
  public ResponseEntity<User> doctorLogin(
      @RequestBody AuthRequest authRequest, HttpServletResponse response) {
    StopWatch stopWatch = StopWatch.createStarted();
    log.debug("START | Request to doctor login by:[{}]", authRequest);
    User user = authService.authenticateDoctor(authRequest, response);
    log.debug(
        "STOP | Doctor login request, logged in:[{}], time:[{}]ms", user, stopWatch.getTime());
    return ResponseEntity.ok(user);
  }

  @PostMapping("/patient-login")
  public ResponseEntity<User> patientLogin(
      @RequestBody AuthRequest authRequest, HttpServletResponse response) {
    StopWatch stopWatch = StopWatch.createStarted();
    log.debug("START | Request to patient login by:[{}]", authRequest);
    User user = authService.authenticatePatient(authRequest, response);
    log.debug(
        "STOP | Patient login request, logged in:[{}], time:[{}]ms", user, stopWatch.getTime());
    return ResponseEntity.ok(user);
  }
}
