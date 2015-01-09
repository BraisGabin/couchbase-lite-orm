package com.petterfactory.couchbaseliteorm.compiler;

import com.petterfactory.couchbaseliteorm.ExampleField;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

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

  public String getMapperClassName() {
    return element.getSimpleName().toString() + "$$Mapper";
  }

  public String getPackageName() {
    final String simpleName = element.getSimpleName().toString();
    final String qualifiedName = element.getQualifiedName().toString();
    return qualifiedName.substring(0, qualifiedName.length() - simpleName.length() - 1);
  }

  public List<ExampleFieldModel> getFields() {
    final List<ExampleFieldModel> fields = new ArrayList<>();
    List<? extends Element> enclosedElements = element.getEnclosedElements();
    for (Element element : enclosedElements) {
      if (element.getKind() == ElementKind.FIELD) {
        ExampleField annotation = element.getAnnotation(ExampleField.class);
        if (annotation != null) {
          fields.add(new ExampleFieldModel((VariableElement) element));
        }
      }
    }
    return fields;
  }
}
