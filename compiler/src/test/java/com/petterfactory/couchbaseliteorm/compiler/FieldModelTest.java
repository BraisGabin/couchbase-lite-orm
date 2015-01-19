package com.petterfactory.couchbaseliteorm.compiler;

import com.petterfactory.couchbaseliteorm.Field;

import org.junit.Test;
import org.mockito.Matchers;

import java.util.List;

import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import static com.google.common.truth.Truth.ASSERT;
import static org.mockito.Matchers.any;
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
  public void checkGetName() {
    Name simpleName = mock(Name.class);
    when(simpleName.toString()).thenReturn("test");
    VariableElement element = mock(VariableElement.class);
    when(element.getSimpleName()).thenReturn(simpleName);

    FieldModel model = new FieldModel(element, null);

    ASSERT.that("test").isEqualTo(model.getName());
  }

  @Test
  public void checkGetPropertyKey() {
    Field annotation = mock(Field.class);
    when(annotation.value()).thenReturn("foo");
    VariableElement element = mock(VariableElement.class);
    when(element.getAnnotation(Matchers.<Class<Field>>anyObject())).thenReturn(annotation);

    FieldModel model = new FieldModel(element, null);

    ASSERT.that("foo").isEqualTo(model.getPropertyKey());
  }

  @Test
  public void checkGetKind_primitive() {
    TypeMirror typeMirror = mock(TypeMirror.class);
    when(typeMirror.getKind()).thenReturn(TypeKind.INT);
    VariableElement element = mock(VariableElement.class);
    when(element.asType()).thenReturn(typeMirror);

    FieldModel model = new FieldModel(element, null);

    Helper helper = mock(Helper.class);
    when(helper.isACollection(any(TypeMirror.class))).thenReturn(false);

    ASSERT.that(FieldKind.primitive).isEqualTo(model.getKind(helper));
  }

  @Test
  public void checkGetKind_simpleObject() {
    TypeMirror typeMirror = mock(TypeMirror.class);
    when(typeMirror.getKind()).thenReturn(TypeKind.OTHER);
    when(typeMirror.toString()).thenReturn("java.lang.String");
    VariableElement element = mock(VariableElement.class);
    when(element.asType()).thenReturn(typeMirror);

    FieldModel model = new FieldModel(element, null);

    Helper helper = mock(Helper.class);
    when(helper.isACollection(any(TypeMirror.class))).thenReturn(false);

    ASSERT.that(FieldKind.simpleObject).isEqualTo(model.getKind(helper));
  }

  @Test
  public void checkGetKind_object() {
    TypeMirror typeMirror = mock(TypeMirror.class);
    when(typeMirror.getKind()).thenReturn(TypeKind.OTHER);
    when(typeMirror.toString()).thenReturn(Object.class.getCanonicalName());
    VariableElement element = mock(VariableElement.class);
    when(element.asType()).thenReturn(typeMirror);

    FieldModel model = new FieldModel(element, mock(EntityModel.class));

    Helper helper = mock(Helper.class);
    when(helper.isACollection(any(TypeMirror.class))).thenReturn(false);

    ASSERT.that(FieldKind.object).isEqualTo(model.getKind(helper));
  }

  @Test
  public void checkGetKind_list() {
    TypeMirror typeMirror = mock(TypeMirror.class);
    when(typeMirror.getKind()).thenReturn(TypeKind.OTHER);
    when(typeMirror.toString()).thenReturn(List.class.getCanonicalName());
    VariableElement element = mock(VariableElement.class);
    when(element.asType()).thenReturn(typeMirror);

    FieldModel model = new FieldModel(element, null);

    Helper helper = mock(Helper.class);
    when(helper.isACollection(any(TypeMirror.class))).thenReturn(true);

    ASSERT.that(FieldKind.collection).isEqualTo(model.getKind(helper));
  }
}
