package com.petterfactory.couchbaseliteorm;

import com.samples.Person$$Mapper;

/**
 * Created by brais on 7/1/15.
 */
class CouchbaseLiteOrmInternal extends CouchbaseLiteOrmInternalBase {
  CouchbaseLiteOrmInternal() {
    super();
    registerType("person", new Person$$Mapper());
  }
}
