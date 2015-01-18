package com.petterfactory.couchbaseliteorm.compiler;

import org.junit.Test;

import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;

import static com.google.common.truth.Truth.ASSERT;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by brais on 7/1/15.
 */
public class MapperModelTest {

  @Test
  public void checkGetName() {
    Name simpleName = mock(Name.class);
    when(simpleName.toString()).thenReturn("Test");
    TypeElement element = mock(TypeElement.class);
    when(element.getSimpleName()).thenReturn(simpleName);

    MapperModel object = new MapperModel(element);

    ASSERT.that("Test$$Mapper").isEqualTo(object.getName());
  }

  @Test
  public void checkGetFullQualifiedName() {
    Name simpleName = mock(Name.class);
    when(simpleName.toString()).thenReturn("com.example.Test");
    TypeElement element = mock(TypeElement.class);
    when(element.getQualifiedName()).thenReturn(simpleName);

    MapperModel object = new MapperModel(element);

    ASSERT.that("com.example.Test$$Mapper").isEqualTo(object.getFullQualifiedName());
  }

  @Test
  public void checkGetPackage() {
    Name simpleName = mock(Name.class);
    when(simpleName.toString()).thenReturn("Test");
    Name qualifiedName = mock(Name.class);
    when(qualifiedName.toString()).thenReturn("com.example.Test");
    TypeElement element = mock(TypeElement.class);
    when(element.getSimpleName()).thenReturn(simpleName);
    when(element.getQualifiedName()).thenReturn(qualifiedName);

    MapperModel object = new MapperModel(element);

    ASSERT.that("com.example").isEqualTo(object.getPackage());
  }

  @Test
  public void checkGetVariable() {
    Name simpleName = mock(Name.class);
    when(simpleName.toString()).thenReturn("Test");
    TypeElement element = mock(TypeElement.class);
    when(element.getSimpleName()).thenReturn(simpleName);

    MapperModel object = new MapperModel(element);

    ASSERT.that("testMapper").isEqualTo(object.getVariable());
  }
}
