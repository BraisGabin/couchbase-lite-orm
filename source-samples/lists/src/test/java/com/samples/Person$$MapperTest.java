package com.samples;

import com.petterfactory.couchbaseliteorm.Mapper;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
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
    properties.put("emails", Arrays.asList("hola@sample.com"));

    final Person object = mapper.toObject(properties);

    ASSERT.that(object.emails).isEqualTo(Arrays.asList("hola@sample.com"));
  }

  @Test
  public void toProperties() {
    Person object = new Person();
    object.emails = Arrays.asList("hola@sample.com");

    final Map<String, Object> stringObjectMap = mapper.toProperties(object);

    ASSERT.that(stringObjectMap.get("emails")).isEqualTo(Arrays.asList("hola@sample.com"));
  }
}
