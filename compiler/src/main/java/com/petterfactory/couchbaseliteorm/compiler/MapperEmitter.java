package com.petterfactory.couchbaseliteorm.compiler;

import com.petterfactory.couchbaseliteorm.Mapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Filer;

import static javax.lang.model.element.Modifier.PUBLIC;

public class MapperEmitter extends Emitter {
  private final Helper helper;
  private final EntityModel model;

  public MapperEmitter(Helper helper, Filer filer, EntityModel model) throws IOException {
    super(filer, model.getMapper().getPackage(), model.getMapper().getName(), model.getElement());
    this.helper = helper;
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
      final FieldKind kind = fieldModel.getKind(helper);
      if (kind == FieldKind.collection && fieldModel.getDependency() != null) {
        imports.add(ArrayList.class.getCanonicalName());
        imports.add(Collection.class.getCanonicalName());
      }
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
    for (EntityModel dependency : model.getDependencies()) {
      final MapperModel mapperDependency = dependency.getMapper();
      writer
          .emitField(mapperDependency.getName(), mapperDependency.getVariable(), EnumSet.of(PUBLIC));
    }
  }

  private void emitToObject() throws IOException {
    final String entityName = model.getName();

    writer
        .emitAnnotation(Override.class)
        .beginMethod(entityName, "toObject", EnumSet.of(PUBLIC), "Map<String, Object>", "properties")
        .emitStatement("final %s object = new %s()", entityName, entityName);
    for (FieldModel fieldModel : model.getFields()) {
      final EntityModel dependency;
      switch (fieldModel.getKind(helper)) {
        case primitive:
          writer
              .beginControlFlow("try")
              .emitStatement("object.%s = (%s) properties.get(\"%s\")", fieldModel.getName(), fieldModel.getType().getName(), fieldModel.getPropertyKey())
              .nextControlFlow("catch (NullPointerException e)")
              .beginControlFlow("if (!properties.containsKey(\"%s\"))", fieldModel.getName(), fieldModel.getPropertyKey())
              .emitStatement("throw new IllegalStateException(\"The property \\\"%s\\\" is not set.\")", fieldModel.getPropertyKey())
              .endControlFlow()
              .emitStatement("throw new NullPointerException(\"The property \\\"%s\\\" has the value null. It can't be set to a %s.\")", fieldModel.getPropertyKey(), fieldModel.getType().getName())
              .endControlFlow();
          break;
        case simpleObject:
          writer
              .emitStatement("object.%s = (%s) properties.get(\"%s\")", fieldModel.getName(), fieldModel.getType().getName(), fieldModel.getPropertyKey())
              .beginControlFlow("if (object.%s == null && !properties.containsKey(\"%s\"))", fieldModel.getName(), fieldModel.getPropertyKey())
              .emitStatement("throw new IllegalStateException(\"The property \\\"%s\\\" is not set.\")", fieldModel.getPropertyKey())
              .endControlFlow();
          break;
        case object:
          dependency = fieldModel.getDependency();
          writer
              .beginControlFlow("if (properties.get(\"%s\") != null)", fieldModel.getPropertyKey()) // FIXME two calls to properties.get
              .emitStatement("object.%s = %s.toObject((Map<String, Object>) properties.get(\"%s\"))", fieldModel.getName(), dependency.getMapper().getVariable(), fieldModel.getPropertyKey())
              .nextControlFlow("else if (!properties.containsKey(\"%s\"))", fieldModel.getPropertyKey())
              .emitStatement("throw new IllegalStateException(\"The property \\\"%s\\\" is not set.\")", fieldModel.getPropertyKey())
              .nextControlFlow("else")
              .emitStatement("object.%s = null", fieldModel.getName())
              .endControlFlow();
          break;
        case collection:
          dependency = fieldModel.getDependency();
          if (dependency == null) {
            writer
                .emitStatement("object.%s = (%s) properties.get(\"%s\")", fieldModel.getName(), fieldModel.getType().getName(), fieldModel.getPropertyKey())
                .beginControlFlow("if (object.%s == null && !properties.containsKey(\"%s\"))", fieldModel.getName(), fieldModel.getPropertyKey())
                .emitStatement("throw new IllegalStateException(\"The property \\\"%s\\\" is not set.\")", fieldModel.getPropertyKey())
                .endControlFlow();
          } else {
            writer
                .beginControlFlow("if (properties.get(\"%s\") != null)", fieldModel.getPropertyKey()) // FIXME two calls to properties.get
                .emitStatement("final Collection<Map<String, Object>> aux = (Collection<Map<String, Object>>) properties.get(\"%s\")", fieldModel.getPropertyKey())
                .emitStatement("final List<%s> list = new ArrayList<>(aux.size())", dependency.getName())
                .beginControlFlow("for (Map<String, Object> auxProperties : aux)")
                .emitStatement("list.add(%s.toObject(auxProperties))", dependency.getMapper().getVariable())
                .endControlFlow()
                .emitStatement("object.%s = list", fieldModel.getName())
                .nextControlFlow("else if (!properties.containsKey(\"%s\"))", fieldModel.getPropertyKey())
                .emitStatement("throw new IllegalStateException(\"The property \\\"%s\\\" is not set.\")", fieldModel.getPropertyKey())
                .nextControlFlow("else")
                .emitStatement("object.%s = null", fieldModel.getName())
                .endControlFlow();
          }
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
      final EntityModel dependency;
      switch (fieldModel.getKind(helper)) {
        case primitive:
        case simpleObject:
          writer
              .emitStatement("properties.put(\"%s\", object.%s)", fieldModel.getPropertyKey(), fieldModel.getName());
          break;
        case object:
          dependency = fieldModel.getDependency();
          writer
              .emitStatement("properties.put(\"%s\", object.%s == null ? null : %s.toProperties(object.%s))", fieldModel.getPropertyKey(), fieldModel.getName(), dependency.getMapper().getVariable(), fieldModel.getName());
          break;
        case collection:
          dependency = fieldModel.getDependency();
          if (dependency == null) {
            writer
                .emitStatement("properties.put(\"%s\", object.%s)", fieldModel.getPropertyKey(), fieldModel.getName());
          } else {
            final String propertiesAuxName = "properties" + dependency.getName();
            writer
                .emitStatement("final List<Map<String, Object>> %s", propertiesAuxName)
                .beginControlFlow("if (object.%s == null)", fieldModel.getName())
                .emitStatement("%s = null", propertiesAuxName)
                .nextControlFlow("else")
                .emitStatement("%s = new ArrayList<>(object.%s.size())", propertiesAuxName, fieldModel.getName())
                .beginControlFlow("for (%s %s : object.%s)", dependency.getName(), dependency.getVariable(), fieldModel.getName())
                .emitStatement("%s.add(%s.toProperties(%s))", propertiesAuxName, dependency.getMapper().getVariable(), dependency.getVariable())
                .endControlFlow()
                .endControlFlow()
                .emitStatement("properties.put(\"%s\", %s)", fieldModel.getPropertyKey(), propertiesAuxName);
          }
          break;
        default:
      }
    }
    writer
        .emitStatement("return properties")
        .endMethod();
  }
}
