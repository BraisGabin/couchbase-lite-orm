package com.petterfactory.couchbaseliteorm;

import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

/**
 * Created by brais on 6/1/15.
 */
@Target(TYPE)
public @interface Example {

  String value();
}
