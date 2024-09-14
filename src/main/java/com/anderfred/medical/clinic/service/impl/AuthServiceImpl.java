package com.anderfred.medical.clinic.service.impl;

import static com.anderfred.medical.clinic.security.JwtTokenService.TOKEN_KEY;

import com.anderfred.medical.clinic.domain.audit.ActionType;
import com.anderfred.medical.clinic.domain.audit.EntityType;
import com.anderfred.medical.clinic.domain.auth.AuthRequest;
import com.anderfred.medical.clinic.domain.user.User;
import com.anderfred.medical.clinic.repository.jpa.UserJpaRepository;
import com.anderfred.medical.clinic.security.CustomAuthenticationToken;
import com.anderfred.medical.clinic.security.JwtTokenService;
import com.anderfred.medical.clinic.security.UserRole;
import com.anderfred.medical.clinic.service.AuthService;
import com.anderfred.medical.clinic.util.MappingUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {
  private final static Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

  private final JwtTokenService jwtTokenService;
  private final AuthenticationManager authenticationManager;
  private final ObjectMapper mapper;
  private final UserJpaRepository userJpaRepository;
  private final AuditService auditService;

  public AuthServiceImpl(
      JwtTokenService jwtTokenService,
      AuthenticationManager authenticationManager,
      ObjectMapper mapper,
      UserJpaRepository userJpaRepository,
      AuditService auditService) {
    this.jwtTokenService = jwtTokenService;
    this.authenticationManager = authenticationManager;
    this.mapper = mapper;
    this.userJpaRepository = userJpaRepository;
    this.auditService = auditService;
  }

  @Override
  @Transactional
  public User authenticateDoctor(AuthRequest authRequest, HttpServletResponse response) {
    log.debug("Authenticating doctor user:[{}]", authRequest);
    User user = innerAuthenticateUser(authRequest, response, UserRole.DOCTOR);
    log.debug("Authenticated doctor user:[{}]", user);
    auditService.createAuditRecord(EntityType.DOCTOR, ActionType.LOGIN, user.getId());
    return user;
  }

  @Override
  @Transactional
  public User authenticatePatient(AuthRequest authRequest, HttpServletResponse response) {
    log.debug("Authenticating patient user:[{}]", authRequest);
    User user = innerAuthenticateUser(authRequest, response, UserRole.PATIENT);
    log.debug("Authenticated patient user:[{}]", user);
    auditService.createAuditRecord(EntityType.PATIENT, ActionType.LOGIN, user.getId());
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
    User user = userJpaRepository.findById(authentication.getActorId()).orElseThrow();
    enrichResponse(response, authentication, user);
    user.setLastLoginDate(Instant.now());
    userJpaRepository.save(user);
    return MappingUtil.copy(mapper, user).removeSensitiveData();
  }

  private void enrichResponse(
      HttpServletResponse response, CustomAuthenticationToken authentication, User user) {
    String jwtToken =
        jwtTokenService.generateToken(
            user.getEmail(), authentication.getAuthorities(), user.getId());
    Cookie jwtCookie = new Cookie(TOKEN_KEY, jwtToken);
    // TODO to configuration
    jwtCookie.setHttpOnly(false);
    jwtCookie.setSecure(false);
    jwtCookie.setPath("/");
    jwtCookie.setMaxAge(60 * 60 * 24);
    response.addCookie(jwtCookie);
  }
}
