package com.samples;

import com.couchbase.lite.Document;

import java.util.Map;

public abstract class Person$$Example {
  public static Person get(Document document) {
    Person object = new Person();
    Map<String, Object> properties = document.getProperties();
    object.name = (String) properties.get("name");
    object.age = (Integer) properties.get("age");
    return object;
  }
}
