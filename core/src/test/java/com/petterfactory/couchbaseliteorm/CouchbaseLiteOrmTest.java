package com.petterfactory.couchbaseliteorm;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Document;
import com.samples.Person;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.truth.Truth.ASSERT;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by brais on 9/1/15.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(Document.class)
public class CouchbaseLiteOrmTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void checkFactory() {
    final CouchbaseLiteOrm orm = CouchbaseLiteOrm.newInstance();

    ASSERT.that(orm).isInstanceOf(CouchbaseLiteOrmInternal.class);
  }

  @Test
  public void checkToObject_correct() {
    Document document = PowerMockito.mock(Document.class); // FIXME remove PowerMockito
    final HashMap<String, Object> properties = new HashMap<>();
    properties.put("type", "person");
    properties.put("name", "Pepe");
    properties.put("age", 23);
    when(document.getProperties()).thenReturn(properties);

    CouchbaseLiteOrm orm = new CouchbaseLiteOrmInternal();

    Person person = orm.toObject(document);

    ASSERT.that(person.getName()).isEqualTo("Pepe");
    ASSERT.that(person.getAge()).isEqualTo(23);
  }

  @Test
  public void checkToObject_noType() {
    Document document = PowerMockito.mock(Document.class); // FIXME remove PowerMockito
    final HashMap<String, Object> properties = new HashMap<>();
    when(document.getProperties()).thenReturn(properties);
    when(document.getId()).thenReturn("foo");

    CouchbaseLiteOrm orm = new CouchbaseLiteOrmInternal();

    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("The document foo doesn't have set the \"type\" property.");
    orm.toObject(document);
  }

  @Test
  public void checkToObject_unknownType() {
    Document document = PowerMockito.mock(Document.class); // FIXME remove PowerMockito
    final HashMap<String, Object> properties = new HashMap<>();
    properties.put("type", "bar");
    when(document.getProperties()).thenReturn(properties);
    when(document.getId()).thenReturn("foo");

    CouchbaseLiteOrm orm = new CouchbaseLiteOrmInternal();

    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Unknown type bar at document foo.");
    orm.toObject(document);
  }

  @Test
  @SuppressWarnings("unchecked")
  public void checkToDocument_correct() throws CouchbaseLiteException {
    Document document = PowerMockito.mock(Document.class); // FIXME remove PowerMockito
    Person person = new Person("Pepe", 23);

    CouchbaseLiteOrm orm = new CouchbaseLiteOrmInternal();

    Document document1 = orm.toDocument(person, document);

    final Map<String, Object> properties = new HashMap<>();
    properties.put("type", "person");
    properties.put("name", "Pepe");
    properties.put("age", 23);

    ArgumentCaptor<Map> argumentCaptor = ArgumentCaptor.forClass(Map.class);
    verify(document).putProperties(argumentCaptor.capture());
    ASSERT.that(argumentCaptor.getValue()).isEqualTo(properties);
    ASSERT.that(document1).isSameAs(document);
  }

  @Test
  @SuppressWarnings("unchecked")
  public void checkToDocument_unknownClass() throws CouchbaseLiteException {
    Document document = PowerMockito.mock(Document.class); // FIXME remove PowerMockito

    CouchbaseLiteOrm orm = new CouchbaseLiteOrmInternal();

    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Unknown class java.lang.Object. It's annotated?");
    orm.toDocument(new Object(), document);
  }
}
