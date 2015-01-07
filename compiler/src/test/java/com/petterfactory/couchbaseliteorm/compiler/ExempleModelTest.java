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
public class ExempleModelTest {

  @Test
  public void checkGetElement() {
    TypeElement element = mock(TypeElement.class);

    ExampleModel exampleModel = new ExampleModel(element);

    ASSERT.that(element).isEqualTo(exampleModel.getElement());
  }

  @Test
  public void checkGetClassName() {
    Name simpleName = mock(Name.class);
    when(simpleName.toString()).thenReturn("Test");
    TypeElement element = mock(TypeElement.class);
    when(element.getSimpleName()).thenReturn(simpleName);

    ExampleModel exampleModel = new ExampleModel(element);

    ASSERT.that("Test").isEqualTo(exampleModel.getClassName());
  }

  @Test
  public void checkGetPackageName() {
    Name simpleName = mock(Name.class);
    when(simpleName.toString()).thenReturn("Test");
    Name qualifiedName = mock(Name.class);
    when(qualifiedName.toString()).thenReturn("com.example.Test");
    TypeElement element = mock(TypeElement.class);
    when(element.getSimpleName()).thenReturn(simpleName);
    when(element.getQualifiedName()).thenReturn(qualifiedName);

    ExampleModel exampleModel = new ExampleModel(element);

    ASSERT.that("com.example").isEqualTo(exampleModel.getPackageName());
  }

  @Test
  public void checkGetPackageName_default() {
    Name simpleName = mock(Name.class);
    when(simpleName.toString()).thenReturn("Test");
    Name qualifiedName = mock(Name.class);
    when(qualifiedName.toString()).thenReturn("Test");
    TypeElement element = mock(TypeElement.class);
    when(element.getSimpleName()).thenReturn(simpleName);
    when(element.getQualifiedName()).thenReturn(qualifiedName);

    ExampleModel exampleModel = new ExampleModel(element);

    ASSERT.that("").isEqualTo(exampleModel.getPackageName());
  }
}
