package com.anderfred.medical.clinic.service;

import static com.anderfred.medical.clinic.util.IdUtils.randomLong;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

import com.anderfred.medical.clinic.base.BaseIT;
import com.anderfred.medical.clinic.domain.user.Patient;
import com.anderfred.medical.clinic.domain.user.UserState;
import com.anderfred.medical.clinic.exceptions.ClinicExceptionCode;
import com.anderfred.medical.clinic.repository.jpa.PatientJpaRepository;
import com.anderfred.medical.clinic.util.AssertJUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.test.context.support.WithMockUser;

public class PatientServiceIT extends BaseIT {
  @Autowired private PatientJpaRepository repository;
  @Autowired private PatientService service;

  @Test
  @WithMockUser(username = "user")
  public void shouldCreatePatient() {
    Patient patient = generatePatient();
    Patient savedPatient = repository.save(patient);
    assertThat(savedPatient.getId()).isNotNull();
    assertThat(savedPatient.getFirstName()).isEqualTo(patient.getFirstName());
    assertThat(savedPatient.getLastName()).isEqualTo(patient.getLastName());
    assertThat(savedPatient.getEmail()).isEqualTo(patient.getEmail());
    assertThat(savedPatient.getLastModifiedDate()).isNotNull();
    assertThat(savedPatient.getLastModifiedBy()).isNotNull();
    assertThat(savedPatient.getCreatedDate()).isNotNull();
    assertThat(savedPatient.getCreatedBy()).isNotNull();
    assertThat(savedPatient.getLastLoginDate()).isNotNull();

    Patient patient2 = generatePatient();
    Patient savedPatient2 = repository.save(patient2);
    assertThat(savedPatient2.getId()).isNotNull();
    assertThat(savedPatient2.getFirstName()).isEqualTo(patient2.getFirstName());
    assertThat(savedPatient2.getLastName()).isEqualTo(patient2.getLastName());
    assertThat(savedPatient2.getEmail()).isEqualTo(patient2.getEmail());
    assertThat(savedPatient2.getLastModifiedDate()).isNotNull();
    assertThat(savedPatient2.getLastModifiedBy()).isNotNull();
    assertThat(savedPatient2.getCreatedDate()).isNotNull();
    assertThat(savedPatient2.getCreatedBy()).isNotNull();
    assertThat(savedPatient2.getLastLoginDate()).isNotNull();
  }

  @Test
  @WithMockUser(username = "user")
  public void shouldNotCreatePatientIfEmailAlreadyExists() {
    Patient patient = generatePatient();
    Patient savedPatient = repository.save(patient);
    assertThat(savedPatient.getId()).isNotNull();

    Patient patientWithSameEmail = (Patient) generatePatient().setEmail(patient.getEmail());
    assertThrows(
        DataIntegrityViolationException.class, () -> repository.save(patientWithSameEmail));
  }

  @Test
  @WithMockUser(username = "user")
  public void shouldRegisterPatient() {
    Patient patient = generatePatient();
    Patient savedPatient = service.registerPatient(patient);
    assertThat(savedPatient.getId()).isNotNull();
    assertThat(savedPatient.getFirstName()).isEqualTo(patient.getFirstName());
    assertThat(savedPatient.getLastName()).isEqualTo(patient.getLastName());
    assertThat(savedPatient.getEmail()).isEqualTo(patient.getEmail());
    assertThat(savedPatient.getLastModifiedDate()).isNotNull();
    assertThat(savedPatient.getLastModifiedBy()).isNotNull();
    assertThat(savedPatient.getCreatedDate()).isNotNull();
    assertThat(savedPatient.getCreatedBy()).isNotNull();
    assertThat(savedPatient.getLastLoginDate()).isNotNull();
    assertThat(repository.findById(savedPatient.getId())).isNotEmpty();
  }

  @Test
  @WithMockUser(username = "user")
  public void shouldUpdatePatient() {
    Patient patient = generatePatient();
    Patient savedPatient = service.registerPatient(patient);
    assertThat(savedPatient.getId()).isNotNull();
    assertThat(savedPatient.getFirstName()).isEqualTo(patient.getFirstName());
    assertThat(savedPatient.getLastName()).isEqualTo(patient.getLastName());
    assertThat(savedPatient.getEmail()).isEqualTo(patient.getEmail());
    assertThat(savedPatient.getLastModifiedDate()).isNotNull();
    assertThat(savedPatient.getLastModifiedBy()).isNotNull();
    assertThat(savedPatient.getCreatedDate()).isNotNull();
    assertThat(savedPatient.getCreatedBy()).isNotNull();
    assertThat(savedPatient.getLastLoginDate()).isNotNull();
    assertThat(repository.findById(savedPatient.getId())).isNotEmpty();

    Patient updated = generatePatient();
    updated.setState(UserState.DELETED);
    updated.setId(savedPatient.getId());

    service.updatePatient(updated);
    Patient actual = repository.findById(savedPatient.getId()).orElseThrow();
    assertThat(actual.getLastName()).isEqualTo(updated.getLastName());
    assertThat(actual.getFirstName()).isEqualTo(updated.getFirstName());
    assertThat(actual.getEmail()).isEqualTo(updated.getEmail());

    // Fields must not updates
    assertThat(actual.getLastModifiedDate()).isNotEqualTo(updated.getLastModifiedDate());
    assertThat(actual.getPassword()).isNotEqualTo(updated.getPassword());
    assertThat(actual.getState()).isNotEqualTo(updated.getState());
  }

