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
    registerType("person", Person.class);
  }

  @Override
  protected <T> T get(Map<String, Object> properties, Class<T> documentType) {
    final T object;
    if (documentType.equals(Person.class)) {
      object = (T) getPerson(properties);
    } else {
      throw new IllegalStateException("If you are getting this error please, report it.");
    }
    return object;
  }

  private static Person getPerson(Map<String, Object> properties) {
    return Person$$Mapper.get(properties);
  }
}
