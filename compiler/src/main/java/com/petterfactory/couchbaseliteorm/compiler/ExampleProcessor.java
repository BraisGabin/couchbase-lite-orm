package com.petterfactory.couchbaseliteorm.compiler;

import com.google.auto.service.AutoService;
import com.petterfactory.couchbaseliteorm.Example;
import com.petterfactory.couchbaseliteorm.Mapper;
import com.squareup.javawriter.JavaWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Processor;
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
@AutoService(Processor.class)
public class ExampleProcessor extends AbstractProcessor {

  private final static Set<Modifier> EMPTY_SET = Collections.emptySet();

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latestSupported();
  }

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    return Collections.singleton(Example.class.getName());
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    List<ExampleModel> models = parseExampleAnnotations(roundEnv);
    try {
      for (ExampleModel model : models) {
        emitExampleCode(model);
      }
      if (!models.isEmpty()) {
        emitInternalParser(models);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return false;
  }

  private List<ExampleModel> parseExampleAnnotations(RoundEnvironment roundEnv) {
    Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Example.class);
    List<ExampleModel> models = new ArrayList<>(elements.size());
    for (Element element : elements) {
      models.add(new ExampleModel((TypeElement) element));
    }
    return models;
  }

  private void emitExampleCode(ExampleModel model) throws IOException {
    final Filer filer = this.processingEnv.getFiler();

    final String className = model.getClassName();
    final String packageName = model.getPackageName();
    final String mapperClassName = model.getMapperClassName();

    JavaFileObject sourceFile = filer.createSourceFile(packageName + "." + mapperClassName, model.getElement());

    JavaWriter writer = new JavaWriter(sourceFile.openWriter());

    writer
        .emitPackage(packageName)
        .emitImports(
            Map.class,
            HashMap.class,
            Mapper.class
        )
        .beginType(mapperClassName, "class", EnumSet.of(PUBLIC), null, "Mapper<" + className + ">")
        .emitAnnotation(Override.class)
        .beginMethod(className, "toObject", EnumSet.of(PUBLIC), "Map<String, Object>", "properties")
        .emitStatement("final %s object = new %s()", className, className);
    for (ExampleFieldModel fieldModel : model.getFields()) {
      writer.emitStatement("object.%s = (%s) properties.get(\"%s\")", fieldModel.getFieldName(), fieldModel.getTypeSimpleName(), fieldModel.getMapProperty());
    }
    writer.emitStatement("return object")
        .endMethod()
        .emitAnnotation(Override.class)
        .beginMethod("Map<String, Object>", "toProperties", EnumSet.of(PUBLIC), className, "object")
        .emitStatement("final Map<String, Object> properties = new HashMap<>()")
        .emitStatement("properties.put(\"type\", \"%s\")", model.getAnnotationValue());
    for (ExampleFieldModel fieldModel : model.getFields()) {
      writer.emitStatement("properties.put(\"%s\", object.%s)", fieldModel.getMapProperty(), fieldModel.getFieldName());
    }
    writer.emitStatement("return properties")
        .endMethod()
        .endType()
        .close();
  }

  private void emitInternalParser(List<ExampleModel> models) throws IOException {
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
    for (ExampleModel model : models) {
      writer
          .emitStatement("registerType(\"%s\", %s.class, new %s$$Mapper())", model.getAnnotationValue(), model.getClassName(), model.getClassName());
    }
    writer
        .endConstructor()
        .endType()
        .close();
  }

  private static Element[] getArrayElements(List<ExampleModel> models) {
    final int size = models.size();
    final Element[] elements = new Element[size];
    for (int i = 0; i < size; i++) {
      elements[i] = models.get(i).getElement();
    }
    return elements;
  }

  private static List<String> getArrayClasses(List<ExampleModel> models) {
    final int size = models.size();
    final List<String> classes = new ArrayList<>(size);
    for (ExampleModel model : models) {
      final String classQualifiedName = model.getClassQualifiedName();
      classes.add(classQualifiedName);
      classes.add(classQualifiedName + "$$Mapper");
    }
    return classes;
  }
}
