package com.anderfred.medical.clinic.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "clinic")
public class MedicalTest {
  @Id private Long id;

  public MedicalTest setId(Long id) {
    this.id = id;
    return this;
  }

  public Long getId() {
    return id;
  }
}
