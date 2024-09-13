package com.anderfred.medical.clinic;

import org.springframework.boot.SpringApplication;

public class TestClinicApplication {

  public static void main(String[] args) {
    SpringApplication.from(ClinicApplication::main).with(TestcontainersConfiguration.class).run(args);
  }

}
