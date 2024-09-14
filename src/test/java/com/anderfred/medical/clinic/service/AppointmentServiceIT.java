package com.anderfred.medical.clinic.service;

import static com.anderfred.medical.clinic.util.IdUtils.randomLong;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.assertj.core.api.Assertions.assertThat;

import com.anderfred.medical.clinic.base.BaseIT;
import com.anderfred.medical.clinic.domain.*;
import com.anderfred.medical.clinic.domain.user.Doctor;
import com.anderfred.medical.clinic.domain.user.Patient;
import com.anderfred.medical.clinic.exceptions.ClinicExceptionCode;
import com.anderfred.medical.clinic.repository.jpa.AppointmentJpaRepository;
import com.anderfred.medical.clinic.security.CustomAuthenticationToken;
import com.anderfred.medical.clinic.security.UserRole;
import com.anderfred.medical.clinic.security.WithCustomMockUser;
import com.anderfred.medical.clinic.service.impl.ExamTypeService;
import com.anderfred.medical.clinic.service.test.UserServiceHelper;
import com.anderfred.medical.clinic.util.AssertJUtil;
import com.anderfred.medical.clinic.util.MappingUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;

public class AppointmentServiceIT extends BaseIT {
  @Autowired AppointmentService appointmentService;
  @Autowired PatientService patientService;
  @Autowired MedicalExamService medicalExamService;
  @Autowired ExamTypeService examTypeService;
  @Autowired UserServiceHelper userServiceHelper;
  @Autowired AppointmentJpaRepository appointmentJpaRepository;
  @Autowired ObjectMapper mapper;

  @Test
  @WithCustomMockUser
  public void shouldCreateAppointmentSimple() {
    Doctor doctor = userServiceHelper.createPersistedDoctor();
    Patient patient = userServiceHelper.createPersistedPatient();

    mockSecurityContext(doctor.getId(), doctor.getEmail(), UserRole.DOCTOR);
    LocalDateTime date = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    Appointment appointment = new Appointment().setResultReadyDate(date);
    Appointment saved = appointmentService.addAppointment(appointment, patient.getId());
    Appointment persisted = appointmentJpaRepository.findFullById(saved.getId()).orElseThrow();
    assertThat(persisted.getDoctor().getId()).isEqualTo(doctor.getId());
    assertThat(persisted.getPatient().getId()).isEqualTo(patient.getId());
    assertThat(persisted.getResultReadyDate()).isEqualTo(date);
    assertThat(persisted.getState()).isEqualTo(AppointmentState.NEW);

    Appointment appointment2 = new Appointment();
    Appointment saved2 = appointmentService.addAppointment(appointment2, patient.getId());
    Appointment persisted2 = appointmentJpaRepository.findFullById(saved2.getId()).orElseThrow();
    assertThat(persisted2.getDoctor().getId()).isEqualTo(doctor.getId());
    assertThat(persisted2.getPatient().getId()).isEqualTo(patient.getId());
    assertThat(persisted2.getResultReadyDate()).isNull();
    assertThat(persisted2.getState()).isEqualTo(AppointmentState.NEW);

    Appointment appointment3 = new Appointment().setState(AppointmentState.IN_PROGRESS);
    Appointment saved3 = appointmentService.addAppointment(appointment3, patient.getId());
    Appointment persisted3 = appointmentJpaRepository.findFullById(saved3.getId()).orElseThrow();
    assertThat(persisted3.getDoctor().getId()).isEqualTo(doctor.getId());
    assertThat(persisted3.getPatient().getId()).isEqualTo(patient.getId());
    assertThat(persisted3.getResultReadyDate()).isNull();
    assertThat(persisted3.getState()).isEqualTo(AppointmentState.IN_PROGRESS);
  }

