package com.petterfactory.couchbaseliteorm;

import java.util.Map;

public interface Mapper<T> {

  T toObject(Map<String, Object> properties);

  Map<String, Object> toProperties(T object);
}
