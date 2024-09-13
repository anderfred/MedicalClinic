package com.anderfred.medical.clinic.security;

import com.anderfred.medical.clinic.domain.User;
import com.anderfred.medical.clinic.repository.jpa.UserJpaRepository;
import com.anderfred.medical.clinic.util.MDCUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
  private final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

  private final JwtTokenService jwtTokenProvider;
  private final UserJpaRepository userJpaRepository;

  public JwtAuthenticationFilter(
      JwtTokenService jwtTokenProvider, UserJpaRepository userJpaRepository) {
    this.jwtTokenProvider = jwtTokenProvider;
    this.userJpaRepository = userJpaRepository;
  }

  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request, HttpServletResponse response) {
    String token = request.getHeader("Authorization");
    log.info("auth");
    if (token != null && token.startsWith("Bearer ")) {
      token = token.substring(7);
      String username = jwtTokenProvider.extractUsername(token);
      Collection<SimpleGrantedAuthority> authorities = jwtTokenProvider.getAuthorities(token);
      Long actorId = jwtTokenProvider.extractActorId(token);
      User user = userJpaRepository.findById(actorId).orElseThrow();
      return new CustomAuthenticationToken(username, null, authorities, null, user);
    }
    return null;
  }

  @Override
  protected void successfulAuthentication(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain,
      Authentication authResult)
      throws IOException, ServletException {
      log.info("successful authentication");
    SecurityContext context = SecurityContextHolder.getContext();
    context.setAuthentication(authResult);
    MDCUtil.init(authResult);
    chain.doFilter(request, response);
  }
}
