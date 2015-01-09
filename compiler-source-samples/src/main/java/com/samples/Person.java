package com.samples;

import com.petterfactory.couchbaseliteorm.Example;
import com.petterfactory.couchbaseliteorm.ExampleField;

@Example("person")
public class Person {

  @ExampleField("name")
  String name;

  @ExampleField("age")
  Integer age;

  public String getName() {
    return name;
  }

  public Integer getAge() {
    return age;
  }
}
