package com.samples;

import com.braisgabin.couchbaseliteorm.Entity;
import com.braisgabin.couchbaseliteorm.Field;

@Entity("person")
public class Person {

  @Field("name")
  String name;

  @Field("age")
  int age;

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
