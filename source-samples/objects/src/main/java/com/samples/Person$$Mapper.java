package com.samples;

import com.braisgabin.couchbaseliteorm.Mapper;

import java.util.HashMap;
import java.util.Map;

public class Person$$Mapper implements Mapper<Person> {
  public Address$$Mapper addressMapper;

  @Override
  public Person toObject(Map<String, Object> properties) {
    final Person object = new Person();
    if (properties.get("address") != null) {
      object.address = addressMapper.toObject((Map<String, Object>) properties.get("address"));
    } else if (!properties.containsKey("address")) {
      throw new IllegalStateException("The property \"address\" is not set.");
    } else {
      object.address = null;
    }
    return object;
  }

  @Override
  public Map<String, Object> toProperties(Person object) {
    final Map<String, Object> properties = new HashMap<>();
    properties.put("type", "person");
    properties.put("address", object.address == null ? null : addressMapper.toProperties(object.address));
    return properties;
  }
}
