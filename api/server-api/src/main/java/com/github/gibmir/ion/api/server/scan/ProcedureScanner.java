package com.github.gibmir.ion.api.server.scan;

import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure0;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure1;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure2;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure3;
import com.github.gibmir.ion.api.dto.method.signature.Signature;

public class ProcedureScanner {
  private ProcedureScanner() {
  }

  public static <R> Signature resolveSignature0(Class<? extends JsonRemoteProcedure0<R>> procedureClass) {
    return new Signature();
  }

  public static <T, R> Signature resolveSignature1(Class<? extends JsonRemoteProcedure1<T, R>> procedureClass) {
    try {
      return new Signature(procedureClass.getMethod("call", Object.class).getParameterTypes());
    } catch (NoSuchMethodException e) {
      throw new IllegalArgumentException("Service wasn't present in [" + procedureClass.getName() + "]", e);
    }
  }

  public static <T1, T2, R> Signature resolveSignature2(Class<? extends JsonRemoteProcedure2<T1, T2, R>> procedureClass) {
    try {
      return new Signature(procedureClass.getMethod("call", Object.class, Object.class).getParameterTypes());
    } catch (NoSuchMethodException e) {
      throw new IllegalArgumentException("Service wasn't present in [" + procedureClass.getName() + "]", e);
    }
  }

  public static <T1, T2, T3, R> Signature resolveSignature3(Class<? extends JsonRemoteProcedure3<T1, T2, T3, R>> procedureClass) {
    try {
      return new Signature(procedureClass.getMethod("call", Object.class, Object.class, Object.class)
        .getParameterTypes());
    } catch (NoSuchMethodException e) {
      throw new IllegalArgumentException("Service wasn't present in [" + procedureClass.getName() + "]", e);
    }
  }
}
