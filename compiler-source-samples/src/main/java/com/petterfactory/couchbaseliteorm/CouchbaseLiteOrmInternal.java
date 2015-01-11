package com.petterfactory.couchbaseliteorm;

import com.samples.Person;
import com.samples.Person$$Mapper;

import java.util.Map;

/**
 * Created by brais on 7/1/15.
 */
class CouchbaseLiteOrmInternal extends CouchbaseLiteOrmInternalBase {
  CouchbaseLiteOrmInternal() {
    super();
    registerType("person", new Person$$Wrapper());
  }

  private static class Person$$Wrapper implements Wrapper<Person> {
    @Override
    public Person get(Map<String, Object> properties) {
      return Person$$Mapper.get(properties);
    }
  }
}
