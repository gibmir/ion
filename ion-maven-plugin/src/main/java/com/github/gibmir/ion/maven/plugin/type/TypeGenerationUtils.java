package com.github.gibmir.ion.maven.plugin.type;

import com.github.gibmir.ion.api.schema.type.PropertyType;
import com.github.gibmir.ion.api.schema.type.TypeDeclaration;
import com.github.gibmir.ion.api.schema.type.TypeParameter;
import com.github.gibmir.ion.api.schema.type.Types;
import com.github.gibmir.ion.maven.plugin.IonPluginMojo;
import com.github.gibmir.ion.maven.plugin.service.ServiceGenerationUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.Map;
import java.util.Stack;

public class TypeGenerationUtils {

  public static final String GETTER_METHOD_PREFIX = "get";
  public static final String SETTER_METHOD_PREFIX = "set";
  public static final String PARAMETRIZATION_SEPARATOR = "<";

  public static void loadTypes(Stack<TypeDeclaration> typeLoadingStack, String packageName, Path path) throws IOException {
    while (!typeLoadingStack.isEmpty()) {
      TypeDeclaration typeDeclaration = typeLoadingStack.pop();
      TypeSpec typeSpec = asTypeSpecification(typeDeclaration);
      JavaFile.builder(packageName, typeSpec).build().writeTo(path);
    }
  }

  public static TypeSpec asTypeSpecification(TypeDeclaration typeDeclaration) {
    TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder(IonPluginMojo.asClassName(typeDeclaration.getName()))
      .addModifiers(Modifier.PUBLIC)
      .addJavadoc(typeDeclaration.getDescription());
    TypeParameter[] parameters = typeDeclaration.getParameters();
    if (isParametrized(parameters)) {
      for (TypeParameter parameter : parameters) {
        typeSpecBuilder.addTypeVariable(TypeVariableName.get(parameter.getName()));
      }
    }
    for (PropertyType propertyType : typeDeclaration.getPropertyTypes()) {
      String propertyName = propertyType.getName();
      String propertyTypeName = propertyType.getTypeName();
      Types type = Types.from(propertyTypeName);
      FieldSpec fieldSpec;
      MethodSpec getterMethodSpec;
      MethodSpec setterMethodSpec;
      if (Types.CUSTOM.equals(type)) {
        if (isParametrizedProperty(propertyTypeName)) {
          ParametrizedTypeTree parametrizedTypeTree = ParametrizedTypeTree.from(propertyTypeName);
          ParameterizedTypeName parametrizedFieldTypeName = parametrizedTypeTree.buildTypeName();
          fieldSpec = createField(propertyType, propertyName, parametrizedFieldTypeName);
          getterMethodSpec = createGetter(propertyName, parametrizedFieldTypeName);
          setterMethodSpec = createSetter(propertyName, parametrizedFieldTypeName);
        } else {
          ClassName fieldTypeName = ClassName.bestGuess(IonPluginMojo.asClassName(propertyTypeName));
          fieldSpec = createField(propertyType, propertyName, fieldTypeName);
          getterMethodSpec = createGetter(propertyName, fieldTypeName);
          setterMethodSpec = createSetter(propertyName, fieldTypeName);
        }
      } else {
        //custom type not parametrized
        Type fieldType = type.resolve();
        fieldSpec = createField(propertyType, propertyName, fieldType);
        getterMethodSpec = createGetter(propertyName, fieldType);
        setterMethodSpec = createSetter(propertyName, fieldType);
      }
      typeSpecBuilder.addField(fieldSpec)
        .addMethod(getterMethodSpec)
        .addMethod(setterMethodSpec);
    }
    return typeSpecBuilder.build();
  }

  private static boolean isParametrized(TypeParameter[] parameters) {
    return parameters.length > 0;
  }

  private static MethodSpec createSetter(String typeName, Type fieldType) {
    return MethodSpec.methodBuilder(SETTER_METHOD_PREFIX + IonPluginMojo.asClassName(typeName))
      .addModifiers(Modifier.PUBLIC)
      .addCode(ServiceGenerationUtils.SETTER_CODE_BLOCK, typeName, typeName)
      .addParameter(ParameterSpec.builder(fieldType, IonPluginMojo.asFieldName(typeName)).build())
      .build();
  }

  private static MethodSpec createSetter(String typeName, ClassName fieldTypeName) {
    return MethodSpec.methodBuilder(SETTER_METHOD_PREFIX + IonPluginMojo.asClassName(typeName))
      .addModifiers(Modifier.PUBLIC)
      .addCode(ServiceGenerationUtils.SETTER_CODE_BLOCK, typeName, typeName)
      .addParameter(ParameterSpec.builder(fieldTypeName, IonPluginMojo.asFieldName(typeName)).build())
      .build();
  }

  private static MethodSpec createSetter(String typeName, ParameterizedTypeName fieldTypeName) {
    return MethodSpec.methodBuilder(SETTER_METHOD_PREFIX + IonPluginMojo.asClassName(typeName))
      .addModifiers(Modifier.PUBLIC)
      .addCode(ServiceGenerationUtils.SETTER_CODE_BLOCK, typeName, typeName)
      .addParameter(ParameterSpec.builder(fieldTypeName, IonPluginMojo.asFieldName(typeName)).build())
      .build();
  }

