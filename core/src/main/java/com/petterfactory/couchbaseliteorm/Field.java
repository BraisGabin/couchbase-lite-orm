package com.petterfactory.couchbaseliteorm;

import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

/**
 * Created by brais on 6/1/15.
 */
@Target(FIELD)
public @interface Field {
  String value();
}
