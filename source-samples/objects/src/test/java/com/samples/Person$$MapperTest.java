package com.samples;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.truth.Truth.ASSERT;

public class Person$$MapperTest {

  private Person$$Mapper mapper;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Before
  public void setMapper() {
    this.mapper = new Person$$Mapper();
    this.mapper.addressMapper = new Address$$Mapper();
  }

  @Test
  public void toObject() {
    Map<String, Object> properties = new HashMap<>();
    Map<String, Object> addressProperties = new HashMap<>();
    addressProperties.put("street", "calle");
    addressProperties.put("number", "7");
    properties.put("address", addressProperties);

    final Person object = mapper.toObject(properties);

    final Person person = new Person();
    final Address address = new Address();
    address.street = "calle";
    address.number = "7";
    person.address = address;
    ASSERT.that(object).isEqualTo(person);
  }

  @Test
  public void toObject_null() {
    Map<String, Object> properties = new HashMap<>();
    properties.put("address", null);

    final Person object = mapper.toObject(properties);

    final Person person = new Person();
    ASSERT.that(person).isEqualTo(object);
  }

  @Test
  public void toProperties() {
    final Person object = new Person();
    final Address address = new Address();
    address.street = "calle";
    address.number = "7";
    object.address = address;

    final Map<String, Object> stringObjectMap = mapper.toProperties(object);

    Map<String, Object> properties = new HashMap<>();
    Map<String, Object> addressProperties = new HashMap<>();
    addressProperties.put("street", "calle");
    addressProperties.put("number", "7");
    properties.put("address", addressProperties);
    properties.put("type", "person");

    ASSERT.that(properties).isEqualTo(stringObjectMap);
  }

  @Test
  public void toProperties_null() {
    final Person object = new Person();

    final Map<String, Object> stringObjectMap = mapper.toProperties(object);

    Map<String, Object> properties = new HashMap<>();
    properties.put("address", null);
    properties.put("type", "person");

    ASSERT.that(properties).isEqualTo(stringObjectMap);
  }
}
