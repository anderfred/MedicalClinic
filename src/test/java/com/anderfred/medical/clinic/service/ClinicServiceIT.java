package com.anderfred.medical.clinic.service;

import static com.anderfred.medical.clinic.domain.clinic.Clinic.INITIAL_CLINIC_ID;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.assertj.core.api.Assertions.assertThat;

import com.anderfred.medical.clinic.base.BaseIT;
import com.anderfred.medical.clinic.domain.clinic.Address;
import com.anderfred.medical.clinic.domain.clinic.Clinic;
import com.anderfred.medical.clinic.domain.clinic.WorkingDay;
import com.anderfred.medical.clinic.repository.jpa.ClinicJpaRepository;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import com.anderfred.medical.clinic.security.WithCustomMockUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ClinicServiceIT extends BaseIT {
  @Autowired private ClinicJpaRepository repository;
  @Autowired private ClinicService service;

  private final String CLINIC_INITIAL_NAME = "Test clinic";
  private final String CLINIC_INITIAL_PHONE = "+1111111111";
  private final String CLINIC_INITIAL_LOGO = "http://test12313321.com/testlogo.png";

  @Test
  public void shouldVerifyInitialClinic() {
    Clinic clinic = repository.findById(1L).orElseThrow();
    assertThat(clinic.getName()).isEqualTo(CLINIC_INITIAL_NAME);
    assertThat(clinic.getPhone()).isEqualTo(CLINIC_INITIAL_PHONE);
    assertThat(clinic.getLogo()).isEqualTo(CLINIC_INITIAL_LOGO);

    Address address = clinic.getAddress();
    assertThat(address.getAddressLine1()).isNotBlank();
    assertThat(address.getAddressLine2()).isNotBlank();
    assertThat(address.getCity()).isNotBlank();
    assertThat(address.getState()).isNotBlank();
    assertThat(address.getZip()).isNotBlank();

    List<WorkingDay> schedule = clinic.getSchedule();
    assertThat(schedule.size()).isEqualTo(7);

    schedule.forEach(
        day -> {
          assertThat(day.isDayOff()).isFalse();
          assertThat(day.getStartTime().equals(LocalTime.of(9, 0))).isTrue();
          assertThat(day.getEndTime().equals(LocalTime.of(20, 0))).isTrue();
          assertThat(Arrays.asList(DayOfWeek.values()).contains(day.getDay())).isTrue();
        });
  }

  @Test
  public void shouldFindClinic() {
    // Should find initial one
    Clinic clinic = service.findClinicById(null);
    assertThat(clinic.getName()).isEqualTo(CLINIC_INITIAL_NAME);

    // Same one directly by id
    Clinic sameOne = service.findClinicById(INITIAL_CLINIC_ID);
    assertThat(sameOne.getName()).isEqualTo(CLINIC_INITIAL_NAME);
  }

  @Test
  @WithCustomMockUser
  public void shouldUpdateClinic() {
    Clinic clinic = service.findClinicById(INITIAL_CLINIC_ID);
    final String updatedName = randomAlphanumeric(10);
    final String updatedLogo = randomAlphanumeric(10);
    final String updatedPhone = randomAlphanumeric(10);

    final Address updatedAddress =
        new Address()
            .setAddressLine1(randomAlphanumeric(10))
            .setAddressLine2(randomAlphanumeric(10))
            .setCity(randomAlphanumeric(6))
            .setState(randomAlphanumeric(2).toUpperCase(Locale.ROOT))
            .setZip(randomAlphanumeric(6));

    List<WorkingDay> days = List.of(new WorkingDay().setDay(DayOfWeek.MONDAY).setDayOff(true));

    Clinic toUpdate =
        new Clinic()
            .setId(INITIAL_CLINIC_ID)
            .setAddress(updatedAddress)
            .setName(updatedName)
            .setLogo(updatedLogo)
            .setPhone(updatedPhone)
            .setSchedule(days);

    service.updateClinic(toUpdate);

    Clinic updated = repository.findById(INITIAL_CLINIC_ID).orElseThrow();

    assertThat(updated.getName()).isEqualTo(updatedName);
    assertThat(updated.getLogo()).isEqualTo(updatedLogo);
    assertThat(updated.getPhone()).isEqualTo(updatedPhone);
    assertThat(updated.getAddress()).isEqualTo(updatedAddress);
    assertThat(updated.getSchedule().size()).isEqualTo(7);
  }
}
