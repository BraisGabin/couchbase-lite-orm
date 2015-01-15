package com.samples;

import com.petterfactory.couchbaseliteorm.Mapper;

import java.util.HashMap;
import java.util.Map;

public class Address$$Mapper implements Mapper<Address>  {

  @Override
  public Address toObject(Map<String, Object> properties) {
    final Address object = new Address();
    object.street = (String) properties.get("street");
    object.number = (String) properties.get("number");
    return object;
  }

  @Override
  public Map<String, Object> toProperties(Address object) {
    final Map<String, Object> properties = new HashMap<>();
    properties.put("street", object.street);
    properties.put("number", object.number);
    return properties;
  }
}