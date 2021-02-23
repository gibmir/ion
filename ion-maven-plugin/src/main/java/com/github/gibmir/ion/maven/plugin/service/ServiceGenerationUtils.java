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
import com.github.gibmir.ion.maven.plugin.type.ParametrizedTypeTree;
import com.github.gibmir.ion.maven.plugin.type.TypeGenerationUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;

public class ServiceGenerationUtils {

  public static final String LOAD_PROCEDURE_CODE_BLOCK = "$L = $L.$L($L.class)";
  public static final String PROCEDURE_RETURN_CODE_BLOCK = "return $L;";
  public static final String SETTER_CODE_BLOCK = "this.$L = $L;";
  public static final String FACTORY_LOAD = "$T $L = $T.load().provide()";

  public static TypeSpec asTypeSpecification(Service service) throws NoSuchMethodException {
    TypeSpec.Builder serviceTypeBuilder = TypeSpec.classBuilder(IonPluginMojo.asClassName(service.getName()))
      .addModifiers(Modifier.PUBLIC)
      .addJavadoc(service.getDescription());
    String requestFactorySimpleName = RequestFactory.class.getSimpleName();
    String requestFactoryFieldName = IonPluginMojo.asFieldName(requestFactorySimpleName);

    CodeBlock.Builder constructorBuilder = CodeBlock.builder()
      .addStatement(FACTORY_LOAD, RequestFactory.class, requestFactoryFieldName, RequestFactoryProvider.class);
    prepareServiceProcedures(service, serviceTypeBuilder, constructorBuilder);
    CodeBlock constructorCodeBlock = constructorBuilder.build();
    MethodSpec serviceConstructor = MethodSpec.constructorBuilder()
      .addModifiers(Modifier.PUBLIC)
      .addCode(constructorCodeBlock)
      .build();

    serviceTypeBuilder.addMethod(serviceConstructor);

    return serviceTypeBuilder.build();
  }

  /**
   * @implNote Constructor builder needs to add procedure request initialization
   */
  private static void prepareServiceProcedures(Service service, TypeSpec.Builder serviceTypeBuilder,
                                               CodeBlock.Builder constructorBuilder) throws NoSuchMethodException {
    for (Procedure serviceProcedure : service.getServiceProcedures()) {
      String serviceProcedureClassName = IonPluginMojo.asClassName(serviceProcedure.getName());
      String serviceProcedureFieldName = IonPluginMojo.asFieldName(serviceProcedure.getName());
      PropertyType[] argumentTypes = serviceProcedure.getArgumentTypes();
      int argumentsCount = argumentTypes.length;
      ClassName returnArgumentClassName = ClassName.bestGuess(IonPluginMojo.asClassName(serviceProcedure.getReturnArgumentType().getTypeName()));
      if (argumentsCount == 0) {
        ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(ClassName.get(Request0.class), returnArgumentClassName);
        serviceTypeBuilder.addMethod(MethodSpec.methodBuilder(serviceProcedureFieldName).addModifiers(Modifier.PUBLIC)
          .addJavadoc(serviceProcedure.getDescription())
          .addCode(PROCEDURE_RETURN_CODE_BLOCK, serviceProcedureFieldName)
          .returns(parameterizedTypeName)
          .build());
        serviceTypeBuilder.addField(parameterizedTypeName,
          serviceProcedureFieldName, Modifier.PRIVATE, Modifier.FINAL);
        constructorBuilder.addStatement(LOAD_PROCEDURE_CODE_BLOCK, serviceProcedureFieldName, IonPluginMojo.asFieldName(RequestFactory.class.getSimpleName()),
          RequestFactory.class.getMethod("noArg", Class.class).getName(), serviceProcedureClassName);
      } else if (argumentsCount == 1) {
        prepareOneArgProcedureField(serviceTypeBuilder, serviceProcedureFieldName,
          argumentTypes[ProcedureScanner.FIRST_PROCEDURE_PARAMETER], returnArgumentClassName,
          serviceProcedure.getDescription());
        constructorBuilder.addStatement(LOAD_PROCEDURE_CODE_BLOCK, serviceProcedureFieldName, IonPluginMojo.asFieldName(RequestFactory.class.getSimpleName()),
          RequestFactory.class.getMethod("singleArg", Class.class).getName(), serviceProcedureClassName);
      } else if (argumentsCount == 2) {
        prepareTwoArgProcedureField(serviceTypeBuilder, serviceProcedureFieldName, argumentTypes, returnArgumentClassName,
          serviceProcedure.getDescription());
        constructorBuilder.addStatement(LOAD_PROCEDURE_CODE_BLOCK, serviceProcedureFieldName, IonPluginMojo.asFieldName(RequestFactory.class.getSimpleName()),
          RequestFactory.class.getMethod("twoArg", Class.class).getName(), serviceProcedureClassName);
      } else if (argumentsCount == 3) {
        prepareThreeArgProcedureField(serviceTypeBuilder, serviceProcedureFieldName, argumentTypes, returnArgumentClassName,
          serviceProcedure.getDescription());
        constructorBuilder.addStatement(LOAD_PROCEDURE_CODE_BLOCK, serviceProcedureFieldName,
          IonPluginMojo.asFieldName(RequestFactory.class.getSimpleName()),
          RequestFactory.class.getMethod("threeArg", Class.class).getName(), serviceProcedureClassName);
      } else {
        String message = String.format("There is too much arguments count [%s] for service [%s] procedure [%s]",
          argumentsCount, service.getName(), serviceProcedure.getName());
        throw new IllegalArgumentException(message);
      }
    }
  }

