package com.petterfactory.couchbaseliteorm.compiler;

import com.petterfactory.couchbaseliteorm.Mapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Filer;

import static javax.lang.model.element.Modifier.PUBLIC;

public class MapperEmitter extends Emitter {
  private final EntityModel model;

  public MapperEmitter(EntityModel model, Filer filer) throws IOException {
    super(filer, model.getMapper().getPackage(), model.getMapper().getName(), model.getElement());
    this.model = model;
  }

  @Override
  protected Set<String> getImports() {
    final Set<String> imports = new HashSet<>(Arrays.asList(
        Map.class.getCanonicalName(),
        HashMap.class.getCanonicalName(),
        Mapper.class.getCanonicalName()
    ));
    for (FieldModel fieldModel : model.getFields()) {
      imports.addAll(fieldModel.getType().getFullQualifiedNames());
    }
    return imports;
  }

  @Override
  protected void emitClass() throws IOException {
    final String entityName = model.getName();
    final MapperModel mapper = model.getMapper();
    final String mapperClassName = mapper.getName();

    writer
        .beginType(mapperClassName, "class", EnumSet.of(PUBLIC), null, "Mapper<" + entityName + ">");

    emitFields();
    emitToObject();
    emitToProperties();

    writer
        .endType();
  }

  private void emitFields() throws IOException {
    for (FieldModel fieldModel : model.getFields()) {
      final MapperModel mapperDependency = fieldModel.getDependencyMapperModel();
      if (mapperDependency != null) {
        writer
            .emitField(mapperDependency.getName(), mapperDependency.getVariable(), EnumSet.of(PUBLIC));
      }
    }
  }

  private void emitToObject() throws IOException {
    final String entityName = model.getName();

    writer
        .emitAnnotation(Override.class)
        .beginMethod(entityName, "toObject", EnumSet.of(PUBLIC), "Map<String, Object>", "properties")
        .emitStatement("final %s object = new %s()", entityName, entityName);
    for (FieldModel fieldModel : model.getFields()) {
      switch (fieldModel.getKind()) {
        case primitive:
          writer
              .beginControlFlow("try")
              .emitStatement("object.%s = (%s) properties.get(\"%s\")", fieldModel.getName(), fieldModel.getType().getName(), fieldModel.getPropertyKey())
              .nextControlFlow("catch (NullPointerException e)")
              .beginControlFlow("if (!properties.containsKey(\"%s\"))", fieldModel.getName(), fieldModel.getPropertyKey())
              .emitStatement("throw new IllegalStateException(\"The property \\\"%s\\\" is not setted.\")", fieldModel.getPropertyKey())
              .endControlFlow()
              .emitStatement("throw new NullPointerException(\"The property \\\"%s\\\" has the value null. It can't be set to a %s.\")", fieldModel.getPropertyKey(), fieldModel.getType().getName())
              .endControlFlow();
          break;
        case simpleObject:
          writer
              .emitStatement("object.%s = (%s) properties.get(\"%s\")", fieldModel.getName(), fieldModel.getType().getName(), fieldModel.getPropertyKey())
              .beginControlFlow("if (object.%s == null && !properties.containsKey(\"%s\"))", fieldModel.getName(), fieldModel.getPropertyKey())
              .emitStatement("throw new IllegalStateException(\"The property \\\"%s\\\" is not setted.\")", fieldModel.getPropertyKey())
              .endControlFlow();
          break;
        case object:
          final MapperModel mapperDependency = fieldModel.getDependencyMapperModel();
          writer
              .beginControlFlow("if (!properties.containsKey(\"%s\"))", fieldModel.getPropertyKey())
              .emitStatement("throw new IllegalStateException(\"The property \\\"%s\\\" is not setted.\")", fieldModel.getPropertyKey())
              .endControlFlow()
              .beginControlFlow("if (properties.get(\"%s\") != null)", fieldModel.getPropertyKey())
              .emitStatement("object.%s = %s.toObject((Map<String, Object>) properties.get(\"%s\"))", fieldModel.getName(), mapperDependency.getVariable(), fieldModel.getPropertyKey())
              .nextControlFlow("else")
              .emitStatement("object.%s = null", fieldModel.getName())
              .endControlFlow();
          break;
        case list:
          writer
              .emitStatement("object.%s = (%s) properties.get(\"%s\")", fieldModel.getName(), fieldModel.getType().getName(), fieldModel.getPropertyKey())
              .beginControlFlow("if (object.%s == null && !properties.containsKey(\"%s\"))", fieldModel.getName(), fieldModel.getPropertyKey())
              .emitStatement("throw new IllegalStateException(\"The property \\\"%s\\\" is not setted.\")", fieldModel.getPropertyKey())
              .endControlFlow();
          break;
        default:
      }
    }
    writer
        .emitStatement("return object")
        .endMethod();
  }

  private void emitToProperties() throws IOException {
    final String entityName = model.getName();

    writer
        .emitAnnotation(Override.class)
        .beginMethod("Map<String, Object>", "toProperties", EnumSet.of(PUBLIC), entityName, "object")
        .emitStatement("final Map<String, Object> properties = new HashMap<>()");
    if (model.hasAnnotationValue()) {
      writer
          .emitStatement("properties.put(\"type\", \"%s\")", model.getAnnotationValue());
    }
    for (FieldModel fieldModel : model.getFields()) {
      final MapperModel dependencyMapper = fieldModel.getDependencyMapperModel();
      if (dependencyMapper == null) {
        writer
            .emitStatement("properties.put(\"%s\", object.%s)", fieldModel.getPropertyKey(), fieldModel.getName());
      } else {
        writer
            .emitStatement("properties.put(\"%s\", object.%s == null ? null : %s.toProperties(object.%s))", fieldModel.getPropertyKey(), fieldModel.getName(), dependencyMapper.getVariable(), fieldModel.getName());
      }
    }
    writer
        .emitStatement("return properties")
        .endMethod();
  }
}