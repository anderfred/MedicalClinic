package com.anderfred.medical.clinic.util;

import static com.anderfred.medical.clinic.config.SystemContextFilter.*;
import static java.util.Objects.isNull;

import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;

public class MDCUtil {
  private static final String systemUserKey = "system";

  public static void cleanup() {
    MDC.clear();
  }

  public static void enrichMDCSystemContext() {
    MDC.put(MDC_USER, systemUserKey);
    initCID();
  }

  private static void initCID() {
    initCID(Optional.empty());
  }

  private static void initCID(Optional<String> externalCID) {
    if (isNull(MDC.get(MDC_CID))) {
      String cid = externalCID.orElseGet(MDCUtil::generate);
      MDC.put(MDC_CID, cid);
    }
    if (isNull(MDC.get(MDC_SCID))) {
      String scid = generate();
      MDC.put(MDC_SCID, scid);
    }
  }

  public static String generate() {
    String rid =
        new String(Base64.getEncoder().encode(DigestUtils.sha256(UUID.randomUUID().toString())));
    rid = StringUtils.replaceChars(rid, "+/=", "");
    return StringUtils.right(rid, 24);
  }

  public static String getCID() {
    return Optional.ofNullable(MDC.get(MDC_CID)).map(String::valueOf).orElse(generate());
  }

  public static String getSCID() {
    return Optional.ofNullable(MDC.get(MDC_SCID)).map(String::valueOf).orElse(generate());
  }

  public static String getUser() {
    return Optional.ofNullable(MDC.get(MDC_USER)).map(String::valueOf).orElse(systemUserKey);
  }

  public static void init(Authentication authResult) {
    MDC.put(MDC_USER, authResult.getPrincipal().toString());
    if (!initialized()) {
      initCID();
    }
  }


  public static boolean initialized() {
    return !StringUtils.isBlank(MDC.get(MDC_USER))
        && !StringUtils.isBlank(MDC.get(MDC_CID))
        && !StringUtils.isBlank(MDC.get(MDC_SCID));
  }
}
