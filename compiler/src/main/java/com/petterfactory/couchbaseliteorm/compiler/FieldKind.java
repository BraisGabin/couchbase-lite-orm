package com.petterfactory.couchbaseliteorm.compiler;

import java.util.Arrays;
import java.util.List;

public enum FieldKind {
  primitive,
  simpleObject,
  object,
  list;

  private final static List<String> simpleObjects = Arrays.asList(
      Boolean.class.getCanonicalName(),
      Byte.class.getCanonicalName(),
      Short.class.getCanonicalName(),
      Integer.class.getCanonicalName(),
      Long.class.getCanonicalName(),
      Float.class.getCanonicalName(),
      Double.class.getCanonicalName(),
      String.class.getCanonicalName()
  );

  public static boolean isSimpleObject(String fullQualifiedName) {
    return simpleObjects.contains(fullQualifiedName);
  }
}
