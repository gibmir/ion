package com.github.gibmir.ion.api.server.scan;

import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure1;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure2;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure3;
import com.github.gibmir.ion.api.dto.method.signature.ParameterizedSignature;
import com.github.gibmir.ion.api.dto.method.signature.Signature;

import java.lang.reflect.Method;

public class ProcedureScanner {

  public static final Signature EMPTY_SIGNATURE = new ParameterizedSignature(null);

  private ProcedureScanner() {
  }

  public static Signature resolveSignature0() {
    return EMPTY_SIGNATURE;
  }

  public static <T, R> Signature resolveSignature1(Class<? extends JsonRemoteProcedure1<T, R>> procedureClass) {
    try {
      Method method = procedureClass.getMethod("call", Object.class);
      return new ParameterizedSignature(method.getGenericParameterTypes());
    } catch (NoSuchMethodException e) {
      throw new IllegalArgumentException("Service wasn't present in [" + procedureClass.getName() + "]", e);
    }
  }

  public static <T1, T2, R> Signature resolveSignature2(Class<? extends JsonRemoteProcedure2<T1, T2, R>> procedureClass) {
    try {
      Method method = procedureClass.getMethod("call", Object.class, Object.class);
      return new ParameterizedSignature(method.getGenericParameterTypes());
    } catch (NoSuchMethodException e) {
      throw new IllegalArgumentException("Service wasn't present in [" + procedureClass.getName() + "]", e);
    }
  }

  public static <T1, T2, T3, R> Signature resolveSignature3(Class<? extends JsonRemoteProcedure3<T1, T2, T3, R>> procedureClass) {
    try {
      Method method = procedureClass.getMethod("call", Object.class, Object.class, Object.class);
      return new ParameterizedSignature(method.getGenericParameterTypes());
    } catch (NoSuchMethodException e) {
      throw new IllegalArgumentException("Service wasn't present in [" + procedureClass.getName() + "]", e);
    }
  }
}
