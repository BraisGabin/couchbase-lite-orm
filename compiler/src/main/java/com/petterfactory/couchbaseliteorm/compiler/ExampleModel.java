package com.petterfactory.couchbaseliteorm.compiler;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

/**
 * Created by brais on 7/1/15.
 */
public class ExampleModel {
  private final TypeElement element;

  public ExampleModel(TypeElement element) {
    this.element = element;
  }

  public Element getElement() {
    return element;
  }

  public String getClassName() {
    return element.getSimpleName().toString();
  }

  public String getPackageName() {
    final String simpleName = element.getSimpleName().toString();
    final String qualifiedName = element.getQualifiedName().toString();
    return qualifiedName.substring(0, qualifiedName.length() - simpleName.length() - 1);
  }
}
