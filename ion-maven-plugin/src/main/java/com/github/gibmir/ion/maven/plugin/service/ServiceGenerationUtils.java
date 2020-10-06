package com.github.gibmir.ion.maven.plugin.service;

import com.github.gibmir.ion.api.client.factory.RequestFactory;
import com.github.gibmir.ion.api.client.factory.provider.RequestFactoryProvider;
import com.github.gibmir.ion.api.client.request.Request0;
import com.github.gibmir.ion.api.client.request.Request1;
import com.github.gibmir.ion.api.client.request.Request2;
import com.github.gibmir.ion.api.client.request.Request3;
import com.github.gibmir.ion.api.core.procedure.scan.ProcedureScanner;
import com.github.gibmir.ion.api.schema.service.Service;
import com.github.gibmir.ion.api.schema.service.procedure.Procedure;
import com.github.gibmir.ion.api.schema.type.PropertyType;
import com.github.gibmir.ion.maven.plugin.IonPluginMojo;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;

public class ServiceGenerationUtils {

  public static final String LOAD_PROCEDURE_CODE_BLOCK = "$L = $L.$L($L.class)";
  public static final String RETURN_PROCEDURE = "return $L;";

  public static TypeSpec asTypeSpecification(Service service) throws NoSuchMethodException {
    TypeSpec.Builder serviceTypeBuilder = TypeSpec.classBuilder(IonPluginMojo.asClassName(service.getName()))
      .addModifiers(Modifier.PUBLIC);
    String requestFactorySimpleName = RequestFactory.class.getSimpleName();
    String requestFactoryFieldName = IonPluginMojo.asFieldName(requestFactorySimpleName);

    CodeBlock.Builder statementBuilder = CodeBlock.builder()
      .add("$T $L = $T.load().provide();", RequestFactory.class, requestFactoryFieldName, RequestFactoryProvider.class);
    prepareServiceProcedures(service, serviceTypeBuilder, statementBuilder);
    MethodSpec serviceConstructor = MethodSpec.constructorBuilder()
      .addModifiers(Modifier.PUBLIC)
      .addStatement(statementBuilder.build())
      .build();

    serviceTypeBuilder.addModifiers(Modifier.PUBLIC)
      .addMethod(serviceConstructor);

    return serviceTypeBuilder.build();
  }

  private static void prepareServiceProcedures(Service service, TypeSpec.Builder serviceTypeBuilder,
                                               CodeBlock.Builder statementBuilder) throws NoSuchMethodException {
    for (Procedure serviceProcedure : service.getServiceProcedures()) {
      String serviceProcedureClassName = IonPluginMojo.asClassName(serviceProcedure.getName());
      String serviceProcedureFieldName = IonPluginMojo.asFieldName(serviceProcedure.getName());

      PropertyType[] argumentTypes = serviceProcedure.getArgumentTypes();
      int argumentsCount = argumentTypes.length;
      ClassName returnArgumentClassName = ClassName.bestGuess(IonPluginMojo.asClassName(serviceProcedure.getReturnArgumentType().getName()));
      if (argumentsCount == 0) {
        ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(ClassName.get(Request0.class), returnArgumentClassName);
        serviceTypeBuilder.addMethod(MethodSpec.methodBuilder(serviceProcedureFieldName).addModifiers(Modifier.PUBLIC)
          .addCode(RETURN_PROCEDURE, serviceProcedureFieldName)
          .returns(parameterizedTypeName)
          .build());
        serviceTypeBuilder.addField(parameterizedTypeName,
          serviceProcedureFieldName, Modifier.PRIVATE, Modifier.FINAL);
        statementBuilder.add(LOAD_PROCEDURE_CODE_BLOCK, serviceProcedureFieldName, IonPluginMojo.asFieldName(RequestFactory.class.getSimpleName()),
          RequestFactory.class.getMethod("noArg", Class.class).getName(), serviceProcedureClassName);
      } else if (argumentsCount == 1) {
        prepareOneArgProcedureField(serviceTypeBuilder, serviceProcedureFieldName,
          argumentTypes[ProcedureScanner.FIRST_PROCEDURE_PARAMETER], returnArgumentClassName);
        statementBuilder.add(LOAD_PROCEDURE_CODE_BLOCK, serviceProcedureFieldName, IonPluginMojo.asFieldName(RequestFactory.class.getSimpleName()),
          RequestFactory.class.getMethod("singleArg", Class.class).getName(), serviceProcedureClassName);
      } else if (argumentsCount == 2) {
        prepareTwoArgProcedureField(serviceTypeBuilder, serviceProcedureFieldName, argumentTypes, returnArgumentClassName);
        statementBuilder.add(LOAD_PROCEDURE_CODE_BLOCK, serviceProcedureFieldName, IonPluginMojo.asFieldName(RequestFactory.class.getSimpleName()),
          RequestFactory.class.getMethod("twoArg", Class.class).getName(), serviceProcedureClassName);
      } else if (argumentsCount == 3) {
        prepareThreeArgProcedureField(serviceTypeBuilder, serviceProcedureFieldName, argumentTypes, returnArgumentClassName);
        statementBuilder.add(LOAD_PROCEDURE_CODE_BLOCK, serviceProcedureFieldName,
          IonPluginMojo.asFieldName(RequestFactory.class.getSimpleName()),
          RequestFactory.class.getMethod("threeArg", Class.class).getName(), serviceProcedureClassName);
      } else {
        throw new IllegalArgumentException();
      }
    }
  }

