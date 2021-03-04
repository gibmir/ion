package com.github.gibmir.ion.maven.plugin.procedure;

import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure0;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure1;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure2;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure3;
import com.github.gibmir.ion.api.core.procedure.named.Named;
import com.github.gibmir.ion.api.core.procedure.scan.ProcedureScanner;
import com.github.gibmir.ion.api.schema.procedure.Procedure;
import com.github.gibmir.ion.api.schema.type.PropertyType;
import com.github.gibmir.ion.maven.plugin.IonPluginMojo;
import com.github.gibmir.ion.maven.plugin.type.ParametrizedTypeTree;
import com.github.gibmir.ion.maven.plugin.type.TypeGenerationUtils;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;

public class ProcedureGenerationUtils {

  public static final String NAMED_ANNOTATION_NAME_PARAM = "name";

  public static TypeSpec asTypeSpecification(Procedure procedure) {
    String procedureName = procedure.getName();
    AnnotationSpec annotationSpec = AnnotationSpec.builder(Named.class)
      .addMember(NAMED_ANNOTATION_NAME_PARAM, IonPluginMojo.asAnnotationMember(procedureName)).build();
    TypeSpec.Builder procedureTypeSpecBuilder = TypeSpec.interfaceBuilder(IonPluginMojo.asClassName(procedureName))
      .addModifiers(Modifier.PUBLIC).addAnnotation(annotationSpec)
      .addJavadoc(procedure.getDescription());
    PropertyType[] argumentTypes = procedure.getArgumentTypes();
    int argumentTypesLength = argumentTypes.length;
    ClassName returnClassName = ClassName.bestGuess(IonPluginMojo.asClassName(procedure.getReturnArgumentType().getTypeName()));
    MethodSpec.Builder callMethodSpec = MethodSpec.methodBuilder(ProcedureScanner.PROCEDURE_MAIN_METHOD_NAME)
      .addModifiers(Modifier.PUBLIC)
      .addModifiers(Modifier.ABSTRACT)
      .addAnnotation(Override.class)
      .returns(returnClassName);
    if (argumentTypesLength == 0) {
      procedureTypeSpecBuilder.addSuperinterface(ParameterizedTypeName.get(ClassName.get(JsonRemoteProcedure0.class),
        returnClassName));
    } else if (argumentTypesLength == 1) {
      prepareOneArgProcedure(procedureTypeSpecBuilder, argumentTypes[ProcedureScanner.FIRST_PROCEDURE_PARAMETER],
        returnClassName, callMethodSpec);
    } else if (argumentTypesLength == 2) {
      prepareTwoArgProcedure(procedureTypeSpecBuilder, argumentTypes, returnClassName, callMethodSpec);
    } else if (argumentTypesLength == 3) {
      prepareThreeArgProcedure(procedureTypeSpecBuilder, argumentTypes, returnClassName, callMethodSpec);
    } else {
      String message = String.format("Procedure [%s] has too many arguments [%d]", procedureName, argumentTypesLength);
      throw new IllegalArgumentException(message);
    }
    procedureTypeSpecBuilder.addMethod(callMethodSpec.build());
    return procedureTypeSpecBuilder.build();
  }

  private static void prepareOneArgProcedure(TypeSpec.Builder procedureTypeSpecBuilder, PropertyType argumentType,
                                             ClassName returnClassName, MethodSpec.Builder callMethodSpec) {
    String firstArgumentTypeName = argumentType.getTypeName();
    String argumentName = argumentType.getName();
    TypeName firstArgumentClassName = resolveClassName(firstArgumentTypeName);
    procedureTypeSpecBuilder.addSuperinterface(ParameterizedTypeName.get(ClassName.get(JsonRemoteProcedure1.class),
      firstArgumentClassName, returnClassName));
    AnnotationSpec annotationSpec = AnnotationSpec.builder(Named.class).addMember(NAMED_ANNOTATION_NAME_PARAM,
      IonPluginMojo.asAnnotationMember(argumentName)).build();
    ParameterSpec parameterSpec = ParameterSpec.builder(firstArgumentClassName, IonPluginMojo.asFieldName(argumentName))
      .addAnnotation(annotationSpec).build();
    callMethodSpec.addParameter(parameterSpec);
  }