  private static FieldSpec createField(PropertyType propertyType, String typeName, ClassName fieldTypeName) {
    return FieldSpec.builder(fieldTypeName, IonPluginMojo.asFieldName(typeName), Modifier.PRIVATE)
      .addJavadoc(propertyType.getDescription())
      .build();
  }

  private static FieldSpec createField(PropertyType propertyType, String typeName, ParameterizedTypeName fieldTypeName) {
    return FieldSpec.builder(fieldTypeName, IonPluginMojo.asFieldName(typeName), Modifier.PRIVATE)
      .addJavadoc(propertyType.getDescription())
      .build();
  }

  private static FieldSpec createField(PropertyType propertyType, String typeName, Type fieldType) {
    return FieldSpec.builder(fieldType, IonPluginMojo.asFieldName(typeName), Modifier.PRIVATE)
      .addJavadoc(propertyType.getDescription())
      .build();
  }

  private static MethodSpec createGetter(String typeName, ClassName fieldTypeName) {
    return MethodSpec.methodBuilder(GETTER_METHOD_PREFIX + IonPluginMojo.asClassName(typeName))
      .addModifiers(Modifier.PUBLIC)
      .addCode(ServiceGenerationUtils.PROCEDURE_RETURN_CODE_BLOCK, typeName)
      .returns(fieldTypeName)
      .build();
  }

  private static MethodSpec createGetter(String typeName, ParameterizedTypeName fieldTypeName) {
    return MethodSpec.methodBuilder(GETTER_METHOD_PREFIX + IonPluginMojo.asClassName(typeName))
      .addModifiers(Modifier.PUBLIC)
      .addCode(ServiceGenerationUtils.PROCEDURE_RETURN_CODE_BLOCK, typeName)
      .returns(fieldTypeName)
      .build();
  }

  private static MethodSpec createGetter(String typeName, Type fieldType) {
    return MethodSpec.methodBuilder(GETTER_METHOD_PREFIX + IonPluginMojo.asClassName(typeName))
      .addModifiers(Modifier.PUBLIC)
      .addCode(ServiceGenerationUtils.PROCEDURE_RETURN_CODE_BLOCK, typeName)
      .returns(fieldType)
      .build();
  }

  public static Stack<TypeDeclaration> buildLoadingStack(Map<String, TypeDeclaration> schemaTypeDeclarationMap) {
    Stack<TypeDeclaration> typeLoadingStack = new Stack<>();
    for (TypeDeclaration typeDeclaration : schemaTypeDeclarationMap.values()) {
      fillStack(typeDeclaration, typeLoadingStack, schemaTypeDeclarationMap);
    }
    return typeLoadingStack;
  }

  private static Stack<TypeDeclaration> fillStack(TypeDeclaration typeDeclaration, Stack<TypeDeclaration> stack,
                                                  Map<String, TypeDeclaration> schemaTypeDeclarationMap) {
    //if type is not custom - it has already loaded
    if (!Types.CUSTOM.equals(Types.from(typeDeclaration.getName()))) {
      return stack;
    }
    //remove to prevent duplicates
    stack.remove(typeDeclaration);
    stack.push(typeDeclaration);
    for (PropertyType propertyType : typeDeclaration.getPropertyTypes()) {
      String propertyName = propertyType.getName();
      String propertyTypeName = propertyType.getTypeName();
      if (Types.CUSTOM.equals(Types.from(propertyTypeName))) {
        if (isParametrizedProperty(propertyTypeName)) {
          fillStackWithParametrizedProperty(stack, schemaTypeDeclarationMap, propertyTypeName);
        } else {
          //if custom property not parametrized - need to load
          TypeDeclaration propertyTypeDeclaration = schemaTypeDeclarationMap.get(propertyTypeName);
          if (propertyTypeDeclaration == null) {
            String message = String.format("Property [%s] type [%s] does not contains in %s",
              propertyName, propertyTypeName, schemaTypeDeclarationMap);
            throw new IllegalArgumentException(message);
          }
          return fillStack(propertyTypeDeclaration, stack, schemaTypeDeclarationMap);
        }
      }
    }
    return stack;
  }

  private static void fillStackWithParametrizedProperty(Stack<TypeDeclaration> stack,
                                                        Map<String, TypeDeclaration> schemaTypeDeclarationMap,
                                                        String propertyTypeName) {
    ParametrizedTypeTree parametrizedTypeTree = ParametrizedTypeTree.from(propertyTypeName);
    Stack<String> parametrizedTypeNameStack = parametrizedTypeTree.buildTypeLoadingStack();
    while (!parametrizedTypeNameStack.isEmpty()) {
      String parametrizedTypeName = parametrizedTypeNameStack.pop();
      TypeDeclaration typeDeclaration = schemaTypeDeclarationMap.get(parametrizedTypeName);
      if (typeDeclaration != null) {
        stack.remove(typeDeclaration);
        stack.push(typeDeclaration);
      }
    }
  }

  public static boolean isParametrizedProperty(String propertyTypeName) {
    return propertyTypeName.contains(PARAMETRIZATION_SEPARATOR);
  }
}
