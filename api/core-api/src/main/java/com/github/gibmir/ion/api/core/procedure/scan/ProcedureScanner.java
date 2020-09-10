package com.github.gibmir.ion.api.core.procedure.scan;

import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure0;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure1;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure2;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure3;
import com.github.gibmir.ion.api.core.procedure.named.Named;
import com.github.gibmir.ion.api.core.procedure.signature.JsonRemoteProcedureSignature;
import com.github.gibmir.ion.api.core.procedure.signature.ParameterizedJsonRemoteProcedureSignature;
import org.reflections.ReflectionUtils;

import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ProcedureScanner {

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
  public static final Type[] EMPTY_ARGUMENT_TYPES = new Type[0];
  public static final String[] EMPTY_ARGUMENT_NAMES = new String[0];
  public static final String FIRST_PARAMETER_DEFAULT_NAME = "firstParameter";
  public static final String SECOND_PARAMETER_DEFAULT_NAME = "secondParameter";
  public static final String THIRD_PARAMETER_DEFAULT_NAME = "thirdParameter";
  public static final String CALL_METHOD_NAME = "call";

  private ProcedureScanner() {
  }

  @SuppressWarnings("unchecked")
  public static <R> JsonRemoteProcedureSignature resolveSignature0(Class<? extends JsonRemoteProcedure0<R>> procedureClass) {
    int parametersCount = 0;
    //unchecked array
    Method callMethod = ReflectionUtils.getMethods(procedureClass, ReflectionUtils.withName(CALL_METHOD_NAME),
      ReflectionUtils.withParametersCount(parametersCount)).iterator().next();
    List<ParameterizedType> parametrizedInterfaces = findParametrizedInterfacesFor(procedureClass);
    String procedureName = getProcedureName(procedureClass);
    for (ParameterizedType parametrizedInterface : parametrizedInterfaces) {
      if (parametrizedInterface.getRawType() == JsonRemoteProcedure0.class) {
        Type[] actualTypeArguments = parametrizedInterface.getActualTypeArguments();

        return new ParameterizedJsonRemoteProcedureSignature(procedureName, EMPTY_ARGUMENT_NAMES,
          EMPTY_ARGUMENT_TYPES,
          getReturnType(actualTypeArguments), MethodType.methodType(callMethod.getReturnType(),
          callMethod.getParameterTypes()), parametersCount);
      }
    }
    throw new IllegalArgumentException("Something went wrong, while analysing " + procedureName);
  }

  @SuppressWarnings("unchecked")
  public static <T, R> JsonRemoteProcedureSignature resolveSignature1(Class<? extends JsonRemoteProcedure1<T, R>> procedureClass) {
    int parametersCount = 1;
    //unchecked array
    Method callMethod = ReflectionUtils.getMethods(procedureClass, ReflectionUtils.withName(CALL_METHOD_NAME),
      ReflectionUtils.withParametersCount(parametersCount)).iterator().next();
    String[] parameterNames = new String[parametersCount];
    setParameterName(parameterNames, callMethod.getParameters(), FIRST_PROCEDURE_PARAMETER, FIRST_PARAMETER_DEFAULT_NAME);

    List<ParameterizedType> parametrizedInterfaces = findParametrizedInterfacesFor(procedureClass);
    String procedureName = getProcedureName(procedureClass);
    for (ParameterizedType parametrizedInterface : parametrizedInterfaces) {
      if (parametrizedInterface.getRawType() == JsonRemoteProcedure1.class) {
        Type[] actualTypeArguments = parametrizedInterface.getActualTypeArguments();
        return new ParameterizedJsonRemoteProcedureSignature(procedureName, parameterNames,
          new Type[]{actualTypeArguments[FIRST_PROCEDURE_PARAMETER]},
          getReturnType(actualTypeArguments), MethodType.methodType(callMethod.getReturnType(),
          callMethod.getParameterTypes()), parametersCount);
      }
    }
    throw new IllegalArgumentException("Something went wrong, while analysing " + procedureName);
  }

  @SuppressWarnings("unchecked")
  public static <T1, T2, R> JsonRemoteProcedureSignature resolveSignature2(Class<? extends JsonRemoteProcedure2<T1, T2, R>> procedureClass) {
    int parametersCount = 2;
    Method callMethod = ReflectionUtils.getMethods(procedureClass, ReflectionUtils.withName("call"),
      ReflectionUtils.withParametersCount(parametersCount)).iterator().next();
    String[] parameterNames = new String[parametersCount];
    Parameter[] parameters = callMethod.getParameters();
    setParameterName(parameterNames, parameters, FIRST_PROCEDURE_PARAMETER, FIRST_PARAMETER_DEFAULT_NAME);
    setParameterName(parameterNames, parameters, SECOND_PROCEDURE_PARAMETER, SECOND_PARAMETER_DEFAULT_NAME);

    List<ParameterizedType> parametrizedInterfacesFor = findParametrizedInterfacesFor(procedureClass);
    String procedureName = getProcedureName(procedureClass);
    for (ParameterizedType parameterizedType : parametrizedInterfacesFor) {
      if (parameterizedType.getRawType() == JsonRemoteProcedure2.class) {
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        return new ParameterizedJsonRemoteProcedureSignature(procedureName, parameterNames,
          new Type[]{actualTypeArguments[FIRST_PROCEDURE_PARAMETER],
            actualTypeArguments[SECOND_PROCEDURE_PARAMETER]},
          getReturnType(actualTypeArguments), MethodType.methodType(callMethod.getReturnType(),
          callMethod.getParameterTypes()), parametersCount);
      }
    }
    throw new IllegalArgumentException("Something went wrong, while analysing " + procedureName);
  }

  @SuppressWarnings("unchecked")
  public static <T1, T2, T3, R> JsonRemoteProcedureSignature resolveSignature3(Class<? extends JsonRemoteProcedure3<T1, T2, T3, R>> procedureClass) {
    int parametersCount = 3;
    Method callMethod = ReflectionUtils.getMethods(procedureClass, ReflectionUtils.withName("call"),
      ReflectionUtils.withParametersCount(parametersCount)).iterator().next();
    String[] parameterNames = new String[parametersCount];
    Parameter[] parameters = callMethod.getParameters();
    setParameterName(parameterNames, parameters, FIRST_PROCEDURE_PARAMETER, FIRST_PARAMETER_DEFAULT_NAME);
    setParameterName(parameterNames, parameters, SECOND_PROCEDURE_PARAMETER, SECOND_PARAMETER_DEFAULT_NAME);
    setParameterName(parameterNames, parameters, THIRD_PROCEDURE_PARAMETER, THIRD_PARAMETER_DEFAULT_NAME);
    List<ParameterizedType> parametrisedInterfaces = findParametrizedInterfacesFor(procedureClass);
    String procedureName = getProcedureName(procedureClass);
    for (ParameterizedType parameterizedType : parametrisedInterfaces) {
      if (parameterizedType.getRawType() == JsonRemoteProcedure3.class) {
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        return new ParameterizedJsonRemoteProcedureSignature(procedureName, parameterNames,
          new Type[]{actualTypeArguments[FIRST_PROCEDURE_PARAMETER],
            actualTypeArguments[SECOND_PROCEDURE_PARAMETER], actualTypeArguments[THIRD_PROCEDURE_PARAMETER]},
          getReturnType(actualTypeArguments), MethodType.methodType(callMethod.getReturnType(),
          callMethod.getParameterTypes()), parametersCount);
      }
    }
    throw new IllegalArgumentException("Something went wrong, while analysing " + procedureName);
  }

  private static void setParameterName(String[] parameterNames, Parameter[] parameters, int position, String defaultName) {
    parameterNames[position] = getParameterNameOrElse(parameters[position],
      defaultName);
  }

  private static String getParameterNameOrElse(Parameter parameter, String defaultName) {
    if (parameter.isAnnotationPresent(Named.class)) {
      return parameter.getAnnotation(Named.class).name();
    } else {
      return defaultName;
    }
  }

  public static String getProcedureName(Class<?> procedureClass) {
    if (procedureClass.isAnnotationPresent(Named.class)) {
      return procedureClass.getAnnotation(Named.class).name();
    } else {
      return procedureClass.getName();
    }
  }

  private static List<ParameterizedType> findParametrizedInterfacesFor(Class<?> analyzable) {
    return Arrays.stream(analyzable.getGenericInterfaces())
      .filter(genericInterface -> genericInterface instanceof ParameterizedType)
      .map(ParameterizedType.class::cast)
      .collect(Collectors.toList());
  }

  private static Type getReturnType(Type[] actualTypeArguments) {
    return actualTypeArguments[ /*return type is last*/ actualTypeArguments.length - 1];
  }
}
