package com.anderfred.medical.clinic.job;

import org.apache.commons.lang3.time.StopWatch;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

// TODO
// @Component
public class CloseAppointmentJob implements Job {
  private final Logger log = LoggerFactory.getLogger(CloseAppointmentJob.class);

  @Override
  @Transactional
  public void execute(JobExecutionContext jobExecutionContext) {
    StopWatch stopWatch = StopWatch.createStarted();
    log.debug("START | Close appointment job");
    log.debug("STOP | Close appointment job time:[{}]ms", stopWatch.getTime());
  }
}
