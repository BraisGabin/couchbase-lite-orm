package com.petterfactory.couchbaseliteorm.compiler;

import com.petterfactory.couchbaseliteorm.ExampleField;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.lang.model.element.VariableElement;

/**
 * Created by brais on 7/1/15.
 */
public class ExampleFieldModel {
  private final static Pattern pattern = Pattern.compile("^(.*)<(.*)>$");

  private final VariableElement element;

  public ExampleFieldModel(VariableElement element) {
    this.element = element;
  }

  public VariableElement getElement() {
    return element;
  }

  public String getFieldName() {
    return element.getSimpleName().toString();
  }

  public String getTypeSimpleName() {
    final String fullQualifiedName = element.asType().toString();
    return getTypeSimpleName(fullQualifiedName);
  }

  private static String getTypeSimpleName(String fullQualifiedName) {
    Matcher matcher = pattern.matcher(fullQualifiedName);
    final String simpleName;
    if (matcher.matches()) {
      final StringBuilder builder = new StringBuilder();
      builder
          .append(removePackageName(matcher.group(1)))
          .append("<");
      final String[] split = matcher.group(2).split(","); // FIXME problems with A<B<C,D>>
      boolean first = true;
      for (String s : split) {
        if (first) {
          first = false;
        } else {
          builder.append(", ");
        }
        builder.append(getTypeSimpleName(s.trim()));
      }
      builder.append(">");
      simpleName = builder.toString();
    } else {
      simpleName = removePackageName(fullQualifiedName);
    }
    return simpleName;
  }

  private static String removePackageName(String fullQualifiedName) {
    return fullQualifiedName.substring(fullQualifiedName.lastIndexOf(".") + 1);
  }

  public String getTypeQualifiedName() {
    return element.asType().toString();
  }

  public List<String> getTypeQualifiedNames() {
    final String fullQualifiedName = element.asType().toString();
    return getTypeQualifiedNames(fullQualifiedName);
  }

  private static List<String> getTypeQualifiedNames(String fullQualifiedName) {
    Matcher matcher = pattern.matcher(fullQualifiedName);
    final List<String> names;
    if (matcher.matches()) {
      names = new ArrayList<>();
      names.add(matcher.group(1));
      final String[] split = matcher.group(2).split(","); // FIXME problems with A<B<C,D>>
      for (String s : split) {
        names.addAll(getTypeQualifiedNames(s.trim()));
      }
    } else {
      names = Arrays.asList(fullQualifiedName);
    }
    return names;
  }

  public String getMapProperty() {
    return element.getAnnotation(ExampleField.class).value();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ExampleFieldModel that = (ExampleFieldModel) o;

    if (!element.equals(that.element)) return false;

    return true;
  }

  @Override
  public int hashCode() {
    return element.hashCode();
  }
}
