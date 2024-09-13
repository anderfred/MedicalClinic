package com.anderfred.medical.clinic.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.slf4j.LoggerFactory.getLogger;

import com.anderfred.medical.clinic.exceptions.BaseException;
import com.anderfred.medical.clinic.exceptions.ClinicExceptionCode;
import org.junit.jupiter.api.function.Executable;
import org.slf4j.Logger;

public final class AssertJUtil {
  private static final Logger log = getLogger(AssertJUtil.class);

  private AssertJUtil() {}

  public static void assertBaseException(String expCode, Executable r) {
    final BaseException baseException = assertThrows(BaseException.class, r);
    assertThat(baseException.getCode()).isEqualTo(expCode);
  }

  public static void assertBaseException(ClinicExceptionCode expCode, Executable r) {
    final BaseException baseException = assertThrows(BaseException.class, r);
    log.info("Exception message - {}", baseException.getMessage());
    assertThat(baseException.getCode()).isEqualTo(expCode.name());
  }
}
