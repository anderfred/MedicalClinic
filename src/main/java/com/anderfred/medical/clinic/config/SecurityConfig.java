package com.anderfred.medical.clinic.config;

import com.anderfred.medical.clinic.repository.jpa.DoctorJpaRepository;
import com.anderfred.medical.clinic.repository.jpa.PatientJpaRepository;
import com.anderfred.medical.clinic.repository.jpa.UserJpaRepository;
import com.anderfred.medical.clinic.security.CustomAuthenticationProvider;
import com.anderfred.medical.clinic.security.JwtTokenService;
import com.anderfred.medical.clinic.security.UserRole;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
  private final DoctorJpaRepository doctorJpaRepository;
  private final PatientJpaRepository patientJpaRepository;
  private final JwtTokenService jwtTokenService;
  private final UserJpaRepository userJpaRepository;

  public SecurityConfig(
      com.anderfred.medical.clinic.repository.jpa.DoctorJpaRepository doctorJpaRepository,
      PatientJpaRepository patientJpaRepository,
      JwtTokenService jwtTokenService,
      UserJpaRepository userJpaRepository) {
    this.doctorJpaRepository = doctorJpaRepository;
    this.patientJpaRepository = patientJpaRepository;
    this.jwtTokenService = jwtTokenService;
    this.userJpaRepository = userJpaRepository;
  }

  @Bean
  public SecurityFilterChain doctorSecurityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers("/api/auth/**")
                    .permitAll()
                    .requestMatchers("/api/doctor/**")
                    .hasAuthority(UserRole.DOCTOR.getDescription())
                    .requestMatchers("/api/patient/**")
                    .hasAuthority(UserRole.PATIENT.getDescription())
                    .requestMatchers("/api/**")
                    .authenticated())
        .formLogin(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .authenticationProvider(customAuthenticationProvider());
    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationProvider customAuthenticationProvider() {
    return new CustomAuthenticationProvider(
        doctorJpaRepository, patientJpaRepository, passwordEncoder());
  }
}
