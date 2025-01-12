package com.anderfred.medical.clinic.service;

import static com.anderfred.medical.clinic.security.JwtTokenService.TOKEN_KEY;
import static com.anderfred.medical.clinic.service.DoctorServiceIT.INITIAL_USER_EMAIL;
import static com.anderfred.medical.clinic.util.IdUtils.randomLong;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.anderfred.medical.clinic.base.BaseIT;
import com.anderfred.medical.clinic.domain.audit.Audit;
import com.anderfred.medical.clinic.domain.audit.EntityType;
import com.anderfred.medical.clinic.domain.auth.AuthRequest;
import com.anderfred.medical.clinic.domain.user.Doctor;
import com.anderfred.medical.clinic.domain.user.Patient;
import com.anderfred.medical.clinic.repository.jpa.AuditJpaRepository;
import com.anderfred.medical.clinic.repository.jpa.DoctorJpaRepository;
import com.anderfred.medical.clinic.security.JwtTokenService;
import com.anderfred.medical.clinic.security.WithCustomMockUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@AutoConfigureMockMvc
public class AuthServiceIT extends BaseIT {
  @Autowired private AuthService authService;
  @Autowired private DoctorService doctorService;
  @Autowired private DoctorJpaRepository doctorJpaRepository;
  @Autowired private PatientService patientService;
  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  @Autowired private JwtTokenService jwtTokenService;
  @Autowired private AuditJpaRepository auditJpaRepository;

