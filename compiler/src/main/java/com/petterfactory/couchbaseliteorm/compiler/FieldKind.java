package com.petterfactory.couchbaseliteorm.compiler;

import java.util.Arrays;
import java.util.List;

public enum FieldKind {
  primitive,
  simpleObject,
  object,
  list;

  private final static List<String> simpleObjects = Arrays.asList(
      Boolean.class.getTypeName(),
      Byte.class.getTypeName(),
      Short.class.getTypeName(),
      Integer.class.getTypeName(),
      Long.class.getTypeName(),
      Float.class.getTypeName(),
      Double.class.getTypeName(),
      String.class.getTypeName()
  );

  public static boolean isSimpleObject(String fullQualifiedName) {
    return simpleObjects.contains(fullQualifiedName);
  }
}
