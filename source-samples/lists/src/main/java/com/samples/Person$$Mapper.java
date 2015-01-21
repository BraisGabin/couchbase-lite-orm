package com.samples;

import com.petterfactory.couchbaseliteorm.Mapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Person$$Mapper implements Mapper<Person> {
  public Address$$Mapper addressMapper;

  @Override
  public Person toObject(Map<String, Object> properties) {
    final Person object = new Person();
    object.emails = (List<String>) properties.get("emails");
    if (object.emails == null && !properties.containsKey("emails")) {
      throw new IllegalStateException("The property \"emails\" is not set.");
    }
    if (properties.get("address") != null) {
      final Collection<Map<String, Object>> aux = (Collection<Map<String, Object>>) properties.get("address");
      final List<Address> list = new ArrayList<>(aux.size());
      for (Map<String, Object> auxProperties : aux) {
        list.add(addressMapper.toObject(auxProperties));
      }
      object.address = list;
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
    properties.put("emails", object.emails);
    final List<Map<String, Object>> propertiesAddress;
    if (object.address == null) {
      propertiesAddress = null;
    } else {
      propertiesAddress = new ArrayList<>(object.address.size());
      for (Address address : object.address) {
        propertiesAddress.add(addressMapper.toProperties(address));
      }
    }
    properties.put("address", propertiesAddress);
    return properties;
  }
}
