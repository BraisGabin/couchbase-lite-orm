package com.petterfactory.couchbaseliteorm;

import com.couchbase.lite.Document;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by brais on 9/1/15.
 */
abstract class CouchbaseLiteOrmInternalBase {
  private final Map<String, Mapper<?>> stringToMapper;

  protected CouchbaseLiteOrmInternalBase() {
    this.stringToMapper = new HashMap<>();
  }

  protected <T> void registerType(String typeName, Mapper<T> type) {
    stringToMapper.put(typeName, type);
  }

  <T> T toObject(Document document) {
    final Map<String, Object> properties = document.getProperties();
    final String sDocumentType = (String) properties.get("type");
    if (sDocumentType == null) {
      throw new IllegalArgumentException("The document " + document.getId() + " doesn't have set the \"type\" property.");
    }
    @SuppressWarnings("unchecked")
    final Mapper<T> wrapper = (Mapper<T>) stringToMapper.get(sDocumentType);
    if (wrapper == null) {
      throw new IllegalArgumentException("Unknown type " + sDocumentType + " at document " + document.getId() + ".");
    }
    return wrapper.toObject(properties);
  }
}
