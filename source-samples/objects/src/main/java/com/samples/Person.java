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
}