  @Test
  @WithCustomMockUser
  public void shouldUpdateAppointment() {
    Doctor doctor = userServiceHelper.createPersistedDoctor();
    Doctor doctor2 = userServiceHelper.createPersistedDoctor();
    Patient patient = userServiceHelper.createPersistedPatient();
    mockSecurityContext(doctor.getId(), doctor.getEmail(), UserRole.DOCTOR);

    ExamType examType = generateExamType();
    ExamType examType2 = generateExamType();
    Appointment appointment = new Appointment();
    Appointment saved = appointmentService.addAppointment(appointment, patient.getId());
    Appointment persisted = appointmentJpaRepository.findFullById(saved.getId()).orElseThrow();
    assertThat(persisted.getDoctor().getId()).isEqualTo(doctor.getId());
    assertThat(persisted.getPatient().getId()).isEqualTo(patient.getId());
    assertThat(persisted.getResultReadyDate()).isNull();
    assertThat(persisted.getState()).isEqualTo(AppointmentState.NEW);

    appointment.setId(persisted.getId());
    LocalDateTime date = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    appointment.setResultReadyDate(date);
    appointment.setState(AppointmentState.IN_PROGRESS);
    MedicalExam exam1 =
        new MedicalExam()
            .setExamType(examType)
            .setAppointment(appointment)
            .setResult(MedicalExamResult.POSITIVE);
    MedicalExam exam2 =
        new MedicalExam()
            .setExamType(examType2)
            .setAppointment(appointment)
            .setResult(MedicalExamResult.NEGATIVE);
    appointment.setMedicalExams(List.of(exam1, exam2));

    mockSecurityContext(doctor2.getId(), doctor2.getEmail(), UserRole.DOCTOR);
    appointmentService.update(appointment);
    persisted = appointmentJpaRepository.findFullById(saved.getId()).orElseThrow();
    assertThat(persisted.getDoctor().getId()).isEqualTo(doctor.getId());
    assertThat(persisted.getPatient().getId()).isEqualTo(patient.getId());
    assertThat(persisted.getLastModifiedDoctor().getId()).isEqualTo(doctor2.getId());
    assertThat(persisted.getResultReadyDate()).isEqualTo(date);
    assertThat(persisted.getState()).isEqualTo(AppointmentState.IN_PROGRESS);
    assertThat(persisted.getMedicalExams()).hasSize(2);

    List<MedicalExam> medicalExams = persisted.getMedicalExams();
    assertThat(medicalExams).hasSize(2);

    medicalExams.forEach(
        ex -> {
          assertThat(ex.getDate()).isNull();
          assertThat(
                  List.of(examType.getId(), examType2.getId()).contains(ex.getExamType().getId()))
              .isTrue();
        });
    assertThat(
            medicalExams.stream()
                .allMatch(
                    ex ->
                        List.of(MedicalExamResult.NEGATIVE, MedicalExamResult.POSITIVE)
                            .contains(ex.getResult())))
        .isTrue();

    LocalDateTime updatedDate = LocalDateTime.of(2024, 10, 12, 10, 11);
    List<MedicalExam> exams =
        MappingUtil.copyList(mapper, persisted.getMedicalExams(), MedicalExam.class);
    MedicalExam exam = exams.iterator().next();
    exam.setDate(updatedDate).setResult(MedicalExamResult.UNKNOWN);
    persisted.setMedicalExams(List.of(exam));
    appointmentService.update(persisted);
    Appointment updated = appointmentJpaRepository.findFullById(saved.getId()).orElseThrow();
    assertThat(updated.getMedicalExams()).hasSize(1);
    MedicalExam updatedExam = updated.getMedicalExams().iterator().next();
    assertThat(updatedExam.getDate()).isEqualTo(updatedDate);
    assertThat(updatedExam.getResult()).isEqualTo(MedicalExamResult.UNKNOWN);

    // Exam date is out of clinic schedule
    LocalDateTime updatedDate2 = LocalDateTime.of(2024, 10, 12, 22, 22);
    List<MedicalExam> exams2 =
        MappingUtil.copyList(mapper, persisted.getMedicalExams(), MedicalExam.class);
    exam = exams2.iterator().next();
    exam.setDate(updatedDate2).setResult(MedicalExamResult.UNKNOWN);
    updated.setMedicalExams(List.of(exam));
    AssertJUtil.assertBaseException(
        ClinicExceptionCode.INVALID_REQUEST, () -> appointmentService.update(updated));
  }

  @Test
  @WithCustomMockUser
  public void shouldDeleteAppointment() {
    Doctor doctor = userServiceHelper.createPersistedDoctor();
    Patient patient = userServiceHelper.createPersistedPatient();

    mockSecurityContext(doctor.getId(), doctor.getEmail(), UserRole.DOCTOR);
    LocalDateTime date = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    Appointment appointment = new Appointment().setResultReadyDate(date);
    Appointment saved = appointmentService.addAppointment(appointment, patient.getId());
    Appointment persisted = appointmentJpaRepository.findFullById(saved.getId()).orElseThrow();
    assertThat(persisted.getDoctor().getId()).isEqualTo(doctor.getId());
    assertThat(persisted.getPatient().getId()).isEqualTo(patient.getId());
    assertThat(persisted.getResultReadyDate()).isEqualTo(date);
    assertThat(persisted.getState()).isEqualTo(AppointmentState.NEW);

    appointmentService.delete(persisted.getId());
    Appointment deleted = appointmentJpaRepository.findFullById(saved.getId()).orElseThrow();
    assertThat(deleted.getState()).isEqualTo(AppointmentState.DELETED);

    // Unable to delete already deleted appointment
    AssertJUtil.assertBaseException(
        ClinicExceptionCode.INVALID_REQUEST, () -> appointmentService.delete(saved.getId()));
  }

