package com.petterfactory.couchbaseliteorm;

import com.couchbase.lite.Document;
import com.samples.Person;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;

import static com.google.common.truth.Truth.ASSERT;
import static org.mockito.Mockito.when;

/**
 * Created by brais on 9/1/15.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Document.class)
public class CouchbaseLiteOrmInternalBaseTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void checkGet_correct() {
    Document document = PowerMockito.mock(Document.class); // FIXME remove PowerMockito
    final HashMap<String, Object> properties = new HashMap<>();
    properties.put("type", "person");
    properties.put("name", "Pepe");
    properties.put("age", 23);
    when(document.getProperties()).thenReturn(properties);

    CouchbaseLiteOrmInternalBase orm = new CouchbaseLiteOrmInternal();

    Person person = orm.get(document);

    ASSERT.that(person.getName()).isEqualTo("Pepe");
    ASSERT.that(person.getAge()).isEqualTo(23);
  }

  @Test
  public void checkGet_noType() {
    Document document = PowerMockito.mock(Document.class); // FIXME remove PowerMockito
    final HashMap<String, Object> properties = new HashMap<>();
    when(document.getProperties()).thenReturn(properties);
    when(document.getId()).thenReturn("foo");

    CouchbaseLiteOrmInternalBase orm = new CouchbaseLiteOrmInternal();

    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("The document foo doesn't have set the \"type\" property.");
    orm.get(document);
  }

  @Test
  public void checkGet_unknownType() {
    Document document = PowerMockito.mock(Document.class); // FIXME remove PowerMockito
    final HashMap<String, Object> properties = new HashMap<>();
    properties.put("type", "bar");
    when(document.getProperties()).thenReturn(properties);
    when(document.getId()).thenReturn("foo");

    CouchbaseLiteOrmInternalBase orm = new CouchbaseLiteOrmInternal();

    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Unknown type bar at document foo.");
    orm.get(document);
  }
}
