package com.petterfactory.couchbaseliteorm;

import com.couchbase.lite.Document;
import com.samples.Person;

import org.junit.Test;
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
public class CouchbaseLiteOrmTest {

  @Test
  public void checkGet() {
    Document document = PowerMockito.mock(Document.class); // FIXME remove PowerMockito
    final HashMap<String, Object> properties = new HashMap<>();
    properties.put("type", "person");
    properties.put("name", "Pepe");
    properties.put("age", 23);
    when(document.getProperties()).thenReturn(properties);

    Person person = CouchbaseLiteOrm.toObject(document);

    ASSERT.that(person.getName()).isEqualTo("Pepe");

    ASSERT.that(person.getAge()).isEqualTo(23);
  }
}
