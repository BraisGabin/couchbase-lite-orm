package com.braisgabin.couchbaseliteorm.compiler;

import com.squareup.javawriter.JavaWriter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.tools.JavaFileObject;

public abstract class Emitter {
  protected final JavaWriter writer;
  private final String packageName;

  public Emitter(Filer filer, String packageName, String className, Element element) throws IOException {
    final JavaFileObject sourceFile = filer.createSourceFile(packageName + "." + className, element);

    this.writer = new JavaWriter(sourceFile.openWriter());
    this.packageName = packageName;
  }

  public final void emit() throws IOException {
    emitPackageName();
    emitImports();
    emitClass();
    writer.close();
  }

  protected void emitPackageName() throws IOException {
    writer.emitPackage(packageName);
  }

  protected void emitImports() throws IOException {
    final Set<String> imports = getImports();
    removeJavaLangImports(imports);
    removeImportsFromPackage(imports);
    writer.emitImports(imports);
  }

  private void removeJavaLangImports(Collection<String> imports) {
    final Iterator<String> iterator = imports.iterator();
    while (iterator.hasNext()) {
      if (iterator.next().startsWith("java.lang.")) {
        iterator.remove();
      }
    }
    imports.removeAll(Arrays.asList("boolean", "byte", "short", "int", "long", "float", "double"));
  }

  private void removeImportsFromPackage(Set<String> imports) {
    final Pattern pattern = Pattern.compile("^" + Pattern.quote(packageName) + "\\.[^\\.]*$");
    final Iterator<String> iterator = imports.iterator();
    while (iterator.hasNext()) {
      final Matcher matcher = pattern.matcher(iterator.next());
      if (matcher.matches()) {
        iterator.remove();
      }
    }
  }

  protected abstract Set<String> getImports();

  protected abstract void emitClass() throws IOException;
}
