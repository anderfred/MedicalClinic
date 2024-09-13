package com.anderfred.medical.clinic.service;

import static com.anderfred.medical.clinic.util.IdUtils.randomLong;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

import com.anderfred.medical.clinic.base.BaseIT;
import com.anderfred.medical.clinic.base.security.WithMockCustomUser;
import com.anderfred.medical.clinic.domain.Doctor;
import com.anderfred.medical.clinic.domain.UserState;
import com.anderfred.medical.clinic.exceptions.ClinicExceptionCode;
import com.anderfred.medical.clinic.repository.jpa.DoctorJpaRepository;
import com.anderfred.medical.clinic.util.AssertJUtil;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

public class DoctorServiceIT extends BaseIT {
  @Autowired private DoctorJpaRepository repository;
  @Autowired private DoctorService doctorService;

  public static final String INITIAL_USER_EMAIL = "admin@test.com";
  public static final String INITIAL_USER_NAME = "admin";

  @Test
  public void shouldVerifySystemDoctorUser() {
    Optional<Doctor> possibleInitialDoctor = repository.findByEmail(INITIAL_USER_EMAIL);
    assertThat(possibleInitialDoctor).isNotEmpty();
    Doctor doctor = possibleInitialDoctor.get();
    assertThat(doctor.getEmail()).isEqualTo(INITIAL_USER_EMAIL);
    assertThat(doctor.getFirstName()).isEqualTo(INITIAL_USER_NAME);
    assertThat(doctor.getLastName()).isEqualTo(INITIAL_USER_NAME);
    assertThat(doctor.getId()).isNotNull();
    assertThat(doctor.getCreatedBy()).isNotNull();
    assertThat(doctor.getCreatedDate()).isNotNull();
    assertThat(doctor.getLastLoginDate()).isNull();
    assertThat(doctor.getLastModifiedDate()).isNotNull();
    assertThat(doctor.getPassword()).isNotNull();
    assertThat(doctor.getLastModifiedBy()).isNotNull();
    assertThat(doctor.getLastModifiedDate()).isNotNull();
  }

  @Test
  @WithMockCustomUser(
      username = "doc",
      roles = {"DOCTOR"})
  public void shouldCreateDoctor() {
    Doctor doctor = generateDoctor();
    Doctor savedDoctor = repository.save(doctor);
    assertThat(savedDoctor.getId()).isNotNull();
    assertThat(savedDoctor.getFirstName()).isEqualTo(doctor.getFirstName());
    assertThat(savedDoctor.getLastName()).isEqualTo(doctor.getLastName());
    assertThat(savedDoctor.getEmail()).isEqualTo(doctor.getEmail());
    assertThat(savedDoctor.getPassword()).isEqualTo(doctor.getPassword());
    assertThat(savedDoctor.getLastModifiedDate()).isNotNull();
    assertThat(savedDoctor.getLastModifiedBy()).isNotNull();
    assertThat(savedDoctor.getCreatedDate()).isNotNull();
    assertThat(savedDoctor.getCreatedBy()).isNotNull();
    assertThat(savedDoctor.getLastLoginDate()).isNull();

    Doctor doctor2 = generateDoctor();
    Doctor savedDoctor2 = repository.save(doctor2);
    assertThat(savedDoctor2.getId()).isNotNull();
    assertThat(savedDoctor2.getFirstName()).isEqualTo(doctor2.getFirstName());
    assertThat(savedDoctor2.getLastName()).isEqualTo(doctor2.getLastName());
    assertThat(savedDoctor2.getEmail()).isEqualTo(doctor2.getEmail());
    assertThat(savedDoctor2.getPassword()).isEqualTo(doctor2.getPassword());
    assertThat(savedDoctor2.getLastModifiedDate()).isNotNull();
    assertThat(savedDoctor2.getLastModifiedBy()).isNotNull();
    assertThat(savedDoctor2.getCreatedDate()).isNotNull();
    assertThat(savedDoctor2.getCreatedBy()).isNotNull();
    assertThat(savedDoctor2.getLastLoginDate()).isNull();
  }

