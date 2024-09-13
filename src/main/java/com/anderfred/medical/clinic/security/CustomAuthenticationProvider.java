package com.anderfred.medical.clinic.security;

import com.anderfred.medical.clinic.domain.User;
import com.anderfred.medical.clinic.exceptions.AccessDeniedException;
import com.anderfred.medical.clinic.exceptions.ClinicExceptionCode;
import com.anderfred.medical.clinic.repository.jpa.DoctorJpaRepository;
import com.anderfred.medical.clinic.repository.jpa.PatientJpaRepository;
import com.anderfred.medical.clinic.util.MDCUtil;
import java.util.Set;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CustomAuthenticationProvider implements AuthenticationProvider {
  private final DoctorJpaRepository doctorJpaRepository;
  private final PatientJpaRepository patientJpaRepository;
  private final PasswordEncoder passwordEncoder;

  private final SimpleGrantedAuthority doctorAuthority =
      new SimpleGrantedAuthority(UserRole.DOCTOR.getDescription());
  private final SimpleGrantedAuthority patientAuthority =
      new SimpleGrantedAuthority(UserRole.PATIENT.getDescription());

  public CustomAuthenticationProvider(
      DoctorJpaRepository doctorJpaRepository,
      PatientJpaRepository patientJpaRepository,
      PasswordEncoder passwordEncoder) {
    this.doctorJpaRepository = doctorJpaRepository;
    this.patientJpaRepository = patientJpaRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    if (!(authentication instanceof CustomAuthenticationToken authToken)) {
      return null;
    }
    String username = (String) authToken.getPrincipal();
    UserRole role = authToken.getRole();
    boolean doctorLogin = UserRole.DOCTOR.equals(role);
    User user =
        doctorLogin
            ? doctorJpaRepository
                .findByEmail(username)
                .orElseThrow(
                    () ->
                        new AccessDeniedException(
                            "User not found", ClinicExceptionCode.ENTITY_NOT_FOUND))
            : patientJpaRepository
                .findByEmail(username)
                .orElseThrow(
                    () ->
                        new AccessDeniedException(
                            "User not found", ClinicExceptionCode.ENTITY_NOT_FOUND));
    UserDetails userDetails =
        new org.springframework.security.core.userdetails.User(
            user.getEmail(),
            user.getPassword(),
            Set.of(doctorLogin ? doctorAuthority : patientAuthority));
    if (!passwordEncoder.matches(authentication.getCredentials().toString(), user.getPassword())) {
      throw new AccessDeniedException("Access denied");
    }
    MDCUtil.init(authentication);
    return new CustomAuthenticationToken(
        userDetails, userDetails.getPassword(), userDetails.getAuthorities(), role, user);
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return CustomAuthenticationToken.class.isAssignableFrom(authentication);
  }
}
