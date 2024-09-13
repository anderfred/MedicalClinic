package com.anderfred.medical.clinic.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

  @Bean
  public FilterRegistrationBean<SystemContextFilter> mdcContextFilter() {
    FilterRegistrationBean<SystemContextFilter> registrationBean = new FilterRegistrationBean<>();

    registrationBean.setFilter(new SystemContextFilter());

    registrationBean.addUrlPatterns("/*");
    registrationBean.setOrder(1);

    return registrationBean;
  }
}
