package com.braisgabin.couchbaseliteorm.compiler;

import com.braisgabin.couchbaseliteorm.Entity;
import com.braisgabin.couchbaseliteorm.Field;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

/**
 * Created by brais on 7/1/15.
 */
public class EntityModel implements EntityData {
  private final TypeElement element;
  private final MapperModel mapperModel;
  private final List<FieldModel> fields;

  public EntityModel(TypeElement element) {
    this.element = element;
    this.mapperModel = new MapperModel(element);
    this.fields = new ArrayList<>();
  }

  public void fillFieldsList(Helper helper, List<EntityModel> models) {
    fields.clear();
    List<? extends Element> enclosedElements = element.getEnclosedElements();
    for (Element element : enclosedElements) {
      if (element.getKind() == ElementKind.FIELD) {
        final Field annotation = element.getAnnotation(Field.class);
        if (annotation != null) {
          final VariableElement variableElement = (VariableElement) element;
          final EntityModel dependency;
          if (FieldModel.getKind(helper, variableElement) == FieldKind.collection) {
            final DeclaredType typeMirror = (DeclaredType) variableElement.asType();
            final List<? extends TypeMirror> typeArguments = typeMirror.getTypeArguments();
            if (typeArguments.isEmpty()) {
              dependency = null;
            } else {
              dependency = findModel(models, typeArguments.get(0).toString());
            }
          } else {
            dependency = findModel(models, variableElement.asType().toString());
          }
          fields.add(new FieldModel(variableElement, dependency));
        }
      }
    }
  }

  private static EntityModel findModel(List<EntityModel> models, String fullQualifiedName) {
    for (EntityModel model : models) {
      if (model.getFullQualifiedName().equals(fullQualifiedName)) {
        return model;
      }
    }
    return null;
  }

  public Element getElement() {
    return element;
  }

  @Override
  public String getName() {
    return element.getSimpleName().toString();
  }

  @Override
  public String getFullQualifiedName() {
    return element.getQualifiedName().toString();
  }

  @Override
  public String getPackage() {
    final String simpleName = element.getSimpleName().toString();
    final String qualifiedName = element.getQualifiedName().toString();
    return qualifiedName.substring(0, qualifiedName.length() - simpleName.length() - 1);
  }

  @Override
  public String getVariable() {
    String mapperVariable = element.getSimpleName().toString();
    return mapperVariable.substring(0, 1).toLowerCase(Locale.US) + mapperVariable.substring(1);
  }

  public MapperModel getMapper() {
    return mapperModel;
  }

  public boolean hasAnnotationValue() {
    return !element.getAnnotation(Entity.class).value().equals(Entity.DEFAULT_VALE);
  }

  public String getAnnotationValue() {
    return element.getAnnotation(Entity.class).value();
  }

  public List<FieldModel> getFields() {
    return fields;
  }

  public List<EntityModel> getDependencies() {
    List<EntityModel> dependencies = new ArrayList<>();
    for (FieldModel fieldModel : fields) {
      EntityModel entityModel = fieldModel.getDependency();
      if (entityModel != null && !dependencies.contains(entityModel)) {
        dependencies.add(entityModel);
      }
    }
    return dependencies;
  }
}
