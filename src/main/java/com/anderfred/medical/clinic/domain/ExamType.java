package com.anderfred.medical.clinic.domain;

import com.anderfred.medical.clinic.domain.base.SequenceEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
@Table(name = "exam_type")
public class ExamType extends SequenceEntity<Long> {

  @Column(name = "id")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", nullable = false)
  @NotEmpty
  private String name;

  @Override
  public Long getId() {
    return id;
  }

  public ExamType setId(Long id) {
    this.id = id;
    return this;
  }

  public @NotEmpty String getName() {
    return name;
  }

  public ExamType setName(@NotEmpty String name) {
    this.name = name;
    return this;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
            .append("id", getId())
            .append("name", getName())
            .toString();
  }
}
