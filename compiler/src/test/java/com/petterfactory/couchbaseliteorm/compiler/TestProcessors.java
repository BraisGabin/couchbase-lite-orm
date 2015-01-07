package com.petterfactory.couchbaseliteorm.compiler;

import java.util.Collections;

import javax.annotation.processing.Processor;

/**
 * Created by brais on 7/1/15.
 */
public class TestProcessors {

  static Iterable<? extends Processor> exampleProcessors() {
    return Collections.singletonList(
        new ExampleProcessor()
    );
  }
}
