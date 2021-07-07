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

  public ParameterizedJsonRemoteProcedureSignature(final String procedureName, final String[] parameterNames,
                                                   final Type[] genericTypes, final Type returnType,
                                                   final MethodType methodType, final int parametersCount) {
    this.procedureName = procedureName;
    this.parameterNames = parameterNames;
    this.genericTypes = genericTypes;
    this.returnType = returnType;
    this.methodType = methodType;
    this.parametersCount = parametersCount;
  }

  @Override
  public final String getProcedureName() {
    return procedureName;
  }

  @Override
  public final String[] getParameterNames() {
    return parameterNames;
  }

  @Override
  public final Type[] getGenericTypes() {
    return genericTypes;
  }

  @Override
  public final Type getReturnType() {
    return returnType;
  }

  @Override
  public final MethodType getMethodType() {
    return methodType;
  }

  @Override
  public final int getParametersCount() {
    return parametersCount;
  }

  @Override
  public final String toString() {
    return "ParameterizedJsonRemoteProcedureSignature{"
      + "procedureName='" + procedureName + '\''
      + ", parameterNames=" + Arrays.toString(parameterNames)
      + ", genericTypes=" + Arrays.toString(genericTypes)
      + ", returnType=" + returnType
      + ", methodType=" + methodType
      + ", parametersCount=" + parametersCount + '}';
  }
}
