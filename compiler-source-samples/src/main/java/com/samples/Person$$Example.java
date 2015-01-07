package com.samples;

import com.couchbase.lite.Document;

import java.util.Map;

public abstract class Person$$Example {
  public static Person get(Document document) {
    Map<String, Object> properties = document.getProperties();
    String name = (String) properties.get("name");
    Integer age = (Integer) properties.get("age");
    return new Person(name, age);
  }
}
