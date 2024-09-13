package com.anderfred.medical.clinic.config;

import com.anderfred.medical.clinic.util.MDCUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

public class SystemContextFilter extends GenericFilterBean {

  public static String MDC_USER = "mdc_user";
  public static String MDC_CID = "mdc_cid";
  public static String MDC_SCID = "mdc_scid";

  protected final Logger log = LoggerFactory.getLogger(SystemContextFilter.class);

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    try {
      String correlationId =
          getCorrelationId((HttpServletRequest) request, ((HttpServletResponse) response));
      String user = getUser((HttpServletRequest) request, ((HttpServletResponse) response));
      String systemTrx = MDCUtil.generate();

      MDC.put(MDC_CID, correlationId);
      MDC.put(MDC_SCID, systemTrx);
      MDC.put(MDC_USER, user);

      chain.doFilter(request, response);
    } finally {
      MDCUtil.cleanup();
    }
  }

  private String getCorrelationId(HttpServletRequest request, HttpServletResponse response) {
    String correlationId = request.getHeader(MDC_CID);

    String requestUri = request.getRequestURI();
    if (StringUtils.isBlank(correlationId)) {
      correlationId = MDCUtil.getCID();
      log.warn(
          "Request without cid : {}/{} --> {} {}, contentLength = {} ",
          request.getRemoteAddr(),
          request.getServerName(),
          request.getMethod(),
          requestUri,
          request.getContentLength());
      log.warn("New cid generated : {} ", correlationId);
    }
    response.setHeader(MDC_CID, correlationId);

    return correlationId;
  }

  private String getUser(HttpServletRequest request, HttpServletResponse response) {
    String user = request.getHeader(MDC_USER);
    if (StringUtils.isBlank(user)) {
      user = SecurityContextHolder.getContext().getAuthentication().getName();
    }
    response.setHeader(MDC_USER, user);
    return user;
  }
}
