package com.samples;

import com.petterfactory.couchbaseliteorm.Mapper;

import java.util.HashMap;
import java.util.Map;

public class Person$$Mapper implements Mapper<Person> {

  @Override
  public Person toObject(Map<String, Object> properties) {
    final Person object = new Person();
    object.name = (String) properties.get("name");
    if (object.name == null && !properties.containsKey("name")) {
      throw new IllegalStateException("The property \"name\" is not setted.");
    }
    try {
      object.age = (int) properties.get("age");
    } catch (NullPointerException e) {
      if (!properties.containsKey("age")) {
        throw new IllegalStateException("The property \"age\" is not setted.");
      }
      throw new NullPointerException("The property \"age\" has the value null. It can't be set to a int.");
    }
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
