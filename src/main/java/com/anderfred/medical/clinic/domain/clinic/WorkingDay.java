package com.anderfred.medical.clinic.domain.clinic;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class WorkingDay implements Serializable {
  private DayOfWeek day;

  @JsonFormat(pattern = "H:mm")
  private LocalTime startTime;

  @JsonFormat(pattern = "H:mm")
  private LocalTime endTime;

  private boolean dayOff;

  public DayOfWeek getDay() {
    return day;
  }

  public WorkingDay setDay(DayOfWeek day) {
    this.day = day;
    return this;
  }

  public LocalTime getStartTime() {
    return startTime;
  }

  public WorkingDay setStartTime(LocalTime startTime) {
    this.startTime = startTime;
    return this;
  }

  public LocalTime getEndTime() {
    return endTime;
  }

  public WorkingDay setEndTime(LocalTime endTime) {
    this.endTime = endTime;
    return this;
  }

  public boolean isDayOff() {
    return dayOff;
  }

  public WorkingDay setDayOff(boolean dayOff) {
    this.dayOff = dayOff;
    return this;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
        .append("day", getDay())
        .append("startTime", getStartTime())
        .append("endTime", getEndTime())
        .append("dayOff", isDayOff())
        .toString();
  }
}
