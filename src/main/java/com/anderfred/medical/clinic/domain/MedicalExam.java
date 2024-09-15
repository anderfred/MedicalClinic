package com.anderfred.medical.clinic.domain;

import com.anderfred.medical.clinic.domain.base.AbstractAuditingEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
@Table(name = "medical_exam")
public class MedicalExam extends AbstractAuditingEntity {
  @Column(name = "id")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "appointment_id")
  private Appointment appointment;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "exam_type")
  private ExamType examType;

  @Column(name = "date")
  private LocalDateTime date;

  @Column(name = "result", nullable = false)
  @Enumerated(EnumType.STRING)
  private MedicalExamResult result;

  public MedicalExam setId(Long id) {
    this.id = id;
    return this;
  }

  public Long getId() {
    return id;
  }

  public Appointment getAppointment() {
    return appointment;
  }

  public MedicalExam setAppointment(Appointment appointment) {
    this.appointment = appointment;
    return this;
  }

  public LocalDateTime getDate() {
    return date;
  }

  public MedicalExam setDate(LocalDateTime date) {
    this.date = date;
    return this;
  }

  public MedicalExamResult getResult() {
    return result;
  }

  public MedicalExam setResult(MedicalExamResult result) {
    this.result = result;
    return this;
  }

  public ExamType getExamType() {
    return examType;
  }

  public MedicalExam setExamType(ExamType examType) {
    this.examType = examType;
    return this;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
        .append("id", getId())
        .append("examType", getExamType())
        .append("date", getDate())
        .append("result", getResult())
        .toString();
  }

  public void update(MedicalExam exam) {
    setExamType(exam.getExamType());
    setDate(exam.getDate());
    setResult(exam.getResult());
  }
}
