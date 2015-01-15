package com.samples;

import com.petterfactory.couchbaseliteorm.Example;
import com.petterfactory.couchbaseliteorm.ExampleField;

@Example("person")
public class Person {

  @ExampleField("address")
  Address address;

  public Address getAddress() {
    return address;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Person person = (Person) o;

    if (address != null ? !address.equals(person.address) : person.address != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    return address != null ? address.hashCode() : 0;
  }
}
