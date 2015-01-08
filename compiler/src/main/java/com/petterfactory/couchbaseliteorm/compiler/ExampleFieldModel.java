package com.petterfactory.couchbaseliteorm.compiler;

import com.petterfactory.couchbaseliteorm.ExampleField;

import javax.lang.model.element.VariableElement;

/**
 * Created by brais on 7/1/15.
 */
public class ExampleFieldModel {
  private final VariableElement element;

  public ExampleFieldModel(VariableElement element) {
    this.element = element;
  }

  public VariableElement getElement() {
    return element;
  }

  public String getFieldName() {
    return element.getSimpleName().toString();
  }

  public String getTypeSimpleName() {
    String name = element.asType().toString();
    return name.substring(name.lastIndexOf(".") + 1);
  }

  public String getTypeQualifiedName() {
    return element.asType().toString();
  }

  public String getMapProperty() {
    return element.getAnnotation(ExampleField.class).value();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ExampleFieldModel that = (ExampleFieldModel) o;

    if (!element.equals(that.element)) return false;

    return true;
  }

  @Override
  public int hashCode() {
    return element.hashCode();
  }
}
