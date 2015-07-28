package com.braisgabin.couchbaseliteorm.compiler;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import static com.google.common.truth.Truth.ASSERT;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HelperTest {

  private Elements elementUtils;
  private Types typeUtils;

  @Before
  public void initElementUtils() {
    elementUtils = mock(Elements.class);
    TypeElement typeElement = mock(TypeElement.class);
    when(elementUtils.getTypeElement(anyString())).thenReturn(typeElement);
  }

  @Before
  public void initTypeUtils() {
    typeUtils = mock(Types.class);
    TypeElement typeElement = mock(TypeElement.class);
    Name name = mock(Name.class);
    when(name.toString()).thenReturn(List.class.getCanonicalName());
    when(typeElement.getQualifiedName()).thenReturn(name);
    when(typeUtils.asElement(any(TypeMirror.class))).thenReturn(typeElement);
    when(typeUtils.isAssignable(any(TypeMirror.class), any(TypeMirror.class))).thenReturn(true);
  }

  @Test
  public void isACollection_true() {
    final Helper helper = new Helper(elementUtils, typeUtils);

    when(typeUtils.isAssignable(any(TypeMirror.class), any(TypeMirror.class))).thenReturn(true);

    ASSERT.that(helper.isACollection(mock(TypeMirror.class))).isTrue();
  }

  @Test
  public void isACollection_false() {
    final Helper helper = new Helper(elementUtils, typeUtils);

    when(typeUtils.isAssignable(any(TypeMirror.class), any(TypeMirror.class))).thenReturn(false);

    ASSERT.that(helper.isACollection(mock(TypeMirror.class))).isFalse();
  }
}
