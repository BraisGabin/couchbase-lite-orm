package com.petterfactory.couchbaseliteorm.compiler;

import com.google.common.base.Joiner;
import com.google.testing.compile.JavaFileObjects;

import org.junit.Test;

import javax.tools.JavaFileObject;

import static com.google.common.truth.Truth.ASSERT;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;
import static com.petterfactory.couchbaseliteorm.compiler.TestProcessors.exampleProcessors;

/**
 * Created by brais on 6/1/15.
 */
public class ExampleProcessorTest {

  final static String input = Joiner.on('\n').join(
      "package com.petterfactory.couchbaseliteorm.test;",
      "",
      "import com.petterfactory.couchbaseliteorm.compiler.Example;",
      "",
      "/**",
      " * Created by brais on 6/1/15.",
      " */",
      "@Example",
      "public class Person {",
      "  private final String name;",
      "  private final Integer age;",
      "",
      "  public Person(String name, Integer age) {",
      "    this.name = name;",
      "    this.age = age;",
      "  }",
      "",
      "  public String getName() {",
      "    return name;",
      "  }",
      "",
      "  public Integer getAge() {",
      "    return age;",
      "  }",
      "}"
  );

  final static String expected = Joiner.on('\n').join(
      "package com.petterfactory.couchbaseliteorm.test;",
      "import com.couchbase.lite.Document;",
      "import java.util.Map;",
      "public abstract class Person$$Example {",
      "  public static Person get(Document document) {",
      "    Map<String, Object> properties = document.getProperties();",
      "    String name = (String) properties.get(\"name\");",
      "    Integer age = (Integer) properties.get(\"age\");",
      "    return new Person(name, age);",
      "  }",
      "}"
  );

  final static JavaFileObject inputFile = JavaFileObjects.forSourceString("com.petterfactory.couchbaseliteorm.test.Person", input);

  final static JavaFileObject expectedFile = JavaFileObjects.forSourceString("com.petterfactory.couchbaseliteorm.test.Person$$Example", expected);

  @Test
  public void simpleCompile() {
    ASSERT.about(javaSource())
        .that(inputFile)
        .processedWith(exampleProcessors())
        .compilesWithoutError()
        .and()
        .generatesSources(expectedFile);
  }
}
