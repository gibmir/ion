package com.github.gibmir.ion.scanner.signature;

import java.lang.invoke.MethodType;
import java.lang.reflect.Type;

public interface JsonRemoteProcedureSignature {
  /**
   * @return procedure name
   */
  String getProcedureName();

  /**
   * @return parameter names
   */
  String[] getParameterNames();

  /**
   * @return generic types
   */
  Type[] getGenericTypes();

  /**
   * @return return type
   */
  Type getReturnType();

  /**
   * @return method type
   */
  MethodType getMethodType();

  /**
   * @return parameters count
   */
  int getParametersCount();
}
