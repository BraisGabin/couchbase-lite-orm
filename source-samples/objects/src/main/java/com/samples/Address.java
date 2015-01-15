package com.samples;

import com.petterfactory.couchbaseliteorm.Example;
import com.petterfactory.couchbaseliteorm.ExampleField;

/**
 * Created by brais on 13/1/15.
 */
@Example
public class Address {

  @ExampleField("street")
  String street;

  @ExampleField("number")
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
