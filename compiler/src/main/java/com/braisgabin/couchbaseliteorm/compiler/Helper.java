package com.braisgabin.couchbaseliteorm.compiler;

import java.util.Collection;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

public class Helper {
  private final Elements elementUtils;
  private final Types typeUtils;

  public Helper(Elements elementUtils, Types typeUtils) {
    this.elementUtils = elementUtils;
    this.typeUtils = typeUtils;
  }

  public boolean isACollection(TypeMirror typeMirror) {
    TypeMirror collectionType = typeUtils.getDeclaredType(
        elementUtils.getTypeElement(Collection.class.getCanonicalName()),
        typeUtils.getWildcardType(null, null)
    );

    final TypeElement typeElement = (TypeElement) typeUtils.asElement(typeMirror);
    final String qualifiedName = typeElement.getQualifiedName().toString();
    final TypeMirror copy = elementUtils.getTypeElement(qualifiedName).asType();

    return typeUtils.isAssignable(copy, collectionType);
  }
}
