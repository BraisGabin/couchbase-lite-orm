package com.braisgabin.couchbaseliteorm.compiler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.lang.model.type.TypeMirror;

public class TypeModel {
  private final static Pattern pattern = Pattern.compile("^(.*)<(.*)>$");

  private final TypeMirror type;

  TypeModel(TypeMirror type) {
    this.type = type;
  }

  public String getName() {
    final String fullQualifiedName = type.toString();
    return getTypeSimpleName(fullQualifiedName);
  }

  public List<String> getFullQualifiedNames() {
    final String fullQualifiedName = type.toString();
    return getTypeQualifiedNames(fullQualifiedName);
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
}
