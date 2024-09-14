package com.anderfred.medical.clinic.service;

import static com.anderfred.medical.clinic.domain.user.Doctor.INITIAL_DOCTOR_ID;
import static com.anderfred.medical.clinic.util.IdUtils.randomLong;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

import com.anderfred.medical.clinic.base.BaseIT;
import com.anderfred.medical.clinic.domain.user.Doctor;
import com.anderfred.medical.clinic.domain.user.UserState;
import com.anderfred.medical.clinic.exceptions.ClinicExceptionCode;
import com.anderfred.medical.clinic.repository.jpa.DoctorJpaRepository;
import com.anderfred.medical.clinic.security.WithCustomMockUser;
import com.anderfred.medical.clinic.util.AssertJUtil;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

public class DoctorServiceIT extends BaseIT {
  @Autowired private DoctorJpaRepository repository;
  @Autowired private DoctorService doctorService;
  @Autowired private PasswordEncoder passwordEncoder;

  public static final String INITIAL_USER_EMAIL = "admin@test.com";
  public static final String INITIAL_USER_NAME = "admin";
  public static final String INITIAL_USER_PASSWORD = "12345";

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
    assertThat(doctor.getLastLoginDate()).isNotNull();
    assertThat(doctor.getLastModifiedDate()).isNotNull();
    assertThat(doctor.getPassword()).isNotNull();
    assertThat(passwordEncoder.matches(INITIAL_USER_PASSWORD, doctor.getPassword())).isTrue();
    assertThat(doctor.getLastModifiedBy()).isNotNull();
    assertThat(doctor.getLastModifiedDate()).isNotNull();
  }

  @Test
  @WithCustomMockUser(username = "user")
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
    assertThat(savedDoctor.getLastLoginDate()).isNotNull();

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
    assertThat(savedDoctor2.getLastLoginDate()).isNotNull();

    Doctor found = doctorService.findById(savedDoctor2.getId());
    assertThat(found.getId()).isEqualTo(doctor2.getId());

    AssertJUtil.assertBaseException(
        ClinicExceptionCode.ENTITY_NOT_FOUND, () -> doctorService.findById(randomLong()));
  }

  @Test
  @WithCustomMockUser(username = "user")
  public void shouldNotCreateDoctorIfEmailAlreadyExists() {
    Doctor doctor = generateDoctor();
    Doctor savedDoctor = repository.save(doctor);
    assertThat(savedDoctor.getId()).isNotNull();

    Doctor doctorWithSameEmail = (Doctor) generateDoctor().setEmail(doctor.getEmail());
    assertThrows(DataIntegrityViolationException.class, () -> repository.save(doctorWithSameEmail));
  }

  @Test
  @WithCustomMockUser(username = "user")
  public void shouldRegisterDoctor() {
    Doctor doctor = generateDoctor();
    Doctor savedDoctor = doctorService.registerDoctor(doctor);
    assertThat(savedDoctor.getId()).isNotNull();
    assertThat(savedDoctor.getFirstName()).isEqualTo(doctor.getFirstName());
    assertThat(savedDoctor.getLastName()).isEqualTo(doctor.getLastName());
    assertThat(savedDoctor.getEmail()).isEqualTo(doctor.getEmail());
    assertThat(savedDoctor.getLastModifiedDate()).isNotNull();
    assertThat(savedDoctor.getLastModifiedBy()).isNotNull();
    assertThat(savedDoctor.getCreatedDate()).isNotNull();
    assertThat(savedDoctor.getCreatedBy()).isNotNull();
    assertThat(savedDoctor.getLastLoginDate()).isNotNull();
    assertThat(repository.findById(savedDoctor.getId())).isNotEmpty();
  }

  @Test
  @WithCustomMockUser(username = "user")
  public void shouldUpdateDoctor() {
    Doctor doctor = generateDoctor();
    Doctor savedDoctor = doctorService.registerDoctor(doctor);
    assertThat(savedDoctor.getId()).isNotNull();
    assertThat(savedDoctor.getFirstName()).isEqualTo(doctor.getFirstName());
    assertThat(savedDoctor.getLastName()).isEqualTo(doctor.getLastName());
    assertThat(savedDoctor.getEmail()).isEqualTo(doctor.getEmail());
    assertThat(savedDoctor.getLastModifiedDate()).isNotNull();
    assertThat(savedDoctor.getLastModifiedBy()).isNotNull();
    assertThat(savedDoctor.getCreatedDate()).isNotNull();
    assertThat(savedDoctor.getCreatedBy()).isNotNull();
    assertThat(savedDoctor.getLastLoginDate()).isNotNull();
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
  @WithCustomMockUser(username = "user")
  public void shouldNotUpdateDoctor() {
    Doctor doctor = generateDoctor();
    Doctor savedDoctor = doctorService.registerDoctor(doctor);
    assertThat(savedDoctor.getId()).isNotNull();
    assertThat(savedDoctor.getFirstName()).isEqualTo(doctor.getFirstName());
    assertThat(savedDoctor.getLastName()).isEqualTo(doctor.getLastName());
    assertThat(savedDoctor.getEmail()).isEqualTo(doctor.getEmail());
    assertThat(savedDoctor.getLastModifiedDate()).isNotNull();
    assertThat(savedDoctor.getLastModifiedBy()).isNotNull();
    assertThat(savedDoctor.getCreatedDate()).isNotNull();
    assertThat(savedDoctor.getCreatedBy()).isNotNull();
    assertThat(savedDoctor.getLastLoginDate()).isNotNull();
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
  @WithCustomMockUser(username = "user")
  public void shouldDeleteDoctor() {
    Doctor doctor = generateDoctor();
    Doctor savedDoctor = doctorService.registerDoctor(doctor);
    assertThat(savedDoctor.getId()).isNotNull();
    assertThat(savedDoctor.getFirstName()).isEqualTo(doctor.getFirstName());
    assertThat(savedDoctor.getLastName()).isEqualTo(doctor.getLastName());
    assertThat(savedDoctor.getEmail()).isEqualTo(doctor.getEmail());
    assertThat(savedDoctor.getLastModifiedDate()).isNotNull();
    assertThat(savedDoctor.getLastModifiedBy()).isNotNull();
    assertThat(savedDoctor.getCreatedDate()).isNotNull();
    assertThat(savedDoctor.getCreatedBy()).isNotNull();
    assertThat(savedDoctor.getLastLoginDate()).isNotNull();
    assertThat(repository.findById(savedDoctor.getId())).isNotEmpty();

    doctorService.deleteDoctor(savedDoctor.getId());
    Doctor persisted = repository.findById(savedDoctor.getId()).orElseThrow();
    assertThat(persisted.getState()).isEqualTo(UserState.DELETED);
  }

  @Test
  @WithCustomMockUser(username = "user")
  public void shouldNotDeleteDoctorAlreadyDeleted() {
    Doctor doctor = generateDoctor();
    Doctor savedDoctor = doctorService.registerDoctor(doctor);
    assertThat(savedDoctor.getId()).isNotNull();
    assertThat(savedDoctor.getFirstName()).isEqualTo(doctor.getFirstName());
    assertThat(savedDoctor.getLastName()).isEqualTo(doctor.getLastName());
    assertThat(savedDoctor.getEmail()).isEqualTo(doctor.getEmail());
    assertThat(savedDoctor.getLastModifiedBy()).isNotNull();
    assertThat(savedDoctor.getCreatedDate()).isNotNull();
    assertThat(savedDoctor.getCreatedBy()).isNotNull();
    assertThat(savedDoctor.getLastLoginDate()).isNotNull();
    assertThat(repository.findById(savedDoctor.getId())).isNotEmpty();

    doctorService.deleteDoctor(savedDoctor.getId());
    Doctor persisted = repository.findById(savedDoctor.getId()).orElseThrow();
    assertThat(persisted.getState()).isEqualTo(UserState.DELETED);

    AssertJUtil.assertBaseException(
        ClinicExceptionCode.INVALID_REQUEST, () -> doctorService.deleteDoctor(savedDoctor.getId()));
  }

  @Test
  @WithCustomMockUser(username = "user")
  public void shouldFindPageOfDoctors() {
    // Clear all in db except initial doctor
    repository.findAll().stream()
        .filter(d -> !d.getId().equals(INITIAL_DOCTOR_ID))
        .forEach(dr -> repository.delete(dr));

    Doctor first = generateDoctor("aa", "aaaaaaa");
    Doctor second = generateDoctor("aa", "aaaaaaab");
    Doctor third = generateDoctor("ab", "aaaaaaab");

    Long firstId = doctorService.registerDoctor(first).getId();
    Long secondId = doctorService.registerDoctor(second).getId();
    Long thirdId = doctorService.registerDoctor(third).getId();

    final long toCreateCount = 50L;
    final int pageSize = 20;

    // Total created + created for check sorting (3) + initial admin 1
    final long totalCount = toCreateCount + 3L + 1L;

    for (int i = 1; i <= toCreateCount; i++) {
      doctorService.registerDoctor(generateDoctor());
    }

    PageRequest pageRequest = PageRequest.of(0, pageSize);
    Page<Doctor> page1 = doctorService.findPage(pageRequest);

    assertThat(page1.getTotalElements()).isEqualTo(totalCount);
    List<Doctor> content1 = page1.getContent();
    Iterator<Doctor> iterator = content1.iterator();
    assertThat(iterator.next().getId()).isEqualTo(firstId);
    assertThat(iterator.next().getId()).isEqualTo(secondId);
    assertThat(iterator.next().getId()).isEqualTo(thirdId);
    assertThat(content1.size()).isEqualTo(pageSize);

    PageRequest pageRequest2 = PageRequest.of(2, pageSize);
    Page<Doctor> page2 = doctorService.findPage(pageRequest2);
    assertThat(page2.getContent().size()).isEqualTo(totalCount - (pageSize) * 2);
  }

  public static Doctor generateDoctor() {
    return generateDoctor(randomAlphabetic(10), randomAlphabetic(10));
  }

  public static Doctor generateDoctor(String name, String lastName) {
    Doctor doctor = new Doctor();
    doctor.setEmail(String.format("%s@test.com", randomAlphabetic(10)));
    doctor.setFirstName(name);
    doctor.setLastName(lastName);
    doctor.setPassword(randomAlphabetic(10));
    return doctor;
  }

  public Doctor createPersistedDoctor() {
    Doctor doctor = generateDoctor();
    return doctorService.registerDoctor(doctor);
  }
}
