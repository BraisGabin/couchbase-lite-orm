package com.samples;

import com.petterfactory.couchbaseliteorm.Mapper;

import java.util.HashMap;
import java.util.Map;

public class Person$$Mapper implements Mapper<Person> {

  @Override
  public Person toObject(Map<String, Object> properties) {
    final Person object = new Person();
    object.name = (String) properties.get("name");
    object.age = (int) properties.get("age");
    return object;
  }

  @Override
  public Map<String, Object> toProperties(Person object) {
    final Map<String, Object> properties = new HashMap<>();
    properties.put("type", "person");
    properties.put("name", object.name);
    properties.put("age", object.age);
    return properties;
  }
}
