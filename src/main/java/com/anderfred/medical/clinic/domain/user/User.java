package com.anderfred.medical.clinic.domain.user;

import com.anderfred.medical.clinic.JsonConstants;
import com.anderfred.medical.clinic.domain.base.AbstractAuditingEntity;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import java.time.Instant;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;

@Entity
@Table(name = "user")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "object_type")
@Access(AccessType.FIELD)
@Configurable
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = JsonConstants.PROPERTY_NAME)
@JsonSubTypes({
  @JsonSubTypes.Type(value = Doctor.class, name = Doctor.TYPE_DISCRIMINATOR),
  @JsonSubTypes.Type(value = Patient.class, name = Patient.TYPE_DISCRIMINATOR)
})
public abstract class User extends AbstractAuditingEntity {

  @Column(name = "id")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "first_name", nullable = false)
  @NotEmpty
  private String firstName;

  @Column(name = "last_name", nullable = false)
  @NotEmpty
  private String lastName;

  @Column(name = "email", nullable = false)
  @NotEmpty
  private String email;

  @Column(name = "password", nullable = false)
  private String password;

  @Column(name = "last_login_date", nullable = false)
  private Instant lastLoginDate = Instant.now();

  @Column(name = "state", nullable = false)
  @Enumerated(EnumType.STRING)
  private UserState state = UserState.ACTIVE;

  public User setId(Long id) {
    this.id = id;
    return this;
  }

  public Long getId() {
    return id;
  }

  public String getFirstName() {
    return firstName;
  }

  public User setFirstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  public String getLastName() {
    return lastName;
  }

  public User setLastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  public String getEmail() {
    return email;
  }

  public User setEmail(String email) {
    this.email = email;
    return this;
  }

  public Instant getLastLoginDate() {
    return lastLoginDate;
  }

  public User setLastLoginDate(Instant lastLoginDate) {
    this.lastLoginDate = lastLoginDate;
    return this;
  }

  public UserState getState() {
    return state;
  }

  public User setState(UserState state) {
    this.state = state;
    return this;
  }

  public String getPassword() {
    return password;
  }

  public User setPassword(String password) {
    this.password = password;
    return this;
  }

  public void update(User updated) {
    setEmail(updated.getEmail());
    setFirstName(updated.getFirstName());
    setLastName(updated.getLastName());
  }

  public void delete() {
    setState(UserState.DELETED);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
        .append("id", getId())
        .append("firstName", getFirstName())
        .append("lastName", getLastName())
        .append("email", getEmail())
        .append("lastLoginDate", getLastLoginDate())
        .append("state", getState())
        .append("createdBy", getCreatedBy())
        .append("lastModifiedDate", getLastModifiedDate())
        .append("lastModifiedBy", getLastModifiedBy())
        .append("createdDate", getCreatedDate())
        .toString();
  }

  public User removeSensitiveData() {
    return setPassword(null);
  }
}
