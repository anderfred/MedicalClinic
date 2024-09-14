package com.anderfred.medical.clinic.service;

import com.anderfred.medical.clinic.domain.user.User;
import com.anderfred.medical.clinic.domain.auth.AuthRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
  User authenticateDoctor(AuthRequest authRequest, HttpServletResponse response);
  User authenticatePatient(AuthRequest authRequest, HttpServletResponse response);
}