  @Test
  @WithCustomMockUser(username = "user")
  public void shouldAuthenticateDoctor() throws Exception {
    Doctor doctor = DoctorServiceIT.generateDoctor();
    final String password = doctor.getPassword();
    Doctor persisted = doctorService.registerDoctor(doctor);
    SecurityContextHolder.clearContext();

    AuthRequest authRequest = AuthRequest.of(doctor.getEmail(), password);

    MvcResult result =
        mockMvc
            .perform(
                post("/api/auth/doctor-login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(authRequest)))
            .andExpect(status().isOk())
            .andReturn();
    MockHttpServletResponse response = result.getResponse();

    String jwtCookie = response.getCookie(TOKEN_KEY).getValue();
    assertThat(jwtTokenService.validateToken(jwtCookie)).isTrue();
    assertThat(jwtTokenService.extractUsername(jwtCookie)).isEqualTo(doctor.getEmail());
    assertThat(jwtTokenService.extractActorId(jwtCookie)).isEqualTo(persisted.getId());
    Date issuedAt = jwtTokenService.getIssuedAtDateFromToken(jwtCookie);
    Date expiration = jwtTokenService.getExpirationDateFromToken(jwtCookie);

    long diffInMillis = expiration.getTime() - issuedAt.getTime();

    // 24 hours = 24 * 60 * 60 * 1000 milliseconds
    long twentyFourHoursInMillis = TimeUnit.HOURS.toMillis(24);
    assertThat(diffInMillis == twentyFourHoursInMillis).isTrue();

    // Check audit record created
    Page<Audit> auditRecords =
        auditJpaRepository.findByActorType(
            doctor.getId(), PageRequest.of(0, 20), EntityType.DOCTOR);
    assertThat(auditRecords.getTotalElements() > 0).isTrue();
  }

  @Test
  @WithCustomMockUser(username = "user")
  public void shouldAuthenticatePatient() throws Exception {
    Patient patient = PatientServiceIT.generatePatient();
    final String password = patient.getPassword();
    Patient persisted = patientService.registerPatient(patient);
    SecurityContextHolder.clearContext();

    AuthRequest authRequest = AuthRequest.of(patient.getEmail(), password);

    MvcResult result =
        mockMvc
            .perform(
                post("/api/auth/patient-login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(authRequest)))
            .andExpect(status().isOk())
            .andReturn();
    MockHttpServletResponse response = result.getResponse();

    String jwtCookie = response.getCookie(TOKEN_KEY).getValue();
    assertThat(jwtTokenService.validateToken(jwtCookie)).isTrue();
    assertThat(jwtTokenService.extractUsername(jwtCookie)).isEqualTo(patient.getEmail());
    assertThat(jwtTokenService.extractActorId(jwtCookie)).isEqualTo(persisted.getId());
    Date issuedAt = jwtTokenService.getIssuedAtDateFromToken(jwtCookie);
    Date expiration = jwtTokenService.getExpirationDateFromToken(jwtCookie);

    long diffInMillis = expiration.getTime() - issuedAt.getTime();

    // 24 hours = 24 * 60 * 60 * 1000 milliseconds
    long twentyFourHoursInMillis = TimeUnit.HOURS.toMillis(24);
    assertThat(diffInMillis == twentyFourHoursInMillis).isTrue();
  }

  @Test
  @WithCustomMockUser(username = "user")
  public void shouldAuthenticateUsersWithSameEmail() throws Exception {
    Patient patient = PatientServiceIT.generatePatient();
    final String password = patient.getPassword();
    Patient persisted = patientService.registerPatient(patient);

    // Set the same email to doctor
    Doctor doctor = DoctorServiceIT.generateDoctor();
    doctor.setEmail(persisted.getEmail());
    final String passwordDoc = doctor.getPassword();
    Doctor persistedDoc = doctorService.registerDoctor(doctor);
    SecurityContextHolder.clearContext();

    AuthRequest authRequest = AuthRequest.of(patient.getEmail(), password);

    mockMvc
        .perform(
            post("/api/auth/patient-login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest)))
        .andExpect(status().isOk())
        .andReturn();

    authRequest = AuthRequest.of(doctor.getEmail(), passwordDoc);
    mockMvc
        .perform(
            post("/api/auth/doctor-login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest)))
        .andExpect(status().isOk())
        .andReturn();
  }

  @Test
  public void shouldNotAuthenticateUsers() throws Exception {
    // User does not exists
    AuthRequest authRequest = AuthRequest.of(randomAlphabetic(10), randomAlphabetic(10));

    mockMvc
        .perform(
            post("/api/auth/patient-login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest)))
        .andExpect(status().isForbidden())
        .andReturn();

    mockMvc
        .perform(
            post("/api/auth/doctor-login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest)))
        .andExpect(status().isForbidden())
        .andReturn();

    // Invalid password
    Doctor persisted = doctorJpaRepository.findByEmail(INITIAL_USER_EMAIL).orElseThrow();
    authRequest = AuthRequest.of(persisted.getEmail(), randomAlphabetic(10));
    mockMvc
        .perform(
            post("/api/auth/doctor-login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest)))
        .andExpect(status().isForbidden())
        .andReturn();
  }

  @Test
  public void shouldDenyAccessWithoutToken() throws Exception {
    mockMvc
        .perform(delete("/api/patient/" + randomLong()).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());

    mockMvc
        .perform(delete("/api/doctor/" + randomLong()).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());
  }

  @Test
  public void shouldAuthenticateInitialDoctor() throws Exception {
    Doctor doctor = doctorJpaRepository.findByEmail(INITIAL_USER_EMAIL).orElseThrow();
    AuthRequest authRequest =
        AuthRequest.of(doctor.getEmail(), DoctorServiceIT.INITIAL_USER_PASSWORD);
    MvcResult result =
        mockMvc
            .perform(
                post("/api/auth/doctor-login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(authRequest)))
            .andExpect(status().isOk())
            .andReturn();
    MockHttpServletResponse response = result.getResponse();

    String jwtCookie = response.getCookie(TOKEN_KEY).getValue();
    assertThat(jwtTokenService.validateToken(jwtCookie)).isTrue();
    assertThat(jwtTokenService.extractUsername(jwtCookie)).isEqualTo(doctor.getEmail());
    assertThat(jwtTokenService.extractActorId(jwtCookie)).isEqualTo(doctor.getId());
    Date issuedAt = jwtTokenService.getIssuedAtDateFromToken(jwtCookie);
    Date expiration = jwtTokenService.getExpirationDateFromToken(jwtCookie);

    long diffInMillis = expiration.getTime() - issuedAt.getTime();

    // 24 hours = 24 * 60 * 60 * 1000 milliseconds
    long twentyFourHoursInMillis = TimeUnit.HOURS.toMillis(24);
    assertThat(diffInMillis == twentyFourHoursInMillis).isTrue();
  }

  @Test
  @WithCustomMockUser(username = "user")
  public void doctorShouldNotHaveAccessToPatientEndpoints() throws Exception {
    Doctor doctor = DoctorServiceIT.generateDoctor();
    final String password = doctor.getPassword();
    Doctor persisted = doctorService.registerDoctor(doctor);
    SecurityContextHolder.clearContext();

    AuthRequest authRequest = AuthRequest.of(doctor.getEmail(), password);
    MvcResult result =
        mockMvc
            .perform(
                post("/api/auth/doctor-login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(authRequest)))
            .andExpect(status().isOk())
            .andReturn();
    MockHttpServletResponse response = result.getResponse();

    String token = response.getCookie(TOKEN_KEY).getValue();

    mockMvc
        .perform(
            get("/api/medical-exam/exams-list?patientId=" + randomLong())
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest)))
        .andExpect(status().isForbidden())
        .andReturn();

    mockMvc
        .perform(
                get("/api/medical-exam/exams-list-pdf?patientId=" + randomLong())
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest)))
        .andExpect(status().isForbidden())
        .andReturn();
  }

  @Test
  @WithCustomMockUser(username = "user")
  public void patientShouldNotHaveAccessToDoctorEndpoints() throws Exception {
    Patient patient = PatientServiceIT.generatePatient();
    final String password = patient.getPassword();
    Patient persisted = patientService.registerPatient(patient);
    SecurityContextHolder.clearContext();

    AuthRequest authRequest = AuthRequest.of(patient.getEmail(), password);
    MvcResult result =
            mockMvc
                    .perform(
                            post("/api/auth/patient-login")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(authRequest)))
                    .andExpect(status().isOk())
                    .andReturn();
    MockHttpServletResponse response = result.getResponse();

    String token = response.getCookie(TOKEN_KEY).getValue();

    //Allowed
    mockMvc
            .perform(
                    get("/api/medical-exam/exams-list?patientId=" + randomLong())
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(authRequest)))
            .andExpect(status().isOk());

    mockMvc
            .perform(
                    get("/api/medical-exam/exams-list-pdf?patientId=" + randomLong())
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(authRequest)))
            .andExpect(status().isBadRequest());

    //Forbidden
    mockMvc
            .perform(
                    get("/api/patient/" + randomLong())
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(authRequest)))
            .andExpect(status().isForbidden())
            .andReturn();
    mockMvc
            .perform(
                    delete("/api/patient/" + randomLong())
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(authRequest)))
            .andExpect(status().isForbidden())
            .andReturn();
    mockMvc
            .perform(
                    get("/api/doctor/" + randomLong())
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(authRequest)))
            .andExpect(status().isForbidden())
            .andReturn();
    mockMvc
            .perform(
                    delete("/api/doctor/" + randomLong())
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(authRequest)))
            .andExpect(status().isForbidden())
            .andReturn();
    mockMvc
            .perform(
                    delete("/api/appointment/" + randomLong())
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(authRequest)))
            .andExpect(status().isForbidden())
            .andReturn();
    mockMvc
            .perform(
                    get("/api/appointment/" + randomLong())
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(authRequest)))
            .andExpect(status().isForbidden())
            .andReturn();
  }
}
