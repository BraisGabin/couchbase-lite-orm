package com.samples;

import com.petterfactory.couchbaseliteorm.Mapper;

import java.util.HashMap;
import java.util.Map;

public class Person$$Mapper implements Mapper<Person>  {
  public Address$$Mapper addressMapper;

  @Override
  public Person toObject(Map<String, Object> properties) {
    final Person object = new Person();
    object.address = addressMapper.toObject((Map<String, Object>) properties.get("address"));
    return object;
  }

  @Override
  public Map<String, Object> toProperties(Person object) {
    final Map<String, Object> properties = new HashMap<>();
    properties.put("type", "person");
    properties.put("address", addressMapper.toProperties(object.address));
    return properties;
  }
}
