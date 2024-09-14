package com.anderfred.medical.clinic.domain.clinic;

import static java.util.Objects.isNull;

import java.io.Serializable;
import java.util.Objects;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Address implements Serializable {

  private String addressLine1;
  private String addressLine2;
  private String city;
  private String state;
  private String zip;

  public String getAddressLine1() {
    return addressLine1;
  }

  public Address setAddressLine1(String addressLine1) {
    this.addressLine1 = addressLine1;
    return this;
  }

  public static String cityCode(Address Address) {
    return isNull(Address) ? null : Address.getCity();
  }

  public String getAddressLine2() {
    return addressLine2;
  }

  public Address setAddressLine2(String addressLine2) {
    this.addressLine2 = addressLine2;
    return this;
  }

  public String getCity() {
    return city;
  }

  public Address setCity(String city) {
    this.city = city;
    return this;
  }

  public String getState() {
    return state;
  }

  public Address setState(String state) {
    this.state = state;
    return this;
  }

  public String getZip() {
    return zip;
  }

  public Address setZip(String zip) {
    this.zip = zip;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Address that = (Address) o;
    return Objects.equals(addressLine1, that.addressLine1)
        && Objects.equals(addressLine2, that.addressLine2)
        && Objects.equals(city, that.city)
        && Objects.equals(state, that.state)
        && Objects.equals(zip, that.zip);
  }

  @Override
  public int hashCode() {
    return Objects.hash(addressLine1, addressLine2, city, state, zip);
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
        .append("addressLine1", getAddressLine1())
        .append("addressLine2", getAddressLine2())
        .append("city", getCity())
        .append("state", getState())
        .append("zip", getZip())
        .toString();
  }
}
