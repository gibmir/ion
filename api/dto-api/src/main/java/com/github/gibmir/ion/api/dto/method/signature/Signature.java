package com.github.gibmir.ion.api.dto.method.signature;
//todo use method.returnGenericTypes
public class Signature {
  private final Class<?>[] argumentTypes;

  public Signature(Class<?>... argumentTypes) {
    this.argumentTypes = argumentTypes;
  }

  public Class<?>[] getArgumentTypes() {
    return argumentTypes;
  }

  public Class<?> getHead() {
    return argumentTypes[0];
  }
}
