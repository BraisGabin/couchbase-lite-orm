package com.petterfactory.couchbaseliteorm;

import com.samples.Person;
import com.samples.Person$$Mapper;

/**
 * Created by brais on 7/1/15.
 */
class CouchbaseLiteOrmInternal extends CouchbaseLiteOrm {
  CouchbaseLiteOrmInternal() {
    registerType("person", Person.class, new Person$$Mapper());
  }
}
