package com.anderfred.medical.clinic.domain.auth;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class AuthRequest {
  private String username;
  private String password;

  public String getUsername() {
    return username;
  }

  public AuthRequest setUsername(String username) {
    this.username = username;
    return this;
  }

  public String getPassword() {
    return password;
  }

  public AuthRequest setPassword(String password) {
    this.password = password;
    return this;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
        .append("username", getUsername())
        .toString();
  }

  public static AuthRequest of(String username, String password) {
      AuthRequest authRequest = new AuthRequest();
      authRequest.setUsername(username);
      authRequest.setPassword(password);
      return authRequest;
  }
}
