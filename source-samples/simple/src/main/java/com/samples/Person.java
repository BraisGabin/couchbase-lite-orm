package com.samples;

import com.petterfactory.couchbaseliteorm.Entity;
import com.petterfactory.couchbaseliteorm.Field;

@Entity("person")
public class Person {

  @Field("name")
  String name;

  @Field("age")
  Integer age;

  public Person() {
  }

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
