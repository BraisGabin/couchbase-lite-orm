package com.samples;

import com.petterfactory.couchbaseliteorm.Example;
import com.petterfactory.couchbaseliteorm.ExampleField;

import java.util.List;

@Example("person")
public class Person {

  @ExampleField("emails")
  List<String> emails;

  public Person() {
  }

  public Person(List<String> emails) {
    this.emails = emails;
  }

  public List<String> getAge() {
    return emails;
  }
}
