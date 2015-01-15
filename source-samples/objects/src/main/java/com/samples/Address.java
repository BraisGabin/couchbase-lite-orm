package com.samples;

import com.petterfactory.couchbaseliteorm.Example;
import com.petterfactory.couchbaseliteorm.ExampleField;

/**
 * Created by brais on 13/1/15.
 */
@Example
public class Address {

  @ExampleField("street")
  String street;

  @ExampleField("number")
  String number;
}
