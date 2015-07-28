package com.braisgabin.couchbaseliteorm.compiler;

import com.braisgabin.couchbaseliteorm.Entity;
import com.google.auto.service.AutoService;
import com.squareup.javawriter.JavaWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

/**
 * Created by brais on 6/1/15.
 */
@AutoService(javax.annotation.processing.Processor.class)
public class Processor extends AbstractProcessor {

  private final static Set<Modifier> EMPTY_SET = Collections.emptySet();
  private final List<EntityModel> models = new ArrayList<>();
  private Helper helper;
  private Filer filer;

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latestSupported();
  }

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    return Collections.singleton(Entity.class.getName());
  }

  @Override
  public synchronized void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);

    this.helper = new Helper(processingEnv.getElementUtils(), processingEnv.getTypeUtils());
    this.filer = processingEnv.getFiler();
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    models.addAll(parseAnnotations(roundEnv));
    try {
      for (EntityModel model : models) {
        model.fillFieldsList(helper, models);
      }
      if (roundEnv.processingOver()) {
        for (EntityModel model : models) {
          new MapperEmitter(helper, filer, model).emit();
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

    final String classPackage = "com.braisgabin.couchbaseliteorm";
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
      for (EntityModel dependency : model.getDependencies()) {
        final MapperModel dependencyMapper = dependency.getMapper();
        final String dependencyMapperVariable = dependencyMapper.getVariable();
        writer
            .emitStatement("%s.%s = %s", mapperVariable, dependencyMapperVariable, dependencyMapperVariable);
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
}
