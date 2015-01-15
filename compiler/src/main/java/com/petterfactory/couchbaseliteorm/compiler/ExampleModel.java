package com.petterfactory.couchbaseliteorm.compiler;

import com.petterfactory.couchbaseliteorm.Example;
import com.petterfactory.couchbaseliteorm.ExampleField;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

/**
 * Created by brais on 7/1/15.
 */
public class ExampleModel {
  private final TypeElement element;
  private final List<ExampleFieldModel> fields;

  public ExampleModel(TypeElement element) {
    this.element = element;
    this.fields = new ArrayList<>();
  }

  public void fillFieldsList(List<ExampleModel> models) {
    fields.clear();
    List<? extends Element> enclosedElements = element.getEnclosedElements();
    for (Element element : enclosedElements) {
      if (element.getKind() == ElementKind.FIELD) {
        final ExampleField annotation = element.getAnnotation(ExampleField.class);
        if (annotation != null) {
          final VariableElement variableElement = (VariableElement) element;
          fields.add(new ExampleFieldModel(variableElement, findModel(models, variableElement.asType().toString())));
        }
      }
    }
  }

  private static ExampleModel findModel(List<ExampleModel> models, String fullQualifiedName) {
    for (ExampleModel model : models) {
      if (model.getClassQualifiedName().equals(fullQualifiedName)) {
        return model;
      }
    }
    return null;
  }

  public Element getElement() {
    return element;
  }

  public String getClassName() {
    return element.getSimpleName().toString();
  }

  public String getClassQualifiedName() {
    return element.getQualifiedName().toString();
  }

  public String getMapperClassName() {
    return element.getSimpleName().toString() + "$$Mapper";
  }

  public String getMapperClassQualifiedName() {
    return element.getQualifiedName().toString() + "$$Mapper";
  }

  public String getMapperVariableName() {
    String mapperVariable = element.getSimpleName().toString();
    return mapperVariable.substring(0, 1).toLowerCase(Locale.US) + mapperVariable.substring(1) + "Mapper";
  }

  public String getPackageName() {
    final String simpleName = element.getSimpleName().toString();
    final String qualifiedName = element.getQualifiedName().toString();
    return qualifiedName.substring(0, qualifiedName.length() - simpleName.length() - 1);
  }

  public String getAnnotationValue() {
    return element.getAnnotation(Example.class).value();
  }

  public boolean hasAnnotationValue() {
    return !element.getAnnotation(Example.class).value().equals(Example.DEFAULT_VALE);
  }

  public List<ExampleFieldModel> getFields() {
    return fields;
  }
}
