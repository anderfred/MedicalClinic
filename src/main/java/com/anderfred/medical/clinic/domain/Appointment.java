package com.anderfred.medical.clinic.domain;

import com.anderfred.medical.clinic.domain.base.AbstractAuditingEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "appointment")
public class Appointment extends AbstractAuditingEntity {
  @Id private Long id;

  public Appointment setId(Long id) {
    this.id = id;
    return this;
  }

  public Long getId() {
    return id;
  }
}
