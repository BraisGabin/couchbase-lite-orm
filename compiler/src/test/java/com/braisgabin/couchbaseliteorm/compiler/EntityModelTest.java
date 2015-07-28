package com.braisgabin.couchbaseliteorm.compiler;

import com.braisgabin.couchbaseliteorm.Entity;
import com.braisgabin.couchbaseliteorm.Field;

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
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import static com.google.common.truth.Truth.ASSERT;
import static org.mockito.Matchers.any;
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
  public void checkGetName() {
    Name simpleName = mock(Name.class);
    when(simpleName.toString()).thenReturn("Test");
    TypeElement element = mock(TypeElement.class);
    when(element.getSimpleName()).thenReturn(simpleName);

    EntityModel entity = new EntityModel(element);

    ASSERT.that("Test").isEqualTo(entity.getName());
  }

  @Test
  public void checkGetFullQualifiedName() {
    Name qualifiedName = mock(Name.class);
    when(qualifiedName.toString()).thenReturn("com.example.Test");
    TypeElement element = mock(TypeElement.class);
    when(element.getQualifiedName()).thenReturn(qualifiedName);

    EntityModel entity = new EntityModel(element);

    ASSERT.that("com.example.Test").isEqualTo(entity.getFullQualifiedName());
  }

  @Test
  public void checkGetPackage() {
    Name name = mock(Name.class);
    when(name.toString()).thenReturn("Test");
    Name qualifiedName = mock(Name.class);
    when(qualifiedName.toString()).thenReturn("com.example.Test");
    TypeElement element = mock(TypeElement.class);
    when(element.getSimpleName()).thenReturn(name);
    when(element.getQualifiedName()).thenReturn(qualifiedName);

    EntityModel entity = new EntityModel(element);

    ASSERT.that("com.example").isEqualTo(entity.getPackage());
  }

  @Test
  public void checkGetVariable() {
    Name qualifiedName = mock(Name.class);
    when(qualifiedName.toString()).thenReturn("Test");
    TypeElement element = mock(TypeElement.class);
    when(element.getSimpleName()).thenReturn(qualifiedName);

    EntityModel entity = new EntityModel(element);

    ASSERT.that("test").isEqualTo(entity.getVariable());
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
    when(typeMirror.getKind()).thenReturn(TypeKind.OTHER);
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

    Helper helper = mock(Helper.class);
    when(helper.isACollection(any(TypeMirror.class))).thenReturn(false);

    entity.fillFieldsList(helper, new ArrayList<EntityModel>());

    ASSERT.that(Arrays.asList(new FieldModel((VariableElement) e2, null))).isEqualTo(entity.getFields());
  }

  @Test
  public void checkGetFields_onlyReturnAnnotatedTypes() {
    final Element e1 = mock(VariableElement.class);
    when(e1.getKind()).thenReturn(ElementKind.FIELD);

    Field annotation = mock(Field.class);
    TypeMirror typeMirror = mock(TypeMirror.class);
    when(typeMirror.toString()).thenReturn("com.example.Foo");
    when(typeMirror.getKind()).thenReturn(TypeKind.OTHER);
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

    Helper helper = mock(Helper.class);
    when(helper.isACollection(any(TypeMirror.class))).thenReturn(false);

    entity.fillFieldsList(helper, new ArrayList<EntityModel>());

    ASSERT.that(Arrays.asList(new FieldModel((VariableElement) e2, null))).isEqualTo(entity.getFields());
  }
}
