package com.github.gibmir.ion.scanner.signature;

import java.lang.invoke.MethodType;
import java.lang.reflect.Type;
import java.util.Arrays;

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

  @Override
  public String toString() {
    return "ParameterizedJsonRemoteProcedureSignature{" +
      "procedureName='" + procedureName + '\'' +
      ", parameterNames=" + Arrays.toString(parameterNames) +
      ", genericTypes=" + Arrays.toString(genericTypes) +
      ", returnType=" + returnType +
      ", methodType=" + methodType +
      ", parametersCount=" + parametersCount +
      '}';
  }
}
