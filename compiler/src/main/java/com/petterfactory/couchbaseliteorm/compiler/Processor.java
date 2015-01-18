package com.petterfactory.couchbaseliteorm.compiler;

import com.google.auto.service.AutoService;
import com.petterfactory.couchbaseliteorm.Entity;
import com.petterfactory.couchbaseliteorm.Mapper;
import com.squareup.javawriter.JavaWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

import static javax.lang.model.element.Modifier.PUBLIC;

/**
 * Created by brais on 6/1/15.
 */
@AutoService(javax.annotation.processing.Processor.class)
public class Processor extends AbstractProcessor {

  private final static Set<Modifier> EMPTY_SET = Collections.emptySet();
  private final List<EntityModel> models = new ArrayList<>();

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latestSupported();
  }

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    return Collections.singleton(Entity.class.getName());
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    models.addAll(parseAnnotations(roundEnv));
    try {
      for (EntityModel model : models) {
        model.fillFieldsList(models);
      }
      if (roundEnv.processingOver()) {
        for (EntityModel model : models) {
          emitMapper(model);
        }
        emitCouchbaseLiteOrmInternal(models);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return false;
  }

  private List<EntityModel> parseAnnotations(RoundEnvironment roundEnv) {
    Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Entity.class);
    List<EntityModel> models = new ArrayList<>(elements.size());
    for (Element element : elements) {
      models.add(new EntityModel((TypeElement) element));
    }
    return models;
  }

  private void emitMapper(EntityModel model) throws IOException {
    final Filer filer = this.processingEnv.getFiler();

    final String entityName = model.getName();
    final MapperModel mapper = model.getMapper();
    final String packageName = mapper.getPackage();
    final String mapperClassName = mapper.getName();

    JavaFileObject sourceFile = filer.createSourceFile(mapper.getFullQualifiedName(), model.getElement());

    JavaWriter writer = new JavaWriter(sourceFile.openWriter());

    Set<String> imports = new HashSet<>(Arrays.asList(
        Map.class.getCanonicalName(),
        HashMap.class.getCanonicalName(),
        Mapper.class.getCanonicalName()
    ));
    for (FieldModel fieldModel : model.getFields()) {
      imports.addAll(fieldModel.getType().getFullQualifiedNames());
    }
    removeJavaLangImports(imports);
    removeImportsFromPackage(imports, packageName);

    writer
        .emitPackage(packageName)
        .emitImports(imports)
        .beginType(mapperClassName, "class", EnumSet.of(PUBLIC), null, "Mapper<" + entityName + ">");
    for (FieldModel fieldModel : model.getFields()) {
      final MapperModel mapperDependency = fieldModel.getDependencyMapperModel();
      if (mapperDependency != null) {
        writer
            .emitField(mapperDependency.getName(), mapperDependency.getVariable(), EnumSet.of(PUBLIC));
      }
    }
    writer
        .emitAnnotation(Override.class)
        .beginMethod(entityName, "toObject", EnumSet.of(PUBLIC), "Map<String, Object>", "properties")
        .emitStatement("final %s object = new %s()", entityName, entityName);
    for (FieldModel fieldModel : model.getFields()) {
      final MapperModel mapperDependency = fieldModel.getDependencyMapperModel();
      if (mapperDependency == null) {
        writer
            .emitStatement("object.%s = (%s) properties.get(\"%s\")", fieldModel.getName(), fieldModel.getType().getName(), fieldModel.getPropertyKey());
      } else {
        writer
            .beginControlFlow("if (properties.get(\"%s\") != null)", fieldModel.getPropertyKey())
            .emitStatement("object.%s = %s.toObject((Map<String, Object>) properties.get(\"%s\"))", fieldModel.getName(), mapperDependency.getVariable(), fieldModel.getPropertyKey())
            .endControlFlow();
      }
    }
    writer
        .emitStatement("return object")
        .endMethod()
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
        .endMethod()
        .endType()
        .close();
  }

  private void emitCouchbaseLiteOrmInternal(List<EntityModel> models) throws IOException {
    Collections.sort(models, new Comparator<EntityModel>() {
      @Override
      public int compare(EntityModel o1, EntityModel o2) {
        int compare;
        compare = o1.getName().compareTo(o2.getName());
        if (compare == 0) {
          compare = o1.getFullQualifiedName().compareTo(o2.getFullQualifiedName());
        }
        return compare;
      }
    });

    final Filer filer = this.processingEnv.getFiler();

    final String classPackage = "com.petterfactory.couchbaseliteorm";
    final String className = "CouchbaseLiteOrmInternal";

    JavaFileObject sourceFile = filer.createSourceFile(classPackage + "." + className, getArrayElements(models));

    JavaWriter writer = new JavaWriter(sourceFile.openWriter());

    List<String> imports = getArrayClasses(models);

    writer
        .emitPackage(classPackage)
        .emitImports(imports)
        .beginType(className, "class", EMPTY_SET, "CouchbaseLiteOrm")
        .beginConstructor(EMPTY_SET);
    for (EntityModel model : models) {
      final MapperModel mapperModel = model.getMapper();
      final String mapperClass = mapperModel.getName();
      final String mapperVariable = mapperModel.getVariable();
      writer
          .emitStatement("final %s %s = new %s()", mapperClass, mapperVariable, mapperClass);
    }
    for (EntityModel model : models) {
      final MapperModel mapperModel = model.getMapper();
      final String mapperVariable = mapperModel.getVariable();
      for (FieldModel fieldModel : model.getFields()) {
        final MapperModel dependencyMapper = fieldModel.getDependencyMapperModel();
        if (dependencyMapper != null) {
          final String dependencyMapperVariable = dependencyMapper.getVariable();
          writer
              .emitStatement("%s.%s = %s", mapperVariable, dependencyMapperVariable, dependencyMapperVariable);
        }
      }
    }
    for (EntityModel model : models) {
      if (model.hasAnnotationValue()) {
        final String mapperVariable = model.getMapper().getVariable();
        writer
            .emitStatement("registerType(\"%s\", %s.class, %s)", model.getAnnotationValue(), model.getName(), mapperVariable);
      }
    }
    writer
        .endConstructor()
        .endType()
        .close();
  }

  private static Element[] getArrayElements(List<EntityModel> models) {
    final int size = models.size();
    final Element[] elements = new Element[size];
    for (int i = 0; i < size; i++) {
      elements[i] = models.get(i).getElement();
    }
    return elements;
  }

  private static List<String> getArrayClasses(List<EntityModel> models) {
    final int size = models.size();
    final List<String> classes = new ArrayList<>(size);
    for (EntityModel model : models) {
      if (model.hasAnnotationValue()) {
        classes.add(model.getFullQualifiedName());
      }
      classes.add(model.getMapper().getFullQualifiedName());
    }
    return classes;
  }

  private static void removeJavaLangImports(Collection<String> imports) {
    final Iterator<String> iterator = imports.iterator();
    while (iterator.hasNext()) {
      if (iterator.next().startsWith("java.lang.")) {
        iterator.remove();
      }
    }
    imports.removeAll(Arrays.asList("boolean", "byte", "short", "int", "long", "float", "double"));
  }

  private static void removeImportsFromPackage(Set<String> imports, String packageName) {
    final Pattern pattern = Pattern.compile("^" + Pattern.quote(packageName) + "\\.[^\\.]*$");
    final Iterator<String> iterator = imports.iterator();
    while (iterator.hasNext()) {
      final Matcher matcher = pattern.matcher(iterator.next());
      if (matcher.matches()) {
        iterator.remove();
      }
    }
  }
}
