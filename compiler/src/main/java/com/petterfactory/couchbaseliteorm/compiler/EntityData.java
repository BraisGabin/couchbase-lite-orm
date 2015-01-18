package com.petterfactory.couchbaseliteorm.compiler;

public interface EntityData {
  String getName();

  String getFullQualifiedName();

  String getPackage();

  String getVariable();
}
