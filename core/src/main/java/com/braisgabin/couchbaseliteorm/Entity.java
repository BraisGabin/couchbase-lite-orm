package com.braisgabin.couchbaseliteorm;

import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

/**
 * Created by brais on 6/1/15.
 */
@Target(TYPE)
public @interface Entity {

  public static final String DEFAULT_VALE = "☕️💩";

  String value() default DEFAULT_VALE;
}
