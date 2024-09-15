package com.anderfred.medical.clinic.domain;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import com.anderfred.medical.clinic.domain.base.AbstractAuditingEntity;
import com.anderfred.medical.clinic.domain.clinic.Clinic;
import com.anderfred.medical.clinic.domain.user.Doctor;
import com.anderfred.medical.clinic.domain.user.Patient;
import com.anderfred.medical.clinic.exceptions.BaseException;
import com.anderfred.medical.clinic.exceptions.ClinicExceptionCode;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@Table(name = "appointment")
public class Appointment extends AbstractAuditingEntity {
  private static final Logger log = LoggerFactory.getLogger(Appointment.class);

  @Column(name = "id")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "doctor")
  private Doctor doctor;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "patient")
  private Patient patient;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "last_modified_doctor")
  private Doctor lastModifiedDoctor;

  @Column(name = "result_ready_date")
  private LocalDateTime resultReadyDate;

  @Column(name = "state", nullable = false)
  @Enumerated(EnumType.STRING)
  private AppointmentState state = AppointmentState.NEW;

  @OneToMany(
      fetch = FetchType.LAZY,
      mappedBy = "appointment",
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  private List<MedicalExam> medicalExams;

  public Doctor getDoctor() {
    return doctor;
  }

  public Appointment setDoctor(Doctor doctor) {
    this.doctor = doctor;
    return this;
  }

  public Patient getPatient() {
    return patient;
  }

  public Appointment setPatient(Patient patient) {
    this.patient = patient;
    return this;
  }

  public Doctor getLastModifiedDoctor() {
    return lastModifiedDoctor;
  }

  public Appointment setLastModifiedDoctor(Doctor lastModifiedDoctor) {
    this.lastModifiedDoctor = lastModifiedDoctor;
    return this;
  }

  public LocalDateTime getResultReadyDate() {
    return resultReadyDate;
  }

  public Appointment setResultReadyDate(LocalDateTime resultReadyDate) {
    this.resultReadyDate = resultReadyDate;
    return this;
  }

  public AppointmentState getState() {
    return state;
  }

  public Appointment setState(AppointmentState state) {
    this.state = state;
    return this;
  }

  public List<MedicalExam> getMedicalExams() {
    if (isNull(medicalExams)) {
      medicalExams = new ArrayList<>();
    }
    return medicalExams;
  }

  public Appointment setMedicalExams(List<MedicalExam> medicalExams) {
    this.medicalExams = medicalExams;
    return this;
  }

  public Appointment setId(Long id) {
    this.id = id;
    return this;
  }

  public Long getId() {
    return id;
  }

  public void update(Appointment appointment, Long doctorId, Clinic clinic) {
    Doctor doctor = (Doctor) new Doctor().setId(doctorId);
    setLastModifiedDoctor(doctor);
    setResultReadyDate(appointment.getResultReadyDate());
    setState(appointment.getState());
    updateExams(appointment.getMedicalExams());
    validate(clinic);
  }

  private void updateExams(List<MedicalExam> exams) {
    if (CollectionUtils.isEmpty(exams)) {
      if (!getMedicalExams().isEmpty()) {
        log.debug("Clear all exams");
        getMedicalExams().clear();
      }
    } else {
      Set<Long> presentExamIds =
          exams.stream()
              .map(MedicalExam::getId)
              .filter(Objects::nonNull)
              .collect(Collectors.toSet());
      if (!presentExamIds.isEmpty()) {
        getMedicalExams()
            .removeIf(
                ex -> {
                  boolean remove = !presentExamIds.contains(ex.getId());
                  if (remove) {
                    log.debug("Removing exam:[{}]", ex);
                  }
                  return remove;
                });
      }
      exams.forEach(
          exam ->
              getMedicalExams().stream()
                  .filter(ex -> nonNull(exam.getId()) && Objects.equals(ex.getId(), exam.getId()))
                  .findAny()
                  .ifPresentOrElse(
                      found -> {
                        log.debug("Updating exam:[{}]->[{}]", found, exam);
                        found.update(exam);
                      },
                      () -> {
                        log.debug("Adding exam:[{}]", exam);
                        getMedicalExams().add(exam);
                      }));
    }
  }

  public void delete() {
    if (AppointmentState.DELETED.equals(getState())) {
      throw new BaseException("Appointment already deleted", ClinicExceptionCode.INVALID_REQUEST);
    }
    setState(AppointmentState.DELETED);
  }

  public void initBeforeCreate(Long doctorId, Long patientId, Clinic clinic) {
    Doctor doctor = (Doctor) new Doctor().setId(doctorId);
    setDoctor(doctor);
    setPatient((Patient) new Patient().setId(patientId));
    setLastModifiedDoctor(doctor);
    CollectionUtils.emptyIfNull(getMedicalExams()).stream()
        .filter(e -> isNull(e.getAppointment()))
        .forEach(e -> e.setAppointment(this));
    validate(clinic);
  }

  private void validate(Clinic clinic) {
    CollectionUtils.emptyIfNull(getMedicalExams()).stream()
        .filter(ex -> nonNull(ex.getDate()))
        .forEach(
            exam -> {
              if (!clinic.checkScheduling(exam.getDate())) {
                log.error(
                    "Date:[{}] of exam:[{}] is not in clinic schedule",
                    exam.getDate(),
                    exam.getExamType().getName());
                throw new BaseException(
                    "Exam date is not in clinic schedule", ClinicExceptionCode.INVALID_REQUEST);
              }
            });
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
        .append("id", getId())
        .append("resultReadyDate", getResultReadyDate())
        .append("state", getState())
        .append("medicalExams", getMedicalExams())
        .toString();
  }
}
