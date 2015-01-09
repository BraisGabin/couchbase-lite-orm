package com.samples;

import java.util.Map;

public abstract class Person$$Example {
  public static Person get(Map<String, Object> properties) {
    final Person object = new Person();
    object.name = (String) properties.get("name");
    object.age = (Integer) properties.get("age");
    return object;
  }
}
