package com.github.gibmir.ion.api.schema.type;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public enum Types {
  BOOLEAN("boolean", Boolean.class),
  STRING("string", String.class),
  NUMBER("number", Double.class),
  CUSTOM("custom", null),
  LIST("list", List.class),
  MAP("map", Map.class),
  ;
  private final String typeName;
  private final Type type;

  Types(String typeName, Type type) {
    this.typeName = typeName;
    this.type = type;
  }

  public static Types from(String typeName) {
    if (typeName.equals(STRING.typeName)) {
      return STRING;
    } else if (typeName.equals(NUMBER.typeName)) {
      return NUMBER;
    } else if (typeName.equals(BOOLEAN.typeName)) {
      return BOOLEAN;
    } else {
      return CUSTOM;
    }
  }

  public static boolean isList(String typeName) {
    return LIST.typeName.equals(typeName.trim());
  }

  public static boolean isMap(String typeName) {
    return MAP.typeName.equals(typeName.trim());
  }

  public Type resolve() {
    return type;
  }
}
