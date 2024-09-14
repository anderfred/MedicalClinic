package com.anderfred.medical.clinic.domain.clinic;

import com.anderfred.medical.clinic.domain.base.AbstractAuditingEntity;
import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "clinic")
public class Clinic extends AbstractAuditingEntity {
  public static final Long INITIAL_CLINIC_ID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name")
  private String name;

  @Column(name = "address", columnDefinition = "json")
  @Type(value = JsonType.class)
  private Address address;

  @Column(name = "phone")
  private String phone;

  @Column(name = "logo")
  private String logo;

  @Column(name = "schedule", columnDefinition = "json")
  @Type(value = JsonType.class)
  private List<WorkingDay> schedule;

  public Clinic setId(Long id) {
    this.id = id;
    return this;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Clinic setName(String name) {
    this.name = name;
    return this;
  }

  public Address getAddress() {
    return address;
  }

  public Clinic setAddress(Address address) {
    this.address = address;
    return this;
  }

  public String getPhone() {
    return phone;
  }

  public Clinic setPhone(String phone) {
    this.phone = phone;
    return this;
  }

  public String getLogo() {
    return logo;
  }

  public Clinic setLogo(String logo) {
    this.logo = logo;
    return this;
  }

  public List<WorkingDay> getSchedule() {
    return CollectionUtils.emptyIfNull(schedule).stream()
        .sorted(Comparator.comparingInt(c -> c.getDay().ordinal()))
        .collect(Collectors.toCollection(LinkedList::new));
  }

  public Clinic setSchedule(List<WorkingDay> schedule) {
    this.schedule = schedule;
    return this;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
        .append("id", getId())
        .append("name", getName())
        .append("address", getAddress())
        .append("phone", getPhone())
        .append("logo", getLogo())
        .append("schedule", getSchedule())
        .toString();
  }

  public void update(Clinic updated) {
    setLogo(updated.getLogo());
    setPhone(updated.getPhone());
    setName(updated.getName());
    setAddress(updated.getAddress());
  }
}
