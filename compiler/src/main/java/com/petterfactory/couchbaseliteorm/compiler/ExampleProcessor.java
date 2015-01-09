package com.petterfactory.couchbaseliteorm.compiler;

import com.google.auto.service.AutoService;
import com.petterfactory.couchbaseliteorm.Example;
import com.squareup.javawriter.JavaWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
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

import static javax.lang.model.element.Modifier.ABSTRACT;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PROTECTED;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;

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

    final String classPackage = model.getPackageName();
    final String className = model.getMapperClassName();

    JavaFileObject sourceFile = filer.createSourceFile(classPackage + "." + className, model.getElement());

    JavaWriter writer = new JavaWriter(sourceFile.openWriter());

    writer
        .emitPackage(classPackage)
        .emitImports(
            Map.class
        )
        .beginType(className, "class", EnumSet.of(PUBLIC, ABSTRACT))
        .beginMethod(model.getClassName(), "get", EnumSet.of(PUBLIC, STATIC), "Map<String, Object>", "properties")
        .emitStatement("final %s object = new %s()", model.getClassName(), model.getClassName());
    for (ExampleFieldModel fieldModel : model.getFields()) {
      writer.emitStatement("object.%s = (%s) properties.get(\"%s\")", fieldModel.getFieldName(), fieldModel.getTypeSimpleName(), fieldModel.getMapProperty());
    }
    writer.emitStatement("return object")
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

    List<String> imports = new ArrayList<>(Arrays.asList(
        Map.class.getCanonicalName()
    ));
    imports.addAll(getArrayClasses(models));

    writer
        .emitPackage(classPackage)
        .emitImports(imports)
        .beginType(className, "class", EMPTY_SET, "CouchbaseLiteOrmInternalBase")
        .beginConstructor(EMPTY_SET)
        .emitStatement("super()");
    for (ExampleModel model : models) {
      writer
          .emitStatement("registerType(\"%s\", %s)", model.getAnnotationValue(), model.getClassName() + ".class");
    }
    writer.endConstructor()
        .emitAnnotation(Override.class)
        .beginMethod("<T> T", "get", EnumSet.of(PROTECTED), "Map<String, Object>", "properties", "Class<T>", "documentType")
        .emitStatement("final T object");
    boolean first = true;
    for (ExampleModel model : models) {
      if (first) {
        first = false;
        writer.beginControlFlow("if (documentType.equals(%s.class))", model.getClassName());
      } else {
        writer.nextControlFlow("else if (documentType.equals(%s.class))", model.getClassName());
      }
      writer.emitStatement("object = (T) get%s(properties)", model.getClassName());
    }
    writer
        .nextControlFlow("else")
        .emitStatement("throw new IllegalStateException(\"If you are getting this error please, report it.\")")
        .endControlFlow()
        .emitStatement("return object")
        .endMethod();
    for (ExampleModel model : models) {
      writer
          .beginMethod(model.getClassName(), "get" + model.getClassName(), EnumSet.of(PRIVATE, STATIC), "Map<String, Object>", "properties")
          .emitStatement("return %s$$Mapper.get(properties)", model.getClassName())
          .endMethod();
    }
    writer
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
