package com.github.gibmir.ion.api.server.scan;

import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure1;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure2;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure3;
import com.github.gibmir.ion.api.dto.method.signature.ParameterizedSignature;
import com.github.gibmir.ion.api.dto.method.signature.Signature;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

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
    Type[] genericInterfaces = procedureClass.getGenericInterfaces();
    for (Type genericInterface : genericInterfaces) {
      if (genericInterface instanceof ParameterizedType) {
        ParameterizedType parameterizedType = (ParameterizedType) genericInterface;
        if (parameterizedType.getRawType() == JsonRemoteProcedure1.class) {
          return new ParameterizedSignature(parameterizedType.getActualTypeArguments()[FIRST_PROCEDURE_PARAMETER]);
        }
      }
    }
    throw new IllegalStateException("Something went wrong, while analysing " + procedureClass.getName());
  }


  public static <T1, T2, R> Signature resolveSignature2(Class<? extends JsonRemoteProcedure2<T1, T2, R>> procedureClass) {
    Type[] genericInterfaces = procedureClass.getGenericInterfaces();
    for (Type genericInterface : genericInterfaces) {
      if (genericInterface instanceof ParameterizedType) {
        ParameterizedType parameterizedType = (ParameterizedType) genericInterface;
        if (parameterizedType.getRawType() == JsonRemoteProcedure2.class) {
          Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
          return new ParameterizedSignature(actualTypeArguments[FIRST_PROCEDURE_PARAMETER],
            actualTypeArguments[SECOND_PROCEDURE_PARAMETER]);
        }
      }
    }
    throw new IllegalArgumentException("Something went wrong, while analysing " + procedureClass.getName());
  }

  public static <T1, T2, T3, R> Signature resolveSignature3(Class<? extends JsonRemoteProcedure3<T1, T2, T3, R>> procedureClass) {
    Type[] genericInterfaces = procedureClass.getGenericInterfaces();
    for (Type genericInterface : genericInterfaces) {
      if (genericInterface instanceof ParameterizedType) {
        ParameterizedType parameterizedType = (ParameterizedType) genericInterface;
        if (parameterizedType.getRawType() == JsonRemoteProcedure1.class) {
          Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
          return new ParameterizedSignature(actualTypeArguments[FIRST_PROCEDURE_PARAMETER],
            actualTypeArguments[SECOND_PROCEDURE_PARAMETER], actualTypeArguments[THIRD_PROCEDURE_PARAMETER]);
        }
      }
    }
    throw new IllegalArgumentException("Something went wrong, while analysing " + procedureClass.getName());
  }
}
