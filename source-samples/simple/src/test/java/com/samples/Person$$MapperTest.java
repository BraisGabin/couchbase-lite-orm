package com.samples;

import com.braisgabin.couchbaseliteorm.Mapper;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.truth.Truth.ASSERT;

public class Person$$MapperTest {

  private Mapper<Person> mapper;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Before
  public void setMapper() {
    this.mapper = new Person$$Mapper();
  }

  @Test
  public void toObject() {
    Map<String, Object> properties = new HashMap<>();
    properties.put("name", "Pepe");
    properties.put("age", 23);

    final Person object = mapper.toObject(properties);

    ASSERT.that(object.name).isEqualTo("Pepe");
    ASSERT.that(object.age).isEqualTo(23);
  }

  @Test
  public void toObject_null() {
    Map<String, Object> properties = new HashMap<>();
    properties.put("name", null);
    properties.put("age", 23);

    final Person object = mapper.toObject(properties);

    ASSERT.that(object.name).isNull();
    ASSERT.that(object.age).isEqualTo(23);
  }

  @Test
  public void toObject_noAllProperties() {
    Map<String, Object> properties = new HashMap<>();
    properties.put("age", 23);

    thrown.expect(IllegalStateException.class);
    thrown.expectMessage("The property \"name\" is not set.");
    mapper.toObject(properties);
  }

  @Test
  public void toObject_nullInPrimitiveTypes() {
    Map<String, Object> properties = new HashMap<>();
    properties.put("name", "Pepe");
    properties.put("age", null);

    thrown.expect(NullPointerException.class);
    thrown.expectMessage("The property \"age\" has the value null. It can't be set to a int.");
    mapper.toObject(properties);
  }

  @Test
  public void toObject_incorrectTypes() {
    Map<String, Object> properties = new HashMap<>();
    properties.put("name", "Pepe");
    properties.put("age", true);

    thrown.expect(ClassCastException.class);
    thrown.expectMessage("java.lang.Boolean cannot be cast to java.lang.Integer");
    mapper.toObject(properties);
  }

  @Test
  public void toProperties() {
    Person object = new Person();
    object.name = "Pepe";
    object.age = 23;

    final Map<String, Object> stringObjectMap = mapper.toProperties(object);

    ASSERT.that(stringObjectMap.get("type")).isEqualTo("person");
    ASSERT.that(stringObjectMap.get("name")).isEqualTo("Pepe");
    ASSERT.that(stringObjectMap.get("age")).isEqualTo(23);
  }

  @Test
  public void toProperties_null() {
    Person object = new Person();
    object.name = null;
    object.age = 23;

    final Map<String, Object> stringObjectMap = mapper.toProperties(object);

    ASSERT.that(stringObjectMap.get("type")).isEqualTo("person");
    ASSERT.that(stringObjectMap.containsKey("name")).isTrue();
    ASSERT.that(stringObjectMap.get("name")).isNull();
    ASSERT.that(stringObjectMap.get("age")).isEqualTo(23);
  }
}
