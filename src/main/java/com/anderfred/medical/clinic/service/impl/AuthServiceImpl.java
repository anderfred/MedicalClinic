package com.anderfred.medical.clinic.service.impl;

import static com.anderfred.medical.clinic.security.JwtTokenService.TOKEN_KEY;

import com.anderfred.medical.clinic.domain.User;
import com.anderfred.medical.clinic.domain.auth.AuthRequest;
import com.anderfred.medical.clinic.repository.jpa.UserJpaRepository;
import com.anderfred.medical.clinic.security.CustomAuthenticationToken;
import com.anderfred.medical.clinic.security.UserRole;
import com.anderfred.medical.clinic.service.AuthService;
import com.anderfred.medical.clinic.security.JwtTokenService;
import com.anderfred.medical.clinic.util.MappingUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;

@Service
public class AuthServiceImpl implements AuthService {
  private final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

  private final JwtTokenService jwtTokenService;
  private final AuthenticationManager authenticationManager;
  private final ObjectMapper mapper;
  private final UserJpaRepository userJpaRepository;

  public AuthServiceImpl(
          JwtTokenService jwtTokenService, AuthenticationManager authenticationManager, ObjectMapper mapper, UserJpaRepository userJpaRepository) {
    this.jwtTokenService = jwtTokenService;
    this.authenticationManager = authenticationManager;
    this.mapper = mapper;
    this.userJpaRepository = userJpaRepository;
  }

  @Override
  @Transactional
  public User authenticateDoctor(AuthRequest authRequest, HttpServletResponse response) {
    log.debug("Authenticating doctor user:[{}]", authRequest);
    User user = innerAuthenticateUser(authRequest, response, UserRole.DOCTOR);
    log.debug("Authenticated doctor user:[{}]", user);
    return user;
  }

  @Override
  @Transactional
  public User authenticatePatient(AuthRequest authRequest, HttpServletResponse response) {
    log.debug("Authenticating patient user:[{}]", authRequest);
    User user = innerAuthenticateUser(authRequest, response, UserRole.PATIENT);
    log.debug("Authenticated patient user:[{}]", user);
    return user;
  }

  private User innerAuthenticateUser(
      AuthRequest authRequest, HttpServletResponse response, UserRole role) {
    CustomAuthenticationToken authentication =
        (CustomAuthenticationToken)
            authenticationManager.authenticate(
                new CustomAuthenticationToken(
                    authRequest.getUsername(), authRequest.getPassword(), role));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    enrichResponse(response, authentication);
    authentication.getUser().setLastLoginDate(Instant.now());
    userJpaRepository.save(authentication.getUser());
    return MappingUtil.copy(mapper, authentication.getUser()).removeSensitiveData();
  }

  private void enrichResponse(
      HttpServletResponse response, CustomAuthenticationToken authentication) {
    String jwtToken =
        jwtTokenService.generateToken(
            authentication.getUser().getEmail(),
            authentication.getAuthorities(),
            authentication.getUser().getId());
    Cookie jwtCookie = new Cookie(TOKEN_KEY, jwtToken);
    // TODO to configuration
    jwtCookie.setHttpOnly(false);
    jwtCookie.setSecure(false);
    jwtCookie.setPath("/");
    jwtCookie.setMaxAge(60 * 60 * 24);
    response.addCookie(jwtCookie);
  }
}