  private static void prepareOneArgProcedureField(TypeSpec.Builder serviceTypeBuilder, String serviceProcedureFieldName,
                                                  PropertyType argumentType, ClassName returnArgumentClassName) {
    String firstArgumentName = argumentType.getName();
    ClassName firstArgumentClassName = ClassName.bestGuess(IonPluginMojo.asClassName(firstArgumentName));
    ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(ClassName.get(Request1.class),
      firstArgumentClassName, returnArgumentClassName);
    serviceTypeBuilder.addField(parameterizedTypeName,
      serviceProcedureFieldName, Modifier.PRIVATE, Modifier.FINAL);
    serviceTypeBuilder.addMethod(MethodSpec.methodBuilder(serviceProcedureFieldName).addModifiers(Modifier.PUBLIC)
      .addCode(RETURN_PROCEDURE, serviceProcedureFieldName)
      .returns(parameterizedTypeName)
      .build());
  }

  private static void prepareTwoArgProcedureField(TypeSpec.Builder serviceTypeBuilder, String serviceProcedureFieldName,
                                                  PropertyType[] argumentTypes, ClassName returnArgumentClassName) {
    String firstArgumentName = argumentTypes[ProcedureScanner.FIRST_PROCEDURE_PARAMETER].getName();
    ClassName firstArgumentClassName = ClassName.bestGuess(IonPluginMojo.asClassName(firstArgumentName));
    String secondArgumentName = argumentTypes[ProcedureScanner.SECOND_PROCEDURE_PARAMETER].getName();
    ClassName secondArgumentClassName = ClassName.bestGuess(IonPluginMojo.asClassName(secondArgumentName));
    ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(ClassName.get(Request2.class),
      firstArgumentClassName, secondArgumentClassName, returnArgumentClassName);
    serviceTypeBuilder.addField(parameterizedTypeName,
      serviceProcedureFieldName, Modifier.PRIVATE, Modifier.FINAL);
    serviceTypeBuilder.addMethod(MethodSpec.methodBuilder(serviceProcedureFieldName).addModifiers(Modifier.PUBLIC)
      .addCode(RETURN_PROCEDURE, serviceProcedureFieldName)
      .returns(parameterizedTypeName)
      .build());
  }

  private static void prepareThreeArgProcedureField(TypeSpec.Builder serviceTypeBuilder, String serviceProcedureFieldName,
                                                    PropertyType[] argumentTypes, ClassName returnArgumentClassName) {
    String firstArgumentName = argumentTypes[ProcedureScanner.FIRST_PROCEDURE_PARAMETER].getName();
    ClassName firstArgumentClassName = ClassName.bestGuess(IonPluginMojo.asClassName(firstArgumentName));
    String secondArgumentName = argumentTypes[ProcedureScanner.SECOND_PROCEDURE_PARAMETER].getName();
    ClassName secondArgumentClassName = ClassName.bestGuess(IonPluginMojo.asClassName(secondArgumentName));
    String thirdArgumentName = argumentTypes[ProcedureScanner.THIRD_PROCEDURE_PARAMETER].getName();
    ClassName thirdArgumentClassName = ClassName.bestGuess(IonPluginMojo.asClassName(thirdArgumentName));
    ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(ClassName.get(Request3.class),
      firstArgumentClassName, secondArgumentClassName, thirdArgumentClassName, returnArgumentClassName);
    serviceTypeBuilder.addField(parameterizedTypeName,
      serviceProcedureFieldName, Modifier.PRIVATE, Modifier.FINAL);
    serviceTypeBuilder.addMethod(MethodSpec.methodBuilder(serviceProcedureFieldName).addModifiers(Modifier.PUBLIC)
      .addCode(RETURN_PROCEDURE, serviceProcedureFieldName)
      .returns(parameterizedTypeName)
      .build());

  }
}
