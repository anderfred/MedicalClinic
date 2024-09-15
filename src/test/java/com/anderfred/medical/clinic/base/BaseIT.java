package com.anderfred.medical.clinic.base;

import com.anderfred.medical.clinic.ClinicApplication;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.MySQLContainer;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
    classes = {
      ClinicApplication.class,
    })
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseIT {
  private static final MySQLContainer<?> MYSQL_CONTAINER =
      new MySQLContainer<>("mysql:8.0.33")
          .withDatabaseName("testdb")
          .withUsername("test")
          .withPassword("test");

  @DynamicPropertySource
  static void mysqlProperties(DynamicPropertyRegistry registry) {
    MYSQL_CONTAINER.start();
    registry.add("spring.datasource.url", MYSQL_CONTAINER::getJdbcUrl);
    registry.add("spring.datasource.username", MYSQL_CONTAINER::getUsername);
    registry.add("spring.datasource.password", MYSQL_CONTAINER::getPassword);
    registry.add("spring.datasource.driver-class-name", MYSQL_CONTAINER::getDriverClassName);
  }
}
