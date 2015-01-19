package com.samples;

import com.petterfactory.couchbaseliteorm.Mapper;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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

  @Test @Ignore
  public void toObject() {
    final Map<String, Object> ad = new HashMap<>();
    ad.put("street", "calle");
    ad.put("number", "17");
    final List<String> emails =  Arrays.asList("hola@sample.com");
    final List<Map<String, Object>> address =  Arrays.asList(ad);

    final Map<String, Object> properties = new HashMap<>();
    properties.put("emails", emails);
    properties.put("address", address);

    final Person object = mapper.toObject(properties);

    final Person other = new Person();
    other.emails = emails;
    other.address = Arrays.asList(new Address("calle", "17"));

    ASSERT.that(object).isEqualTo(other);
  }

  @Test
  public void toProperties() {
    Person object = new Person();
    object.emails = Arrays.asList("hola@sample.com");

    final Map<String, Object> stringObjectMap = mapper.toProperties(object);

    ASSERT.that(stringObjectMap.get("emails")).isEqualTo(Arrays.asList("hola@sample.com"));
  }
}
