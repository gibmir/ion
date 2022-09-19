package com.github.gibmir.ion.api.schema.type;

import java.util.List;
import java.util.Map;

public enum Types {
  BOOLEAN("boolean", Boolean.class, false),
  STRING("string", String.class, false),
  NUMBER("number", Double.class, false),
  INT("int", Integer.class, false),
  CUSTOM("custom", null, true),
  LIST("list", List.class, true),
  MAP("map", Map.class, true);
  private final String typeName;
  private final Class<?> type;
  private final boolean parametrized;

  Types(final String typeName, final Class<?> type, final boolean parametrized) {
    this.typeName = typeName;
    this.type = type;
    this.parametrized = parametrized;
  }

  /**
   * Resolves type by specified type name.
   *
   * @param typeName type name
   * @return resolved type
   */
  public static Types from(final String typeName) {
    if (typeName.equals(STRING.typeName)) {
      return STRING;
    } else if (typeName.equals(NUMBER.typeName)) {
      return NUMBER;
    } else if (typeName.equals(INT.typeName)) {
      return INT;
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

  /**
   * @return resolved class
   * @implNote returns null for custom type.
   */
  public Class<?> resolve() {
    return type;
  }

  /**
   * @return true if type parametrized
   */
  public boolean isParametrized() {
    return parametrized;
  }
}
