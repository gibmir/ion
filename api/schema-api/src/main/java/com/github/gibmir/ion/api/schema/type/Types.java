package com.github.gibmir.ion.api.schema.type;

import java.util.List;
import java.util.Map;

public enum Types {
  BOOLEAN("boolean", Boolean.class, false),
  STRING("string", String.class, false),
  NUMBER("number", Double.class, false),
  CUSTOM("custom", null, true),
  LIST("list", List.class, true),
  MAP("map", Map.class, true),
  ;
  private final String typeName;
  private final Class<?> type;
  private final boolean parametrized;

  Types(String typeName, Class<?> type, boolean parametrized) {
    this.typeName = typeName;
    this.type = type;
    this.parametrized = parametrized;
  }

  public static Types from(String typeName) {
    if (typeName.equals(STRING.typeName)) {
      return STRING;
    } else if (typeName.equals(NUMBER.typeName)) {
      return NUMBER;
    } else if (typeName.equals(BOOLEAN.typeName)) {
      return BOOLEAN;
    } else if (typeName.equals(LIST.typeName)) {
      return LIST;
    } else if (typeName.equals(MAP.typeName)) {
      return MAP;
    } else {
      return CUSTOM;
    }
  }

  public Class<?> resolve() {
    return type;
  }

  public boolean isParametrized() {
    return parametrized;
  }
}
