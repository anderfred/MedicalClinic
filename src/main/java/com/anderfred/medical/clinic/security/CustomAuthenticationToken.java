package com.anderfred.medical.clinic.security;

import java.util.Collection;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class CustomAuthenticationToken extends UsernamePasswordAuthenticationToken {
  private UserRole role;
  private Long actorId;

  public CustomAuthenticationToken(Object principal, Object credentials, UserRole role) {
    super(principal, credentials);
    this.role = role;
  }

  public CustomAuthenticationToken(
      Object principal,
      Object credentials,
      Collection<? extends GrantedAuthority> authorities,
      UserRole role,
      Long actorId) {
    super(principal, credentials, authorities);
    this.role = role;
    this.actorId = actorId;
  }

  public UserRole getRole() {
    return role;
  }

  public Long getActorId() {
    return actorId;
  }
}
