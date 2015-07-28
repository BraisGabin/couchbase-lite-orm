package com.braisgabin.couchbaseliteorm.compiler;

import com.braisgabin.couchbaseliteorm.Field;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import static com.braisgabin.couchbaseliteorm.compiler.FieldKind.collection;
import static com.braisgabin.couchbaseliteorm.compiler.FieldKind.isSimpleObject;
import static com.braisgabin.couchbaseliteorm.compiler.FieldKind.object;
import static com.braisgabin.couchbaseliteorm.compiler.FieldKind.primitive;
import static com.braisgabin.couchbaseliteorm.compiler.FieldKind.simpleObject;

/**
 * Created by brais on 7/1/15.
 */
public class FieldModel {
  private final VariableElement element;
  private final TypeModel type;
  private final EntityModel dependency;

  public FieldModel(VariableElement element, EntityModel dependency) {
    this.element = element;
    this.type = new TypeModel(element.asType());
    this.dependency = dependency;
  }

  public static FieldKind getKind(Helper helper, VariableElement element) {
    final FieldKind fieldKind;
    final TypeMirror typeMirror = element.asType();
    if (typeMirror.getKind().isPrimitive()) {
      fieldKind = primitive;
    } else if (isSimpleObject(typeMirror.toString())) {
      fieldKind = simpleObject;
    } else if (helper.isACollection(typeMirror)) {
      fieldKind = collection;
    } else {
      fieldKind = object;
    }
    return fieldKind;
  }

  public VariableElement getElement() {
    return element;
  }

  public String getName() {
    return element.getSimpleName().toString();
  }

  public TypeModel getType() {
    return type;
  }

  public String getPropertyKey() {
    return element.getAnnotation(Field.class).value();
  }

  public EntityModel getDependency() {
    return dependency;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    FieldModel that = (FieldModel) o;

    if (!element.equals(that.element)) return false;

    return true;
  }

  @Override
  public int hashCode() {
    return element.hashCode();
  }

  public FieldKind getKind(Helper helper) {
    return getKind(helper, element);
  }
}
