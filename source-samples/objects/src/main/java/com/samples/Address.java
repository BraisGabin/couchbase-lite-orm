package com.samples;

import com.braisgabin.couchbaseliteorm.Entity;
import com.braisgabin.couchbaseliteorm.Field;

/**
 * Created by brais on 13/1/15.
 */
@Entity
public class Address {

  @Field("street")
  String street;

  @Field("number")
  String number;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Address address = (Address) o;

    if (number != null ? !number.equals(address.number) : address.number != null) return false;
    if (street != null ? !street.equals(address.street) : address.street != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = street != null ? street.hashCode() : 0;
    result = 31 * result + (number != null ? number.hashCode() : 0);
    return result;
  }
}
