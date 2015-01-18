package com.petterfactory.couchbaseliteorm.compiler;

import com.petterfactory.couchbaseliteorm.Field;

import org.junit.Test;
import org.mockito.Matchers;

import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;

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
}