  @Test
  @WithMockCustomUser(
      username = "doc",
      roles = {"DOCTOR"})
  public void shouldNotCreateDoctorIfEmailAlreadyExists() {
    Doctor doctor = generateDoctor();
    Doctor savedDoctor = repository.save(doctor);
    assertThat(savedDoctor.getId()).isNotNull();

    Doctor doctorWithSameEmail = (Doctor) generateDoctor().setEmail(doctor.getEmail());
    assertThrows(DataIntegrityViolationException.class, () -> repository.save(doctorWithSameEmail));
  }

  @Test
  @WithMockCustomUser(
      username = "doc",
      roles = {"DOCTOR"})
  public void shouldRegisterDoctor() {
    Doctor doctor = generateDoctor();
    Doctor savedDoctor = doctorService.registerDoctor(doctor);
    assertThat(savedDoctor.getId()).isNotNull();
    assertThat(savedDoctor.getFirstName()).isEqualTo(doctor.getFirstName());
    assertThat(savedDoctor.getLastName()).isEqualTo(doctor.getLastName());
    assertThat(savedDoctor.getEmail()).isEqualTo(doctor.getEmail());
    assertThat(savedDoctor.getPassword()).isEqualTo(doctor.getPassword());
    assertThat(savedDoctor.getLastModifiedDate()).isNotNull();
    assertThat(savedDoctor.getLastModifiedBy()).isNotNull();
    assertThat(savedDoctor.getCreatedDate()).isNotNull();
    assertThat(savedDoctor.getCreatedBy()).isNotNull();
    assertThat(savedDoctor.getLastLoginDate()).isNull();
    assertThat(repository.findById(savedDoctor.getId())).isNotEmpty();
  }

  @Test
  @WithMockCustomUser(
      username = "doc",
      roles = {"DOCTOR"})
  public void shouldUpdateDoctor() {
    Doctor doctor = generateDoctor();
    Doctor savedDoctor = doctorService.registerDoctor(doctor);
    assertThat(savedDoctor.getId()).isNotNull();
    assertThat(savedDoctor.getFirstName()).isEqualTo(doctor.getFirstName());
    assertThat(savedDoctor.getLastName()).isEqualTo(doctor.getLastName());
    assertThat(savedDoctor.getEmail()).isEqualTo(doctor.getEmail());
    assertThat(savedDoctor.getPassword()).isEqualTo(doctor.getPassword());
    assertThat(savedDoctor.getLastModifiedDate()).isNotNull();
    assertThat(savedDoctor.getLastModifiedBy()).isNotNull();
    assertThat(savedDoctor.getCreatedDate()).isNotNull();
    assertThat(savedDoctor.getCreatedBy()).isNotNull();
    assertThat(savedDoctor.getLastLoginDate()).isNull();
    assertThat(repository.findById(savedDoctor.getId())).isNotEmpty();

    Doctor updated = generateDoctor();
    updated.setState(UserState.DELETED);
    updated.setId(savedDoctor.getId());

    doctorService.updateDoctor(updated);
    Doctor actual = repository.findById(savedDoctor.getId()).orElseThrow();
    assertThat(actual.getLastName()).isEqualTo(updated.getLastName());
    assertThat(actual.getFirstName()).isEqualTo(updated.getFirstName());
    assertThat(actual.getEmail()).isEqualTo(updated.getEmail());

    // Fields must not updates
    assertThat(actual.getLastModifiedDate()).isNotEqualTo(updated.getLastModifiedDate());
    assertThat(actual.getPassword()).isNotEqualTo(updated.getPassword());
    assertThat(actual.getState()).isNotEqualTo(updated.getState());
  }

