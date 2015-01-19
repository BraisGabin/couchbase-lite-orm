package com.samples;

import com.petterfactory.couchbaseliteorm.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Person$$Mapper implements Mapper<Person>  {

  @Override
  public Person toObject(Map<String, Object> properties) {
    final Person object = new Person();
    object.emails = (List<String>) properties.get("emails");
    if (object.emails == null && !properties.containsKey("emails")) {
      throw new IllegalStateException("The property \"emails\" is not setted.");
    }
    object.address = (List<Address>) properties.get("address");
    if (object.address == null && !properties.containsKey("address")) {
      throw new IllegalStateException("The property \"address\" is not setted.");
    }
    return object;
  }

  @Override
  public Map<String, Object> toProperties(Person object) {
    final Map<String, Object> properties = new HashMap<>();
    properties.put("type", "person");
    properties.put("emails", object.emails);
    properties.put("address", object.address);
    return properties;
  }
}
