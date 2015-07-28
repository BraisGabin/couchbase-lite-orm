package com.braisgabin.couchbaseliteorm.compiler;

import org.junit.Test;

import java.util.Arrays;

import javax.lang.model.type.TypeMirror;

import static com.google.common.truth.Truth.ASSERT;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by brais on 8/1/15.
 */
public class TypeModelTest {

  @Test
  public void checkGetName() {
    TypeMirror typeMirror = mock(TypeMirror.class);
    when(typeMirror.toString()).thenReturn("com.example.Foo");

    TypeModel model = new TypeModel(typeMirror);

    ASSERT.that("Foo").isEqualTo(model.getName());
  }

  @Test
  public void checkGetName_generics() {
    TypeMirror typeMirror = mock(TypeMirror.class);
    when(typeMirror.toString()).thenReturn("com.example.Foo<com.example.Bar>");

    TypeModel model = new TypeModel(typeMirror);

    ASSERT.that("Foo<Bar>").isEqualTo(model.getName());
  }

  @Test
  public void checkGetName_genericsMultiples() {
    TypeMirror typeMirror = mock(TypeMirror.class);
    when(typeMirror.toString()).thenReturn("com.example.Foo<com.example.Bar, com.example.Baz>");

    TypeModel model = new TypeModel(typeMirror);

    ASSERT.that("Foo<Bar, Baz>").isEqualTo(model.getName());
  }

  @Test
  public void checkGetPropertyKey() {
    TypeMirror typeMirror = mock(TypeMirror.class);
    when(typeMirror.toString()).thenReturn("com.example.Foo");

    TypeModel model = new TypeModel(typeMirror);

    ASSERT.that(Arrays.asList("com.example.Foo")).isEqualTo(model.getFullQualifiedNames());
  }

  @Test
  public void checkGetPropertyKey_generics() {
    TypeMirror typeMirror = mock(TypeMirror.class);
    when(typeMirror.toString()).thenReturn("com.example.Foo<com.example.Bar>");

    TypeModel model = new TypeModel(typeMirror);

    ASSERT.that(Arrays.asList("com.example.Foo", "com.example.Bar")).isEqualTo(model.getFullQualifiedNames());
  }

  @Test
  public void checkGetPropertyKey_genericsMultiples() {
    TypeMirror typeMirror = mock(TypeMirror.class);
    when(typeMirror.toString()).thenReturn("com.example.Foo<com.example.Bar, com.example.Baz>");

    TypeModel model = new TypeModel(typeMirror);

    ASSERT.that(Arrays.asList("com.example.Foo", "com.example.Bar", "com.example.Baz")).isEqualTo(model.getFullQualifiedNames());
  }
}
