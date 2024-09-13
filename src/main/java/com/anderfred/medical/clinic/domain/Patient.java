package com.anderfred.medical.clinic.domain;

import jakarta.persistence.*;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue(value = Patient.TYPE_DISCRIMINATOR)
@Access(AccessType.FIELD)
public class Patient extends User {
  public static final String TYPE_DISCRIMINATOR = "patient";

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("user", super.toString()).toString();
  }
}
