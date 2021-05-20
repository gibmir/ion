package com.github.gibmir.ion.scanner.signature;

import java.lang.invoke.MethodType;
import java.lang.reflect.Type;

public interface JsonRemoteProcedureSignature {
  String getProcedureName();

  String[] getParameterNames();

  Type[] getGenericTypes();

  Type getReturnType();

  MethodType getMethodType();

  int getParametersCount();
}
