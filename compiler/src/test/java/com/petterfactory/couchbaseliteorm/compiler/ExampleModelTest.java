package com.petterfactory.couchbaseliteorm.compiler;

import com.petterfactory.couchbaseliteorm.ExampleField;

import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

import static com.google.common.truth.Truth.ASSERT;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by brais on 7/1/15.
 */
public class ExampleModelTest {

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
  public void checkGetMapperClassName() {
    Name simpleName = mock(Name.class);
    when(simpleName.toString()).thenReturn("Test");
    TypeElement element = mock(TypeElement.class);
    when(element.getSimpleName()).thenReturn(simpleName);

    ExampleModel exampleModel = new ExampleModel(element);

    ASSERT.that("Test$$Mapper").isEqualTo(exampleModel.getMapperClassName());
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
  public void checkGetFields_onlyReturnFieldTypes() {
    final Element e1 = mock(TypeElement.class);
    when(e1.getKind()).thenReturn(ElementKind.CLASS);
    ExampleField annotation = mock(ExampleField.class);
    final Element e2 = mock(VariableElement.class);
    when(e2.getKind()).thenReturn(ElementKind.FIELD);
    when(e2.getAnnotation(Matchers.<Class<ExampleField>>anyObject())).thenReturn(annotation);
    final Element e3 = mock(ExecutableElement.class);
    when(e3.getKind()).thenReturn(ElementKind.METHOD);
    TypeElement element = mock(TypeElement.class);
    when(element.getEnclosedElements()).thenAnswer(new Answer<List<Element>>() {
      public List<Element> answer(InvocationOnMock invocation) throws Throwable {
        return Arrays.asList(e1, e2, e3);
      }
    });

    ExampleModel exampleModel = new ExampleModel(element);

    ASSERT.that(Arrays.asList(new ExampleFieldModel((VariableElement) e2))).isEqualTo(exampleModel.getFields());
  }

  @Test
  public void checkGetFields_onlyReturnAnnotatedTypes() {
    final Element e1 = mock(VariableElement.class);
    when(e1.getKind()).thenReturn(ElementKind.FIELD);
    ExampleField annotation = mock(ExampleField.class);
    final Element e2 = mock(VariableElement.class);
    when(e2.getKind()).thenReturn(ElementKind.FIELD);
    when(e2.getAnnotation(Matchers.<Class<Annotation>>anyObject())).thenReturn(annotation);
    TypeElement element = mock(TypeElement.class);
    when(element.getEnclosedElements()).thenAnswer(new Answer<List<Element>>() {
      public List<Element> answer(InvocationOnMock invocation) throws Throwable {
        return Arrays.asList(e1, e2);
      }
    });

    ExampleModel exampleModel = new ExampleModel(element);

    ASSERT.that(Arrays.asList(new ExampleFieldModel((VariableElement) e2))).isEqualTo(exampleModel.getFields());
  }
}
