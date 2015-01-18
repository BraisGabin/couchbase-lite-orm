package com.petterfactory.couchbaseliteorm.compiler;

import java.util.Locale;

import javax.lang.model.element.TypeElement;


public class MapperModel implements EntityData {
  private final TypeElement element;

  public MapperModel(TypeElement element) {
    this.element = element;
  }

  @Override
  public String getName() {
    return element.getSimpleName().toString() + "$$Mapper";
  }

  @Override
  public String getFullQualifiedName() {
    return element.getQualifiedName().toString() + "$$Mapper";
  }

  @Override
  public String getPackage() {
    final String simpleName = element.getSimpleName().toString();
    final String qualifiedName = element.getQualifiedName().toString();
    return qualifiedName.substring(0, qualifiedName.length() - simpleName.length() - 1);
  }

  @Override
  public String getVariable() {
    String mapperVariable = element.getSimpleName().toString();
    return mapperVariable.substring(0, 1).toLowerCase(Locale.US) + mapperVariable.substring(1) + "Mapper";
  }
}
