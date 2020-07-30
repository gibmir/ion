package com.github.gibmir.ion.api.core.procedure.signature;

import java.lang.reflect.Type;

public class ParameterizedJsonRemoteProcedureSignature implements JsonRemoteProcedureSignature {
  private final String procedureName;
  private final String[] parameterNames;
  private final Type[] genericTypes;
  private final Type returnType;

  public ParameterizedJsonRemoteProcedureSignature(String procedureName, String[] parameterNames,
                                                   Type[] genericTypes, Type returnType) {
    this.procedureName = procedureName;
    this.parameterNames = parameterNames;
    this.genericTypes = genericTypes;
    this.returnType = returnType;
  }

  @Override
  public String getProcedureName() {
    return procedureName;
  }

  @Override
  public String[] getParameterNames() {
    return parameterNames;
  }

  @Override
  public Type[] getGenericTypes() {
    return genericTypes;
  }

  @Override
  public Type getReturnType() {
    return returnType;
  }
}
