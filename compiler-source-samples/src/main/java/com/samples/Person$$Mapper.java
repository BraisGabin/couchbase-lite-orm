package com.samples;

import com.petterfactory.couchbaseliteorm.Mapper;

import java.util.Map;

public class Person$$Mapper implements Mapper<Person>  {

  @Override
  public Person toObject(Map<String, Object> properties) {
    final Person object = new Person();
    object.name = (String) properties.get("name");
    object.age = (Integer) properties.get("age");
    return object;
  }
}
