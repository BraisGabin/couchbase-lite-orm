package com.petterfactory.couchbaseliteorm.compiler;

import com.petterfactory.couchbaseliteorm.Field;

import org.junit.Test;
import org.mockito.Matchers;

import java.util.Arrays;

import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import static com.google.common.truth.Truth.ASSERT;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by brais on 8/1/15.
 */
public class FieldModelTest {

  @Test
  public void checkGetElement() {
    VariableElement element = mock(VariableElement.class);

    FieldModel model = new FieldModel(element, null);

    ASSERT.that(element).isEqualTo(model.getElement());
  }

  @Test
  public void checkGetFieldName() {
    Name simpleName = mock(Name.class);
    when(simpleName.toString()).thenReturn("test");
    VariableElement element = mock(VariableElement.class);
    when(element.getSimpleName()).thenReturn(simpleName);

    FieldModel model = new FieldModel(element, null);

    ASSERT.that("test").isEqualTo(model.getFieldName());
  }

  @Test
  public void checkGetTypeSimpleName() {
    TypeMirror typeMirror = mock(TypeMirror.class);
    when(typeMirror.toString()).thenReturn("com.example.Foo");
    VariableElement element = mock(VariableElement.class);
    when(element.asType()).thenReturn(typeMirror);

    FieldModel model = new FieldModel(element, null);

    ASSERT.that("Foo").isEqualTo(model.getTypeSimpleName());
  }

  @Test
  public void checkGetTypeSimpleName_generics() {
    TypeMirror typeMirror = mock(TypeMirror.class);
    when(typeMirror.toString()).thenReturn("com.example.Foo<com.example.Bar>");
    VariableElement element = mock(VariableElement.class);
    when(element.asType()).thenReturn(typeMirror);

    FieldModel model = new FieldModel(element, null);

    ASSERT.that("Foo<Bar>").isEqualTo(model.getTypeSimpleName());
  }

  @Test
  public void checkGetTypeSimpleName_genericsMultiples() {
    TypeMirror typeMirror = mock(TypeMirror.class);
    when(typeMirror.toString()).thenReturn("com.example.Foo<com.example.Bar, com.example.Baz>");
    VariableElement element = mock(VariableElement.class);
    when(element.asType()).thenReturn(typeMirror);

    FieldModel model = new FieldModel(element, null);

    ASSERT.that("Foo<Bar, Baz>").isEqualTo(model.getTypeSimpleName());
  }

  @Test
  public void checkGetTypeQualifiedName() {
    TypeMirror typeMirror = mock(TypeMirror.class);
    when(typeMirror.toString()).thenReturn("com.example.Foo");
    VariableElement element = mock(VariableElement.class);
    when(element.asType()).thenReturn(typeMirror);

    FieldModel model = new FieldModel(element, null);

    ASSERT.that("com.example.Foo").isEqualTo(model.getTypeQualifiedName());
  }

  @Test
  public void checkGetTypeQualifiedNames() {
    TypeMirror typeMirror = mock(TypeMirror.class);
    when(typeMirror.toString()).thenReturn("com.example.Foo");
    VariableElement element = mock(VariableElement.class);
    when(element.asType()).thenReturn(typeMirror);

    FieldModel model = new FieldModel(element, null);

    ASSERT.that(Arrays.asList("com.example.Foo")).isEqualTo(model.getTypeQualifiedNames());
  }

  @Test
  public void checkGetTypeQualifiedNames_generics() {
    TypeMirror typeMirror = mock(TypeMirror.class);
    when(typeMirror.toString()).thenReturn("com.example.Foo<com.example.Bar>");
    VariableElement element = mock(VariableElement.class);
    when(element.asType()).thenReturn(typeMirror);

    FieldModel model = new FieldModel(element, null);

    ASSERT.that(Arrays.asList("com.example.Foo", "com.example.Bar")).isEqualTo(model.getTypeQualifiedNames());
  }

  @Test
  public void checkGetTypeQualifiedNames_genericsMultiples() {
    TypeMirror typeMirror = mock(TypeMirror.class);
    when(typeMirror.toString()).thenReturn("com.example.Foo<com.example.Bar, com.example.Baz>");
    VariableElement element = mock(VariableElement.class);
    when(element.asType()).thenReturn(typeMirror);

    FieldModel model = new FieldModel(element, null);

    ASSERT.that(Arrays.asList("com.example.Foo", "com.example.Bar", "com.example.Baz")).isEqualTo(model.getTypeQualifiedNames());
  }

  @Test
  public void checkGetMapProperty() {
    Field annotation = mock(Field.class);
    when(annotation.value()).thenReturn("foo");
    VariableElement element = mock(VariableElement.class);
    when(element.getAnnotation(Matchers.<Class<Field>>anyObject())).thenReturn(annotation);

    FieldModel model = new FieldModel(element, null);

    ASSERT.that("foo").isEqualTo(model.getMapProperty());
  }
}
