package com.github.gibmir.ion.api.schema.type;

import java.lang.reflect.Type;

public enum Types {
  BOOLEAN("boolean", Boolean.class),
  STRING("string", String.class),
  NUMBER("number", Double.class),
  CUSTOM("custom", null),
  //todo list, map
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
    } else {
      return CUSTOM;
    }
  }

  public Type resolve() {
    return type;
  }
}
