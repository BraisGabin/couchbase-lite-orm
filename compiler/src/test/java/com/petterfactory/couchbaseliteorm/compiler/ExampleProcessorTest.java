package com.petterfactory.couchbaseliteorm.compiler;

import com.google.testing.compile.JavaFileObjects;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import javax.tools.JavaFileObject;

import static com.google.common.truth.Truth.ASSERT;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;
import static com.google.testing.compile.JavaSourcesSubjectFactory.javaSources;
import static com.petterfactory.couchbaseliteorm.compiler.TestProcessors.exampleProcessors;

/**
 * Created by brais on 6/1/15.
 */
public class ExampleProcessorTest {
  private String compilerPath;

  private static JavaFileObject getJavaFileObject(String compilerPath, String sampleName, String fullyQualifiedName) throws IOException {
    return JavaFileObjects.forSourceString(fullyQualifiedName,
        new String(Files.readAllBytes(qualifiedNameToPath(compilerPath, sampleName, fullyQualifiedName)), "UTF-8")
    );
  }

  private static Path qualifiedNameToPath(String compilerPath, String sampleName, String fullyQualifiedName) {
    return Paths.get(compilerPath
        + "/../source-samples/"
        + sampleName
        + "/src/main/java/"
        + fullyQualifiedName.replaceAll("\\.", "/")
        + ".java");
  }

  @Before
  public void init() {
    // FIXME this is a workaround.
    this.compilerPath = System.getProperty("compilerProjectPath");
    if (this.compilerPath == null) {
      this.compilerPath = "./compiler";
    }
  }

//  @Test
//  public void simpleCompile() throws IOException {
//    final String sampleName = "simple";
//    final JavaFileObject inputFile = getJavaFileObject(compilerPath, sampleName, "com.samples.Person");
//    final JavaFileObject expectedFile1 = getJavaFileObject(compilerPath, sampleName, "com.samples.Person$$Mapper");
//    final JavaFileObject expectedFile2 = getJavaFileObject(compilerPath, sampleName, "com.petterfactory.couchbaseliteorm.CouchbaseLiteOrmInternal");
//
//    ASSERT.about(javaSource())
//        .that(inputFile)
//        .processedWith(exampleProcessors())
//        .compilesWithoutError()
//        .and()
//        .generatesSources(expectedFile1, expectedFile2);
//  }

  @Test
  public void listsCompile() throws IOException {
    final String sampleName = "lists";
    final JavaFileObject inputFile = getJavaFileObject(compilerPath, sampleName, "com.samples.Person");
    final JavaFileObject expectedFile1 = getJavaFileObject(compilerPath, sampleName, "com.samples.Person$$Mapper");
    final JavaFileObject expectedFile2 = getJavaFileObject(compilerPath, sampleName, "com.petterfactory.couchbaseliteorm.CouchbaseLiteOrmInternal");

    ASSERT.about(javaSource())
        .that(inputFile)
        .processedWith(exampleProcessors())
        .compilesWithoutError()
        .and()
        .generatesSources(expectedFile1, expectedFile2);
  }

  @Test
  public void objectsCompile() throws IOException {
    final String sampleName = "objects";
    final JavaFileObject inputFile1 = getJavaFileObject(compilerPath, sampleName, "com.samples.Person");
    final JavaFileObject inputFile2 = getJavaFileObject(compilerPath, sampleName, "com.samples.Address");
    final JavaFileObject expectedFile1 = getJavaFileObject(compilerPath, sampleName, "com.samples.Person$$Mapper");
    final JavaFileObject expectedFile2 = getJavaFileObject(compilerPath, sampleName, "com.samples.Address$$Mapper");
    final JavaFileObject expectedFile3 = getJavaFileObject(compilerPath, sampleName, "com.petterfactory.couchbaseliteorm.CouchbaseLiteOrmInternal");

    ASSERT.about(javaSources())
        .that(Arrays.asList(inputFile1, inputFile2))
        .processedWith(exampleProcessors())
        .compilesWithoutError()
        .and()
        .generatesSources(expectedFile1, expectedFile2, expectedFile3);
  }
}
