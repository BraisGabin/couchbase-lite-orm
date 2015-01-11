package com.petterfactory.couchbaseliteorm;

import com.couchbase.lite.Document;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by brais on 9/1/15.
 */
abstract class CouchbaseLiteOrmInternalBase {
  private final Map<String, Wrapper<?>> typeWrappers;

  protected CouchbaseLiteOrmInternalBase() {
    this.typeWrappers = new HashMap<>();
  }

  protected void registerType(String typeName, Wrapper<?> type) {
    typeWrappers.put(typeName, type);
  }

  <T> T get(Document document) {
    final Map<String, Object> properties = document.getProperties();
    final String sDocumentType = (String) properties.get("type");
    if (sDocumentType == null) {
      throw new IllegalArgumentException("The document " + document.getId() + " doesn't have set the \"type\" property.");
    }
    @SuppressWarnings("unchecked")
    final Wrapper<T> wrapper = (Wrapper<T>) typeWrappers.get(sDocumentType);
    if (wrapper == null) {
      throw new IllegalArgumentException("Unknown type " + sDocumentType + " at document " + document.getId() + ".");
    }
    return wrapper.get(properties);
  }

  interface Wrapper<T> {
    T get(Map<String, Object> properties);
  }
}