  private static void prepareTwoArgProcedure(TypeSpec.Builder procedureTypeSpecBuilder, PropertyType[] argumentTypes,
                                             ClassName returnClassName, MethodSpec.Builder callMethodSpec) {
    String firstArgumentName = argumentTypes[ProcedureScanner.FIRST_PROCEDURE_PARAMETER].getName();
    String firstArgumentTypeName = argumentTypes[ProcedureScanner.FIRST_PROCEDURE_PARAMETER].getTypeName();
    TypeName firstArgumentClassName = resolveClassName(firstArgumentTypeName);
    String secondArgumentName = argumentTypes[ProcedureScanner.SECOND_PROCEDURE_PARAMETER].getName();
    String secondArgumentTypeName = argumentTypes[ProcedureScanner.SECOND_PROCEDURE_PARAMETER].getTypeName();
    TypeName secondArgumentClassName = resolveClassName(secondArgumentTypeName);
    procedureTypeSpecBuilder.addSuperinterface(ParameterizedTypeName.get(ClassName.get(JsonRemoteProcedure2.class),
      firstArgumentClassName, secondArgumentClassName, returnClassName));
    callMethodSpec.addParameter(ParameterSpec.builder(firstArgumentClassName, IonPluginMojo.asFieldName(firstArgumentName))
      .addAnnotation(AnnotationSpec.builder(Named.class).addMember(NAMED_ANNOTATION_NAME_PARAM,
        IonPluginMojo.asAnnotationMember(firstArgumentName)).build())
      .build())
      .addParameter(ParameterSpec.builder(secondArgumentClassName, IonPluginMojo.asFieldName(secondArgumentName))
        .addAnnotation(AnnotationSpec.builder(Named.class).addMember(NAMED_ANNOTATION_NAME_PARAM,
          IonPluginMojo.asAnnotationMember(secondArgumentName)).build())
        .build());
  }

  private static void prepareThreeArgProcedure(TypeSpec.Builder procedureTypeSpecBuilder, PropertyType[] argumentTypes,
                                               ClassName returnClassName, MethodSpec.Builder callMethodSpec) {
    String firstArgumentName = argumentTypes[ProcedureScanner.FIRST_PROCEDURE_PARAMETER].getName();
    String firstArgumentTypeName = argumentTypes[ProcedureScanner.FIRST_PROCEDURE_PARAMETER].getTypeName();
    TypeName firstArgumentClassName = resolveClassName(firstArgumentTypeName);
    String secondArgumentName = argumentTypes[ProcedureScanner.SECOND_PROCEDURE_PARAMETER].getName();
    String secondArgumentTypeName = argumentTypes[ProcedureScanner.SECOND_PROCEDURE_PARAMETER].getTypeName();
    TypeName secondArgumentClassName = resolveClassName(secondArgumentTypeName);
    String thirdArgumentName = argumentTypes[ProcedureScanner.THIRD_PROCEDURE_PARAMETER].getName();
    String thirdArgumentTypeName = argumentTypes[ProcedureScanner.THIRD_PROCEDURE_PARAMETER].getTypeName();
    TypeName thirdArgumentClassName = resolveClassName(thirdArgumentTypeName);
    procedureTypeSpecBuilder.addSuperinterface(ParameterizedTypeName.get(ClassName.get(JsonRemoteProcedure3.class),
      firstArgumentClassName, secondArgumentClassName, thirdArgumentClassName, returnClassName));
    callMethodSpec.addParameter(ParameterSpec.builder(firstArgumentClassName, IonPluginMojo.asFieldName(firstArgumentName))
      .addAnnotation(AnnotationSpec.builder(Named.class).addMember(NAMED_ANNOTATION_NAME_PARAM,
        IonPluginMojo.asAnnotationMember(firstArgumentName)).build())
      .build())
      .addParameter(ParameterSpec.builder(secondArgumentClassName, IonPluginMojo.asFieldName(secondArgumentName))
        .addAnnotation(AnnotationSpec.builder(Named.class).addMember(NAMED_ANNOTATION_NAME_PARAM,
          IonPluginMojo.asAnnotationMember(secondArgumentName)).build())
        .build())
      .addParameter(ParameterSpec.builder(thirdArgumentClassName, IonPluginMojo.asFieldName(thirdArgumentName))
        .addAnnotation(AnnotationSpec.builder(Named.class).addMember(NAMED_ANNOTATION_NAME_PARAM,
          IonPluginMojo.asAnnotationMember(thirdArgumentName)).build())
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
