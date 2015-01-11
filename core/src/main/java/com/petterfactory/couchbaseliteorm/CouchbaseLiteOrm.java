package com.petterfactory.couchbaseliteorm;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Document;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by brais on 9/1/15.
 */
public abstract class CouchbaseLiteOrm {

  private final Map<String, Mapper<?>> stringToMapper;
  private final Map<Class<?>, Mapper<?>> classToMapper; // Is it possible force '? == ?'?

  protected CouchbaseLiteOrm() {
    this.stringToMapper = new HashMap<>();
    this.classToMapper = new HashMap<>();
  }

  public static CouchbaseLiteOrm newInstance() {
    final CouchbaseLiteOrm instance;
    // FIXME It'll be great don't use reflection here.
    try {
      Class<?> a = Class.forName("com.petterfactory.couchbaseliteorm.CouchbaseLiteOrmInternal");
      instance = (CouchbaseLiteOrm) a.newInstance();
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
    return instance;
  }

  protected <T> void registerType(String typeName, Class<T> clazz, Mapper<T> type) {
    stringToMapper.put(typeName, type);
    classToMapper.put(clazz, type);
  }

  // TODO Document
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

  // TODO Document
  <T> Document toDocument(T object, Document document) throws CouchbaseLiteException {
    @SuppressWarnings("unchecked")
    final Mapper<T> wrapper = (Mapper<T>) classToMapper.get(object.getClass());
    if (wrapper == null) {
      throw new IllegalArgumentException("Unknown class " + object.getClass().getCanonicalName() + ". It's annotated?");
    }
    Map<String, Object> properties = wrapper.toProperties(object);
    document.putProperties(properties);
    return document;
  }
}