  @Test
  @WithCustomMockUser
  public void shouldFindAppointmentById() {
    Doctor doctor = userServiceHelper.createPersistedDoctor();
    Patient patient = userServiceHelper.createPersistedPatient();

    mockSecurityContext(doctor.getId(), doctor.getEmail(), UserRole.DOCTOR);
    LocalDateTime date = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    Appointment appointment = new Appointment().setResultReadyDate(date);
    Appointment saved = appointmentService.addAppointment(appointment, patient.getId());
    Appointment persisted = appointmentJpaRepository.findFullById(saved.getId()).orElseThrow();
    assertThat(persisted.getDoctor().getId()).isEqualTo(doctor.getId());
    assertThat(persisted.getPatient().getId()).isEqualTo(patient.getId());
    assertThat(persisted.getResultReadyDate()).isEqualTo(date);
    assertThat(persisted.getState()).isEqualTo(AppointmentState.NEW);

    Appointment appointmentById = appointmentService.findAppointmentById(persisted.getId());
    assertThat(appointmentById.getId()).isEqualTo(saved.getId());

    AssertJUtil.assertBaseException(
        ClinicExceptionCode.ENTITY_NOT_FOUND,
        () -> appointmentService.findAppointmentById(randomLong()));
  }

  @Test
  @WithCustomMockUser
  public void shouldGetAllActiveAppointments() {
    Doctor doctor = userServiceHelper.createPersistedDoctor();
    Patient patient = userServiceHelper.createPersistedPatient();

    List<Appointment> list = new ArrayList<>();

    mockSecurityContext(doctor.getId(), doctor.getEmail(), UserRole.DOCTOR);
    for (int i = 0; i < 10; i++) {
      list.add(appointmentService.addAppointment(new Appointment(), patient.getId()));
    }

    Set<Long> deleted =
        list.subList(0, 3).stream().map(Appointment::getId).collect(Collectors.toSet());
    list.removeIf(e -> deleted.contains(e.getId()));

    deleted.forEach(d -> appointmentService.delete(d));

    list.subList(0, 3).forEach(a -> a.setState(AppointmentState.IN_PROGRESS));
    list.subList(3, 5).forEach(a -> a.setState(AppointmentState.CLOSED));

    list.forEach(a -> appointmentService.update(a));

    PageRequest pageRequest = PageRequest.of(0, 20);
    Page<Appointment> activeAppointments = appointmentService.findActiveAppointments(pageRequest);
    assertThat(activeAppointments.getTotalElements()).isEqualTo(list.size());
    assertThat(
            activeAppointments.getContent().stream()
                .allMatch(
                    a ->
                        list.stream()
                            .map(Appointment::getId)
                            .collect(Collectors.toSet())
                            .contains(a.getId())))
        .isTrue();
  }

  @Test
  @WithCustomMockUser
  public void shouldNotHardDeletePatientWithExistedAppointment() {
    Doctor doctor = userServiceHelper.createPersistedDoctor();
    Patient patient = userServiceHelper.createPersistedPatient();

    mockSecurityContext(doctor.getId(), doctor.getEmail(), UserRole.DOCTOR);
    LocalDateTime date = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    Appointment appointment = new Appointment().setResultReadyDate(date);
    Appointment saved = appointmentService.addAppointment(appointment, patient.getId());
    Appointment persisted = appointmentJpaRepository.findFullById(saved.getId()).orElseThrow();
    assertThat(persisted.getDoctor().getId()).isEqualTo(doctor.getId());
    assertThat(persisted.getPatient().getId()).isEqualTo(patient.getId());
    assertThat(persisted.getResultReadyDate()).isEqualTo(date);
    assertThat(persisted.getState()).isEqualTo(AppointmentState.NEW);

    patientService.deletePatient(patient.getId());
    Patient found = patientService.findById(patient.getId());
    assertThat(found).isNotNull();
  }

  private ExamType generateExamType() {
    ExamType examType = new ExamType().setName(randomAlphanumeric(10));
    return examTypeService.create(examType);
  }

  private void mockSecurityContext(Long id, String name, UserRole role) {
    CustomAuthenticationToken auth =
        new CustomAuthenticationToken(
            name, null, List.of(new SimpleGrantedAuthority(role.getDescription())), role, id);
    SecurityContextHolder.getContext().setAuthentication(auth);
  }
}
