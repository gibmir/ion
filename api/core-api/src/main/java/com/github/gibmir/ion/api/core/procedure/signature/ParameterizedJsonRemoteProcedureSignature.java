package com.github.gibmir.ion.api.core.procedure.signature;

import java.lang.invoke.MethodType;
import java.lang.reflect.Type;

public class ParameterizedJsonRemoteProcedureSignature implements JsonRemoteProcedureSignature {
  private final String procedureName;
  private final String[] parameterNames;
  private final Type[] genericTypes;
  private final Type returnType;
  private final MethodType methodType;
  private final int parametersCount;

  public ParameterizedJsonRemoteProcedureSignature(String procedureName, String[] parameterNames,
                                                   Type[] genericTypes, Type returnType, MethodType methodType,
                                                   int parametersCount) {
    this.procedureName = procedureName;
    this.parameterNames = parameterNames;
    this.genericTypes = genericTypes;
    this.returnType = returnType;
    this.methodType = methodType;
    this.parametersCount = parametersCount;
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

  @Override
  public MethodType getMethodType() {
    return methodType;
  }

  @Override
  public int getParametersCount() {
    return parametersCount;
  }
}
