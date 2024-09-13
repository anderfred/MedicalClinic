package com.anderfred.medical.clinic.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/medical-test")
public class MedicalTestResource {
  private final Logger log = LoggerFactory.getLogger(MedicalTestResource.class);

  // TODO pdf generation by jasper soft
}
