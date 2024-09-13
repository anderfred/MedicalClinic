package com.anderfred.medical.clinic.domain;

import com.anderfred.medical.clinic.domain.base.AbstractAuditingEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "clinic")
public class Clinic extends AbstractAuditingEntity {
  @Id
  private Long id;

  public Clinic setId(Long id) {
    this.id = id;
    return this;
  }

  public Long getId() {
    return id;
  }
}
