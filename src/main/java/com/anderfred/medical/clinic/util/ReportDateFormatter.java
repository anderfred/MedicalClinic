package com.anderfred.medical.clinic.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReportDateFormatter {
  private static final DateTimeFormatter FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

  public static String format(LocalDateTime dateTime) {
    return dateTime != null ? dateTime.format(FORMATTER) : "-";
  }
}
