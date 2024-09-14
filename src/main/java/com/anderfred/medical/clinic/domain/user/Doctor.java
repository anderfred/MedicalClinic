package com.anderfred.medical.clinic.domain.user;

import jakarta.persistence.*;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue(value = Doctor.TYPE_DISCRIMINATOR)
@Access(AccessType.FIELD)
public class Doctor extends User {
  public static final String TYPE_DISCRIMINATOR = "doctor";

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("user", super.toString()).toString();
  }
}
