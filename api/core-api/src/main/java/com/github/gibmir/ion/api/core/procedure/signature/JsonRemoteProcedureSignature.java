package com.github.gibmir.ion.api.core.procedure.signature;

import java.lang.reflect.Type;

public interface JsonRemoteProcedureSignature {
  String getProcedureName();

  String[] getParameterNames();

  Type[] getGenericTypes();

  Type getReturnType();
}
