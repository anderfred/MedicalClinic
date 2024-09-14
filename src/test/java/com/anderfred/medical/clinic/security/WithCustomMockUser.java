package com.anderfred.medical.clinic.security;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(
    factory = CustomWithMockUserSecurityContextFactory.class)
public @interface WithCustomMockUser {
  String username() default "doctor";

  String[] roles() default {"DOCTOR_ROLE"};

  String password() default "password";

  String actorId() default "1";
}
