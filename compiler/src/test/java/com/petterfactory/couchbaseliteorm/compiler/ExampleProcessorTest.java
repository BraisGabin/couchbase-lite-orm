package com.petterfactory.couchbaseliteorm.compiler;

import com.google.testing.compile.JavaFileObjects;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.tools.JavaFileObject;

import static com.google.common.truth.Truth.ASSERT;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;
import static com.petterfactory.couchbaseliteorm.compiler.TestProcessors.exampleProcessors;

/**
 * Created by brais on 6/1/15.
 */
public class ExampleProcessorTest {
  private String compilerPath;

  @Before
  public void init() {
    // FIXME this is a workaround.
    this.compilerPath = System.getProperty("compilerProjectPath");
    if (this.compilerPath == null) {
      this.compilerPath = "./compiler";
    }
  }

  @Test
  public void simpleCompile() throws IOException {
    final JavaFileObject inputFile = JavaFileObjects.forSourceString("com.samples.Person",
        new String(Files.readAllBytes(Paths.get(compilerPath + "/../compiler-source-samples/src/main/java/com/samples/Person.java")), "UTF-8")
    );
    final JavaFileObject expectedFile = JavaFileObjects.forSourceString("com.samples.Person$$Example",
        new String(Files.readAllBytes(Paths.get(compilerPath + "/../compiler-source-samples/src/main/java/com/samples/Person$$Example.java")), "UTF-8")
    );

    ASSERT.about(javaSource())
        .that(inputFile)
        .processedWith(exampleProcessors())
        .compilesWithoutError()
        .and()
        .generatesSources(expectedFile);
  }
}
