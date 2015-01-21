package com.petterfactory.couchbaseliteorm;

import com.samples.Address$$Mapper;
import com.samples.Person;
import com.samples.Person$$Mapper;

/**
 * Created by brais on 7/1/15.
 */
class CouchbaseLiteOrmInternal extends CouchbaseLiteOrm {
  CouchbaseLiteOrmInternal() {
    final Address$$Mapper addressMapper = new Address$$Mapper();
    final Person$$Mapper personMapper = new Person$$Mapper();
    personMapper.addressMapper = addressMapper;
    registerType("person", Person.class, personMapper);
  }
}
