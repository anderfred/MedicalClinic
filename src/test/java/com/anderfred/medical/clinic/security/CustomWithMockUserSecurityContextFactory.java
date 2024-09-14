package com.anderfred.medical.clinic.security;

import java.util.Arrays;
import java.util.stream.Collectors;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class CustomWithMockUserSecurityContextFactory
    implements WithSecurityContextFactory<WithCustomMockUser> {

  @Override
  public SecurityContext createSecurityContext(WithCustomMockUser customUser) {
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    User principal =
        new User(
            customUser.username(),
            customUser.password(),
            Arrays.stream(customUser.roles())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList()));
    context.setAuthentication(
        new CustomAuthenticationToken(
            principal,
            principal.getPassword(),
            principal.getAuthorities(),
            null,
            Long.parseLong(customUser.actorId())));

    return context;
  }
}
