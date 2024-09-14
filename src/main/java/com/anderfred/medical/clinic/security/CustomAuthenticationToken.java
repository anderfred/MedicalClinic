package com.anderfred.medical.clinic.security;

import com.anderfred.medical.clinic.domain.user.User;
import java.util.Collection;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class CustomAuthenticationToken extends UsernamePasswordAuthenticationToken {
  private UserRole role;
  private User user;

  public CustomAuthenticationToken(
      Object principal, Object credentials, UserRole role) {
    super(principal, credentials);
    this.role = role;
  }

  public CustomAuthenticationToken(
      Object principal,
      Object credentials,
      Collection<? extends GrantedAuthority> authorities,
      UserRole role,
      User user) {
    super(principal, credentials, authorities);
    this.role = role;
    this.user = user;
  }

  public UserRole getRole() {
    return role;
  }

  public User getUser() {
    return user;
  }
}
