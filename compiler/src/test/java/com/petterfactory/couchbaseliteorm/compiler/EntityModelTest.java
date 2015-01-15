package com.petterfactory.couchbaseliteorm.compiler;

import com.petterfactory.couchbaseliteorm.Entity;
import com.petterfactory.couchbaseliteorm.Field;

import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import static com.google.common.truth.Truth.ASSERT;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by brais on 7/1/15.
 */
public class EntityModelTest {

  @Test
  public void checkGetElement() {
    TypeElement element = mock(TypeElement.class);

    EntityModel entity = new EntityModel(element);

    ASSERT.that(element).isEqualTo(entity.getElement());
  }

  @Test
  public void checkGetClassName() {
    Name simpleName = mock(Name.class);
    when(simpleName.toString()).thenReturn("Test");
    TypeElement element = mock(TypeElement.class);
    when(element.getSimpleName()).thenReturn(simpleName);

    EntityModel entity = new EntityModel(element);

    ASSERT.that("Test").isEqualTo(entity.getClassName());
  }

  @Test
  public void checkGetClassQualifiedName() {
    Name qualifiedName = mock(Name.class);
    when(qualifiedName.toString()).thenReturn("com.example.Test");
    TypeElement element = mock(TypeElement.class);
    when(element.getQualifiedName()).thenReturn(qualifiedName);

    EntityModel entity = new EntityModel(element);

    ASSERT.that("com.example.Test").isEqualTo(entity.getClassQualifiedName());
  }

  @Test
  public void checkGetMapperClassName() {
    Name simpleName = mock(Name.class);
    when(simpleName.toString()).thenReturn("Test");
    TypeElement element = mock(TypeElement.class);
    when(element.getSimpleName()).thenReturn(simpleName);

    EntityModel entity = new EntityModel(element);

    ASSERT.that("Test$$Mapper").isEqualTo(entity.getMapperClassName());
  }

  @Test
  public void checkGetMapperVariableName() {
    Name simpleName = mock(Name.class);
    when(simpleName.toString()).thenReturn("Test");
    TypeElement element = mock(TypeElement.class);
    when(element.getSimpleName()).thenReturn(simpleName);

    EntityModel entity = new EntityModel(element);

    ASSERT.that("testMapper").isEqualTo(entity.getMapperVariableName());
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

    EntityModel entity = new EntityModel(element);

    ASSERT.that("com.example").isEqualTo(entity.getPackageName());
  }

  @Test
  public void checkGetMapProperty() {
    Entity annotation = mock(Entity.class);
    when(annotation.value()).thenReturn("foo");
    TypeElement element = mock(TypeElement.class);
    when(element.getAnnotation(Matchers.<Class<Entity>>anyObject())).thenReturn(annotation);

    EntityModel entity = new EntityModel(element);

    ASSERT.that("foo").isEqualTo(entity.getAnnotationValue());
  }

  @Test
  public void checkHasAnnotationValue_true() {
    Entity annotation = mock(Entity.class);
    when(annotation.value()).thenReturn("foo");
    TypeElement element = mock(TypeElement.class);
    when(element.getAnnotation(Matchers.<Class<Entity>>anyObject())).thenReturn(annotation);

    EntityModel entity = new EntityModel(element);

    ASSERT.that(entity.hasAnnotationValue()).isTrue();
  }

  @Test
  public void checkHasAnnotationValue_false() {
    Entity annotation = mock(Entity.class);
    when(annotation.value()).thenReturn(Entity.DEFAULT_VALE);
    TypeElement element = mock(TypeElement.class);
    when(element.getAnnotation(Matchers.<Class<Entity>>anyObject())).thenReturn(annotation);

    EntityModel entity = new EntityModel(element);

    ASSERT.that(entity.hasAnnotationValue()).isFalse();
  }

  @Test
  public void checkGetFields_onlyReturnFieldTypes() {
    final Element e1 = mock(TypeElement.class);
    when(e1.getKind()).thenReturn(ElementKind.CLASS);

    Field annotation = mock(Field.class);
    TypeMirror typeMirror = mock(TypeMirror.class);
    when(typeMirror.toString()).thenReturn("com.example.Foo");
    final Element e2 = mock(VariableElement.class);
    when(e2.getKind()).thenReturn(ElementKind.FIELD);
    when(e2.getAnnotation(Matchers.<Class<Field>>anyObject())).thenReturn(annotation);
    when(e2.asType()).thenReturn(typeMirror);

    final Element e3 = mock(ExecutableElement.class);
    when(e3.getKind()).thenReturn(ElementKind.METHOD);

    TypeElement element = mock(TypeElement.class);
    when(element.getEnclosedElements()).thenAnswer(new Answer<List<Element>>() {
      public List<Element> answer(InvocationOnMock invocation) throws Throwable {
        return Arrays.asList(e1, e2, e3);
      }
    });

    EntityModel entity = new EntityModel(element);
    entity.fillFieldsList(new ArrayList<EntityModel>());

    ASSERT.that(Arrays.asList(new FieldModel((VariableElement) e2, null))).isEqualTo(entity.getFields());
  }

  @Test
  public void checkGetFields_onlyReturnAnnotatedTypes() {
    final Element e1 = mock(VariableElement.class);
    when(e1.getKind()).thenReturn(ElementKind.FIELD);

    Field annotation = mock(Field.class);
    TypeMirror typeMirror = mock(TypeMirror.class);
    when(typeMirror.toString()).thenReturn("com.example.Foo");
    final Element e2 = mock(VariableElement.class);
    when(e2.getKind()).thenReturn(ElementKind.FIELD);
    when(e2.getAnnotation(Matchers.<Class<Annotation>>anyObject())).thenReturn(annotation);
    when(e2.asType()).thenReturn(typeMirror);

    TypeElement element = mock(TypeElement.class);
    when(element.getEnclosedElements()).thenAnswer(new Answer<List<Element>>() {
      public List<Element> answer(InvocationOnMock invocation) throws Throwable {
        return Arrays.asList(e1, e2);
      }
    });

    EntityModel entity = new EntityModel(element);
    entity.fillFieldsList(new ArrayList<EntityModel>());

    ASSERT.that(Arrays.asList(new FieldModel((VariableElement) e2, null))).isEqualTo(entity.getFields());
  }
}
