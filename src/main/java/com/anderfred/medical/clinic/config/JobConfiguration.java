package com.anderfred.medical.clinic.config;

import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

@Configuration
@ConditionalOnProperty(name = "org.quartz.enabled", matchIfMissing = true)
public class JobConfiguration {
  @Value("${org.quartz.scheduler.start-delay:1}")
  private long configDelay;

  private final long startDelay = 1000 * 60 * configDelay;

  /*@Bean
  public JobDetailFactoryBean closeAppointmentJobDetails() {
    return createJobDetail(CloseAppointmentJob.class);
  }*/

  /*//TODO
  @Bean(name = "closeAppointmentJobDetailsJobTrigger")
  public CronTriggerFactoryBean closeAppointmentJobDetailsJobTrigger(
      @Qualifier("closeAppointmentJobDetails") JobDetail jobDetail,
      @Value("${org.quartz.scheduler.close-appointment}") String cronExpression) {
    return createCronTrigger(jobDetail, cronExpression);
  }*/

  public CronTriggerFactoryBean createCronTrigger(JobDetail jobDetail, String cronExpression) {
    CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
    factoryBean.setJobDetail(jobDetail);
    factoryBean.setCronExpression(cronExpression);
    factoryBean.setStartDelay(startDelay);
    factoryBean.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
    return factoryBean;
  }

  public JobDetailFactoryBean createJobDetail(Class jobClass) {
    JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
    factoryBean.setJobClass(jobClass);
    factoryBean.setDurability(true);
    return factoryBean;
  }
}
