package com.petterfactory.couchbaseliteorm;

import com.couchbase.lite.Document;

/**
 * Created by brais on 9/1/15.
 */
public class CouchbaseLiteOrm {
  private final static CouchbaseLiteOrmInternalBase internal;

  static {
    // FIXME It'll be great don't use reflection here.
    try {
      Class<?> a = Class.forName("com.petterfactory.couchbaseliteorm.CouchbaseLiteOrmInternal");
      internal = (CouchbaseLiteOrmInternalBase) a.newInstance();
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  // TODO Document
  public static <T> T get(Document document) {
    return internal.get(document);
  }
}
