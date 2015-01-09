package com.petterfactory.couchbaseliteorm;

import com.couchbase.lite.Document;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by brais on 9/1/15.
 */
abstract class CouchbaseLiteOrmInternalBase {
  private final Map<String, Class<?>> typesMapper;

  protected CouchbaseLiteOrmInternalBase() {
    this.typesMapper = new HashMap<>();
  }

  protected void registerType(String typeName, Class<?> type) {
    typesMapper.put(typeName, type);
  }

  <T> T get(Document document) {
    final Map<String, Object> properties = document.getProperties();
    final String sDocumentType = (String) properties.get("type");
    if (sDocumentType == null) {
      throw new IllegalArgumentException("The document " + document.getId() + " doesn't have set the \"type\" property.");
    }
    @SuppressWarnings("unchecked")
    final Class<T> documentType = (Class<T>) typesMapper.get(sDocumentType);
    if (documentType == null) {
      throw new IllegalArgumentException("Unknown type " + sDocumentType + " at document " + document.getId() + ".");
    }
    return get(properties, documentType);
  }

  protected abstract <T> T get(Map<String, Object> properties, Class<T> documentType);
}
