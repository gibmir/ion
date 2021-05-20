package com.github.gibmir.ion.scanner;

import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure0;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure1;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure2;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure3;
import com.github.gibmir.ion.api.core.procedure.named.Named;
import com.github.gibmir.ion.scanner.signature.JsonRemoteProcedureSignature;
import com.github.gibmir.ion.scanner.signature.ParameterizedJsonRemoteProcedureSignature;
import org.reflections.ReflectionUtils;

import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
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
  public static final String PROCEDURE_MAIN_METHOD_NAME = "call";

  private ProcedureScanner() {
  }

  @SuppressWarnings("unchecked")
  public static JsonRemoteProcedureSignature resolveSignature0(Class<?> procedureClass) {
    int parametersCount = 0;
    //unchecked array
    Method callMethod = ReflectionUtils.getMethods(procedureClass, ReflectionUtils.withName(PROCEDURE_MAIN_METHOD_NAME),
      ReflectionUtils.withParametersCount(parametersCount)).iterator().next();
    //resolve parametrized types
    Collection<Class<?>> analyzableHierarchy = resolveHierarchy(procedureClass, JsonRemoteProcedure0.class);
    Collection<ParameterizedType> parametrizedInterfaces = getParametrizedInterfacesFrom(analyzableHierarchy);
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
  public static JsonRemoteProcedureSignature resolveSignature1(Class<?> procedureClass) {
    int parametersCount = 1;
    String procedureName = getProcedureName(procedureClass);
    //unchecked array
    Method callMethod = ReflectionUtils.getAllMethods(procedureClass, ReflectionUtils.withName(PROCEDURE_MAIN_METHOD_NAME),
      ReflectionUtils.withParametersCount(parametersCount)).iterator().next();
    String[] parameterNames = new String[parametersCount];
    setParameterName(parameterNames, callMethod.getParameters(), FIRST_PROCEDURE_PARAMETER, FIRST_PARAMETER_DEFAULT_NAME);
    //resolve parametrized types
    Collection<Class<?>> analyzableHierarchy = resolveHierarchy(procedureClass, JsonRemoteProcedure1.class);
    Collection<ParameterizedType> parametrizedInterfaces = getParametrizedInterfacesFrom(analyzableHierarchy);
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
  public static JsonRemoteProcedureSignature resolveSignature2(Class<?> procedureClass) {
    int parametersCount = 2;
    String procedureName = getProcedureName(procedureClass);
    Method callMethod = ReflectionUtils.getMethods(procedureClass, ReflectionUtils.withName(PROCEDURE_MAIN_METHOD_NAME),
      ReflectionUtils.withParametersCount(parametersCount)).iterator().next();
    String[] parameterNames = new String[parametersCount];
    Parameter[] parameters = callMethod.getParameters();
    setParameterName(parameterNames, parameters, FIRST_PROCEDURE_PARAMETER, FIRST_PARAMETER_DEFAULT_NAME);
    setParameterName(parameterNames, parameters, SECOND_PROCEDURE_PARAMETER, SECOND_PARAMETER_DEFAULT_NAME);
    //resolve parametrized types
    Collection<Class<?>> analyzableHierarchy = resolveHierarchy(procedureClass, JsonRemoteProcedure2.class);
    Collection<ParameterizedType> parametrizedInterfaces = getParametrizedInterfacesFrom(analyzableHierarchy);
    for (ParameterizedType parameterizedType : parametrizedInterfaces) {
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
  public static JsonRemoteProcedureSignature resolveSignature3(Class<?> procedureClass) {
    int parametersCount = 3;
    String procedureName = getProcedureName(procedureClass);
    Method callMethod = ReflectionUtils.getMethods(procedureClass, ReflectionUtils.withName(PROCEDURE_MAIN_METHOD_NAME),
      ReflectionUtils.withParametersCount(parametersCount)).iterator().next();
    String[] parameterNames = new String[parametersCount];
    Parameter[] parameters = callMethod.getParameters();
    setParameterName(parameterNames, parameters, FIRST_PROCEDURE_PARAMETER, FIRST_PARAMETER_DEFAULT_NAME);
    setParameterName(parameterNames, parameters, SECOND_PROCEDURE_PARAMETER, SECOND_PARAMETER_DEFAULT_NAME);
    setParameterName(parameterNames, parameters, THIRD_PROCEDURE_PARAMETER, THIRD_PARAMETER_DEFAULT_NAME);
    //resolve parametrized types
    Collection<Class<?>> analyzableHierarchy = resolveHierarchy(procedureClass, JsonRemoteProcedure3.class);
    Collection<ParameterizedType> parametrizedInterfaces = getParametrizedInterfacesFrom(analyzableHierarchy);
    for (ParameterizedType parameterizedType : parametrizedInterfaces) {
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

  @SuppressWarnings("unchecked")
  private static Collection<Class<?>> resolveHierarchy(Class<?> analyzableType, Class<?> type) {
    Set<Class<?>> hierarchy = ReflectionUtils.getAllSuperTypes(analyzableType,
      //if specified type assignable from analyzable super types
      type::isAssignableFrom);
    hierarchy.add(analyzableType);
    return hierarchy;
  }

  private static Collection<ParameterizedType> getParametrizedInterfacesFrom(Collection<Class<?>> analyzableHierarchy) {
    return analyzableHierarchy.stream()
      //get all generic interfaces for each hierarchy node
      .map(Class::getGenericInterfaces)
      .flatMap(Arrays::stream)
      .filter(genericInterface -> genericInterface instanceof ParameterizedType)
      .map(ParameterizedType.class::cast)
      .collect(Collectors.toList());
  }

  private static Type getReturnType(Type[] actualTypeArguments) {
    return actualTypeArguments[ /*return type is last*/ actualTypeArguments.length - 1];
  }
}
