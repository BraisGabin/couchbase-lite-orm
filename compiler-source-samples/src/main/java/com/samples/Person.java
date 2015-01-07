package com.samples;

import com.petterfactory.couchbaseliteorm.Example;

@Example
public class Person {
  private final String name;
  private final Integer age;

  public Person(String name, Integer age) {
    this.name = name;
    this.age = age;
  }

  public String getName() {
    return name;
  }

  public Integer getAge() {
    return age;
  }
}
