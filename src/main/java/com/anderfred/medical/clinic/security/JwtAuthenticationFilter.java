package com.anderfred.medical.clinic.security;

import com.anderfred.medical.clinic.util.MDCUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private final JwtTokenService jwtTokenProvider;

  public JwtAuthenticationFilter(
      JwtTokenService jwtTokenProvider) {
    this.jwtTokenProvider = jwtTokenProvider;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String token = request.getHeader("Authorization");
    if (token != null && token.startsWith("Bearer ")) {
      token = token.substring(7);
      String username = jwtTokenProvider.extractUsername(token);
      Collection<SimpleGrantedAuthority> authorities = jwtTokenProvider.getAuthorities(token);
      Long actorId = jwtTokenProvider.extractActorId(token);
      Authentication authentication =
          new CustomAuthenticationToken(username, null, authorities, null, actorId);
      SecurityContextHolder.getContext().setAuthentication(authentication);
      MDCUtil.init(authentication);
    }
    filterChain.doFilter(request, response);
  }
}
