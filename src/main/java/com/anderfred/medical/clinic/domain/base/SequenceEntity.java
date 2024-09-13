package com.anderfred.medical.clinic.domain.base;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import org.springframework.beans.factory.annotation.Configurable;

@Configurable
public abstract class SequenceEntity<T> {
  public abstract T getId();

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (isNull(o) || !(o.getClass().equals(getClass()))) return false;

    final SequenceEntity<?> obj = (SequenceEntity<?>) o;
    return nonNull(getId()) && getId().equals(obj.getId());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
