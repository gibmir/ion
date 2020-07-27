package com.github.gibmir.ion.api.core.procedure.scan;

import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure0;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure1;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure2;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure3;
import com.github.gibmir.ion.api.core.procedure.signature.ParameterizedSignature;
import com.github.gibmir.ion.api.core.procedure.signature.Signature;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ProcedureScanner {

  public static final Signature EMPTY_SIGNATURE = new ParameterizedSignature();
  /**
   * {@link JsonRemoteProcedure1#call(Object) T type}
   * {@link JsonRemoteProcedure2#call(Object, Object) T1 type}
   * {@link JsonRemoteProcedure3#call(Object, Object, Object) T1 type}
   */
  public static final int FIRST_PROCEDURE_PARAMETER = 0;
  /**
   * {@link JsonRemoteProcedure2#call(Object, Object) T2 type}
   * {@link JsonRemoteProcedure3#call(Object, Object, Object) T2 type}
   */
  public static final int SECOND_PROCEDURE_PARAMETER = 1;
  /**
   * {@link JsonRemoteProcedure3#call(Object, Object, Object) T3 type}
   */
  public static final int THIRD_PROCEDURE_PARAMETER = 2;

  private ProcedureScanner() {
  }

  public static Signature resolveSignature0() {
    return EMPTY_SIGNATURE;
  }

  public static <T, R> Signature resolveSignature1(Class<? extends JsonRemoteProcedure1<T, R>> procedureClass) {
    List<ParameterizedType> parametrizedInterfaces = findParametrizedInterfacesFor(procedureClass);
    for (ParameterizedType parametrizedInterface : parametrizedInterfaces) {
      if (parametrizedInterface.getRawType() == JsonRemoteProcedure1.class) {
        return new ParameterizedSignature(parametrizedInterface.getActualTypeArguments()[FIRST_PROCEDURE_PARAMETER]);
      }
    }
    throw new IllegalArgumentException("Something went wrong, while analysing " + procedureClass.getName());
  }


  public static <T1, T2, R> Signature resolveSignature2(Class<? extends JsonRemoteProcedure2<T1, T2, R>> procedureClass) {
    List<ParameterizedType> parametrizedInterfacesFor = findParametrizedInterfacesFor(procedureClass);
    for (ParameterizedType parameterizedType : parametrizedInterfacesFor) {
      if (parameterizedType.getRawType() == JsonRemoteProcedure2.class) {
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        return new ParameterizedSignature(actualTypeArguments[FIRST_PROCEDURE_PARAMETER],
          actualTypeArguments[SECOND_PROCEDURE_PARAMETER]);
      }
    }
    throw new IllegalArgumentException("Something went wrong, while analysing " + procedureClass.getName());
  }

  public static <T1, T2, T3, R> Signature resolveSignature3(Class<? extends JsonRemoteProcedure3<T1, T2, T3, R>> procedureClass) {
    List<ParameterizedType> parametrisedInterfaces = findParametrizedInterfacesFor(procedureClass);
    for (ParameterizedType parameterizedType : parametrisedInterfaces) {
      if (parameterizedType.getRawType() == JsonRemoteProcedure3.class) {
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        return new ParameterizedSignature(actualTypeArguments[FIRST_PROCEDURE_PARAMETER],
          actualTypeArguments[SECOND_PROCEDURE_PARAMETER], actualTypeArguments[THIRD_PROCEDURE_PARAMETER]);
      }
    }
    throw new IllegalArgumentException("Something went wrong, while analysing " + procedureClass.getName());
  }

  private static List<ParameterizedType> findParametrizedInterfacesFor(Class<?> analyzable) {
    return Arrays.stream(analyzable.getGenericInterfaces())
      .filter(genericInterface -> genericInterface instanceof ParameterizedType)
      .map(ParameterizedType.class::cast)
      .collect(Collectors.toList());
  }
}