  @Test
  @WithMockCustomUser(
      username = "doc",
      roles = {"DOCTOR"})
  public void shouldNotUpdateDoctor() {
    Doctor doctor = generateDoctor();
    Doctor savedDoctor = doctorService.registerDoctor(doctor);
    assertThat(savedDoctor.getId()).isNotNull();
    assertThat(savedDoctor.getFirstName()).isEqualTo(doctor.getFirstName());
    assertThat(savedDoctor.getLastName()).isEqualTo(doctor.getLastName());
    assertThat(savedDoctor.getEmail()).isEqualTo(doctor.getEmail());
    assertThat(savedDoctor.getPassword()).isEqualTo(doctor.getPassword());
    assertThat(savedDoctor.getLastModifiedDate()).isNotNull();
    assertThat(savedDoctor.getLastModifiedBy()).isNotNull();
    assertThat(savedDoctor.getCreatedDate()).isNotNull();
    assertThat(savedDoctor.getCreatedBy()).isNotNull();
    assertThat(savedDoctor.getLastLoginDate()).isNull();
    assertThat(repository.findById(savedDoctor.getId())).isNotEmpty();

    Doctor withEmailDuplicate = generateDoctor();
    doctorService.registerDoctor(withEmailDuplicate);

    Doctor updated = generateDoctor();
    updated.setEmail(withEmailDuplicate.getEmail());
    updated.setId(savedDoctor.getId());

    // Email already taken
    AssertJUtil.assertBaseException(
        ClinicExceptionCode.INVALID_EMAIL, () -> doctorService.updateDoctor(updated));
    // Id of doctor not found
    AssertJUtil.assertBaseException(
        ClinicExceptionCode.ENTITY_NOT_FOUND,
        () -> doctorService.updateDoctor((Doctor) updated.setId(randomLong())));
  }

  @Test
  @WithMockCustomUser(
      username = "doc",
      roles = {"DOCTOR"})
  public void shouldDeleteDoctor() {
    Doctor doctor = generateDoctor();
    Doctor savedDoctor = doctorService.registerDoctor(doctor);
    assertThat(savedDoctor.getId()).isNotNull();
    assertThat(savedDoctor.getFirstName()).isEqualTo(doctor.getFirstName());
    assertThat(savedDoctor.getLastName()).isEqualTo(doctor.getLastName());
    assertThat(savedDoctor.getEmail()).isEqualTo(doctor.getEmail());
    assertThat(savedDoctor.getPassword()).isEqualTo(doctor.getPassword());
    assertThat(savedDoctor.getLastModifiedDate()).isNotNull();
    assertThat(savedDoctor.getLastModifiedBy()).isNotNull();
    assertThat(savedDoctor.getCreatedDate()).isNotNull();
    assertThat(savedDoctor.getCreatedBy()).isNotNull();
    assertThat(savedDoctor.getLastLoginDate()).isNull();
    assertThat(repository.findById(savedDoctor.getId())).isNotEmpty();

    doctorService.deleteDoctor(savedDoctor.getId());
    Doctor persisted = repository.findById(savedDoctor.getId()).orElseThrow();
    assertThat(persisted.getState()).isEqualTo(UserState.DELETED);
  }

  @Test
  @WithMockCustomUser(
      username = "doc",
      roles = {"DOCTOR"})
  public void shouldNotDeleteDoctorAlreadyDeleted() {
    Doctor doctor = generateDoctor();
    Doctor savedDoctor = doctorService.registerDoctor(doctor);
    assertThat(savedDoctor.getId()).isNotNull();
    assertThat(savedDoctor.getFirstName()).isEqualTo(doctor.getFirstName());
    assertThat(savedDoctor.getLastName()).isEqualTo(doctor.getLastName());
    assertThat(savedDoctor.getEmail()).isEqualTo(doctor.getEmail());
    assertThat(savedDoctor.getPassword()).isEqualTo(doctor.getPassword());
    assertThat(savedDoctor.getLastModifiedDate()).isNotNull();
    assertThat(savedDoctor.getLastModifiedBy()).isNotNull();
    assertThat(savedDoctor.getCreatedDate()).isNotNull();
    assertThat(savedDoctor.getCreatedBy()).isNotNull();
    assertThat(savedDoctor.getLastLoginDate()).isNull();
    assertThat(repository.findById(savedDoctor.getId())).isNotEmpty();

    doctorService.deleteDoctor(savedDoctor.getId());
    Doctor persisted = repository.findById(savedDoctor.getId()).orElseThrow();
    assertThat(persisted.getState()).isEqualTo(UserState.DELETED);

    AssertJUtil.assertBaseException(
        ClinicExceptionCode.INVALID_REQUEST, () -> doctorService.deleteDoctor(savedDoctor.getId()));
  }

  private Doctor generateDoctor() {
    Doctor doctor = new Doctor();
    doctor.setEmail(String.format("%s@test.com", randomAlphabetic(10)));
    doctor.setFirstName(randomAlphabetic(10));
    doctor.setLastName(randomAlphabetic(10));
    doctor.setPassword(randomAlphabetic(10));
    return doctor;
  }
}
