package com.petterfactory.couchbaseliteorm.compiler;

import com.couchbase.lite.Document;
import com.google.auto.service.AutoService;
import com.petterfactory.couchbaseliteorm.Example;
import com.squareup.javawriter.JavaWriter;

import java.io.IOException;
import java.util.ArrayList;
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

/**
 * Created by brais on 6/1/15.
 */
@AutoService(Processor.class)
public class ExampleProcessor extends AbstractProcessor {

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
    for (ExampleModel model : models) {
      try {
        emitExampleCode(model);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
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
    final String className = model.getClassName() + "$$Example";

    JavaFileObject sourceFile = filer.createSourceFile(classPackage + "." + className, model.getElement());

    JavaWriter writer = new JavaWriter(sourceFile.openWriter());

    writer
        .emitPackage(classPackage)
        .emitImports(
            Document.class,
            Map.class
        )
        .beginType(className, "class", EnumSet.of(Modifier.PUBLIC, Modifier.ABSTRACT))
        .beginMethod(model.getClassName(), "get", EnumSet.of(Modifier.PUBLIC, Modifier.STATIC), "Document", "document")
        .emitStatement("%s object = new %s()", model.getClassName(), model.getClassName())
        .emitStatement("Map<String, Object> properties = document.getProperties()");
    for (ExampleFieldModel fieldModel : model.getFields()) {
      writer.emitStatement("object.%s = (%s) properties.get(\"%s\")", fieldModel.getFieldName(), fieldModel.getTypeSimpleName(), fieldModel.getMapProperty());
    }
    writer.emitStatement("return object")
        .endMethod()
        .endType()
        .close();
  }
}
