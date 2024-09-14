package com.anderfred.medical.clinic.job;

import com.anderfred.medical.clinic.repository.jpa.AppointmentJpaRepository;
import com.anderfred.medical.clinic.service.AppointmentService;
import org.apache.commons.lang3.time.StopWatch;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CloseAppointmentJob implements Job {
  private static final Logger log = LoggerFactory.getLogger(CloseAppointmentJob.class);

  private AppointmentService appointmentService;

  public CloseAppointmentJob(AppointmentService appointmentService) {
    this.appointmentService = appointmentService;
  }

  @Override
  @Transactional
  public void execute(JobExecutionContext jobExecutionContext) {
    StopWatch stopWatch = StopWatch.createStarted();
    log.debug("START | Close appointment job");
    appointmentService.closeAppointments();
    log.debug("STOP | Close appointment job time:[{}]ms", stopWatch.getTime());
  }
}
