package com.anderfred.medical.clinic.config;

import com.anderfred.medical.clinic.repository.jpa.DoctorJpaRepository;
import com.anderfred.medical.clinic.repository.jpa.PatientJpaRepository;
import com.anderfred.medical.clinic.security.CustomAuthenticationProvider;
import com.anderfred.medical.clinic.security.JwtAuthenticationFilter;
import com.anderfred.medical.clinic.security.JwtTokenService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {
  private final DoctorJpaRepository doctorJpaRepository;
  private final PatientJpaRepository patientJpaRepository;
  private final JwtTokenService jwtTokenService;

  public SecurityConfig(
      com.anderfred.medical.clinic.repository.jpa.DoctorJpaRepository doctorJpaRepository,
      PatientJpaRepository patientJpaRepository,
      JwtTokenService jwtTokenService) {
    this.doctorJpaRepository = doctorJpaRepository;
    this.patientJpaRepository = patientJpaRepository;
    this.jwtTokenService = jwtTokenService;
  }

  @Bean
  public SecurityFilterChain doctorSecurityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers("/api/auth/**")
                    .permitAll()
                    .requestMatchers("/api/**")
                    .authenticated())
        .formLogin(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .authenticationProvider(customAuthenticationProvider())
        .addFilterBefore(
            new JwtAuthenticationFilter(jwtTokenService),
            UsernamePasswordAuthenticationFilter.class);
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