  @Test
  @WithMockUser(username = "user")
  public void shouldNotUpdatePatient() {
    Patient patient = generatePatient();
    Patient savedPatient = service.registerPatient(patient);
    assertThat(savedPatient.getId()).isNotNull();
    assertThat(savedPatient.getFirstName()).isEqualTo(patient.getFirstName());
    assertThat(savedPatient.getLastName()).isEqualTo(patient.getLastName());
    assertThat(savedPatient.getEmail()).isEqualTo(patient.getEmail());
    assertThat(savedPatient.getLastModifiedDate()).isNotNull();
    assertThat(savedPatient.getLastModifiedBy()).isNotNull();
    assertThat(savedPatient.getCreatedDate()).isNotNull();
    assertThat(savedPatient.getCreatedBy()).isNotNull();
    assertThat(savedPatient.getLastLoginDate()).isNotNull();
    assertThat(repository.findById(savedPatient.getId())).isNotEmpty();

    Patient withEmailDuplicate = generatePatient();
    service.registerPatient(withEmailDuplicate);

    Patient updated = generatePatient();
    updated.setEmail(withEmailDuplicate.getEmail());
    updated.setId(savedPatient.getId());

    // Email already taken
    AssertJUtil.assertBaseException(
        ClinicExceptionCode.INVALID_EMAIL, () -> service.updatePatient(updated));
    // Id of patient not found
    AssertJUtil.assertBaseException(
        ClinicExceptionCode.ENTITY_NOT_FOUND,
        () -> service.updatePatient((Patient) updated.setId(randomLong())));
  }

  @Test
  @WithMockUser(username = "user")
  public void shouldDeletePatient() {
    Patient patient = generatePatient();
    Patient savedPatient = service.registerPatient(patient);
    assertThat(savedPatient.getId()).isNotNull();
    assertThat(savedPatient.getFirstName()).isEqualTo(patient.getFirstName());
    assertThat(savedPatient.getLastName()).isEqualTo(patient.getLastName());
    assertThat(savedPatient.getEmail()).isEqualTo(patient.getEmail());
    assertThat(savedPatient.getLastModifiedDate()).isNotNull();
    assertThat(savedPatient.getLastModifiedBy()).isNotNull();
    assertThat(savedPatient.getCreatedDate()).isNotNull();
    assertThat(savedPatient.getCreatedBy()).isNotNull();
    assertThat(savedPatient.getLastLoginDate()).isNotNull();
    assertThat(repository.findById(savedPatient.getId())).isNotEmpty();

    service.deletePatient(savedPatient.getId());
    Patient persisted = repository.findById(savedPatient.getId()).orElseThrow();
    assertThat(persisted.getState()).isEqualTo(UserState.DELETED);
  }

  @Test
  @WithMockUser(username = "user")
  public void shouldNotDeletePatientAlreadyDeleted() {
    Patient patient = generatePatient();
    Patient savedPatient = service.registerPatient(patient);
    assertThat(savedPatient.getId()).isNotNull();
    assertThat(savedPatient.getFirstName()).isEqualTo(patient.getFirstName());
    assertThat(savedPatient.getLastName()).isEqualTo(patient.getLastName());
    assertThat(savedPatient.getEmail()).isEqualTo(patient.getEmail());
    assertThat(savedPatient.getLastModifiedDate()).isNotNull();
    assertThat(savedPatient.getLastModifiedBy()).isNotNull();
    assertThat(savedPatient.getCreatedDate()).isNotNull();
    assertThat(savedPatient.getCreatedBy()).isNotNull();
    assertThat(savedPatient.getLastLoginDate()).isNotNull();
    assertThat(repository.findById(savedPatient.getId())).isNotEmpty();

    service.deletePatient(savedPatient.getId());
    Patient persisted = repository.findById(savedPatient.getId()).orElseThrow();
    assertThat(persisted.getState()).isEqualTo(UserState.DELETED);

    AssertJUtil.assertBaseException(
        ClinicExceptionCode.INVALID_REQUEST, () -> service.deletePatient(savedPatient.getId()));
  }

  public static Patient generatePatient() {
    Patient patient = new Patient();
    patient.setEmail(String.format("%s@test.com", randomAlphabetic(10)));
    patient.setFirstName(randomAlphabetic(10));
    patient.setLastName(randomAlphabetic(10));
    patient.setPassword(randomAlphabetic(10));
    return patient;
  }
}
