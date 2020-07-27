package com.github.gibmir.ion.api.core.procedure.signature;

import java.lang.reflect.Type;

public class ParameterizedSignature implements Signature {
  private final Type[] genericTypes;

  public ParameterizedSignature(Type... genericTypes) {
    this.genericTypes = genericTypes;
  }

  @Override
  public Type[] getGenericTypes() {
    return genericTypes;
  }
}
