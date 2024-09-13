package com.anderfred.medical.clinic.web;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@TestPropertySource(value = "/config/application.yml")
@EnableConfigurationProperties
@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
public abstract class BaseResourceTest {}
