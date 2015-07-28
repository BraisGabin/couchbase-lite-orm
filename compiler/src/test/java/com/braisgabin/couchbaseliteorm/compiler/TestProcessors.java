package com.braisgabin.couchbaseliteorm.compiler;

import java.util.Collections;

/**
 * Created by brais on 7/1/15.
 */
public class TestProcessors {

  static Iterable<? extends javax.annotation.processing.Processor> processors() {
    return Collections.singletonList(
        new Processor()
    );
  }
}
