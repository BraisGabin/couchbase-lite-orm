package com.samples;

import com.petterfactory.couchbaseliteorm.Entity;
import com.petterfactory.couchbaseliteorm.Field;

import java.util.List;

@Entity("person")
public class Person {

  @Field("emails")
  List<String> emails;

  public Person() {
  }

  public Person(List<String> emails) {
    this.emails = emails;
  }
}