  private static void prepareOneArgProcedureField(TypeSpec.Builder serviceTypeBuilder, String serviceProcedureFieldName,
                                                  PropertyType argumentType, ClassName returnArgumentClassName,
                                                  String procedureDescription) {
    String firstArgumentTypeName = argumentType.getTypeName();
    TypeName firstArgumentClassName = resolveClassName(firstArgumentTypeName);
    ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(ClassName.get(Request1.class),
      firstArgumentClassName, returnArgumentClassName);
    serviceTypeBuilder.addField(parameterizedTypeName,
      serviceProcedureFieldName, Modifier.PRIVATE, Modifier.FINAL);
    serviceTypeBuilder.addMethod(MethodSpec.methodBuilder(serviceProcedureFieldName).addModifiers(Modifier.PUBLIC)
      .addCode(PROCEDURE_RETURN_CODE_BLOCK, serviceProcedureFieldName)
      .addJavadoc(procedureDescription)
      .returns(parameterizedTypeName)
      .build());
  }

  private static void prepareTwoArgProcedureField(TypeSpec.Builder serviceTypeBuilder, String serviceProcedureFieldName,
                                                  PropertyType[] argumentTypes, ClassName returnArgumentClassName,
                                                  String procedureDescription) {
    String firstArgumentTypeName = argumentTypes[ProcedureScanner.FIRST_PROCEDURE_PARAMETER].getTypeName();
    TypeName firstArgumentClassName = resolveClassName(firstArgumentTypeName);
    String secondArgumentName = argumentTypes[ProcedureScanner.SECOND_PROCEDURE_PARAMETER].getTypeName();
    TypeName secondArgumentClassName = resolveClassName(secondArgumentName);
    ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(ClassName.get(Request2.class),
      firstArgumentClassName, secondArgumentClassName, returnArgumentClassName);
    serviceTypeBuilder.addField(parameterizedTypeName,
      serviceProcedureFieldName, Modifier.PRIVATE, Modifier.FINAL);
    serviceTypeBuilder.addMethod(MethodSpec.methodBuilder(serviceProcedureFieldName).addModifiers(Modifier.PUBLIC)
      .addCode(PROCEDURE_RETURN_CODE_BLOCK, serviceProcedureFieldName)
      .addJavadoc(procedureDescription)
      .returns(parameterizedTypeName)
      .build());
  }

  private static void prepareThreeArgProcedureField(TypeSpec.Builder serviceTypeBuilder, String serviceProcedureFieldName,
                                                    PropertyType[] argumentTypes, ClassName returnArgumentClassName,
                                                    String procedureDescription) {
    String firstArgumentTypeName = argumentTypes[ProcedureScanner.FIRST_PROCEDURE_PARAMETER].getTypeName();
    TypeName firstArgumentClassName = resolveClassName(firstArgumentTypeName);
    String secondArgumentTypeName = argumentTypes[ProcedureScanner.SECOND_PROCEDURE_PARAMETER].getTypeName();
    TypeName secondArgumentClassName = resolveClassName(secondArgumentTypeName);
    String thirdArgumentTypeName = argumentTypes[ProcedureScanner.THIRD_PROCEDURE_PARAMETER].getTypeName();
    TypeName thirdArgumentClassName = resolveClassName(thirdArgumentTypeName);
    ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(ClassName.get(Request3.class),
      firstArgumentClassName, secondArgumentClassName, thirdArgumentClassName, returnArgumentClassName);
    serviceTypeBuilder.addField(parameterizedTypeName,
      serviceProcedureFieldName, Modifier.PRIVATE, Modifier.FINAL);
    serviceTypeBuilder.addMethod(MethodSpec.methodBuilder(serviceProcedureFieldName).addModifiers(Modifier.PUBLIC)
      .addCode(PROCEDURE_RETURN_CODE_BLOCK, serviceProcedureFieldName)
      .addJavadoc(procedureDescription)
      .returns(parameterizedTypeName)
      .build());
  }

  private static TypeName resolveClassName(String argumentTypeName) {
    if (TypeGenerationUtils.isParametrizedProperty(argumentTypeName)) {
      ParametrizedTypeTree parametrizedTypeTree = ParametrizedTypeTree.from(argumentTypeName);
      return parametrizedTypeTree.buildTypeName();
    } else {
      return ClassName.bestGuess(IonPluginMojo.asClassName(argumentTypeName));
    }
  }
}
