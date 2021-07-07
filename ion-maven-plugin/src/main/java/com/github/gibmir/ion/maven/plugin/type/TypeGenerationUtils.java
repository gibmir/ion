package com.github.gibmir.ion.maven.plugin.type;

import com.github.gibmir.ion.api.schema.type.PropertyType;
import com.github.gibmir.ion.api.schema.type.TypeDeclaration;
import com.github.gibmir.ion.api.schema.type.TypeParameter;
import com.github.gibmir.ion.api.schema.type.Types;
import com.github.gibmir.ion.maven.plugin.IonPluginMojo;
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

public final class TypeGenerationUtils {

  public static final String GETTER_METHOD_PREFIX = "get";
  public static final String SETTER_METHOD_PREFIX = "set";
  public static final String PARAMETRIZATION_SEPARATOR = "<";
  public static final String PROCEDURE_RETURN_CODE_BLOCK = "return $L;";
  public static final String SETTER_CODE_BLOCK = "this.$L = $L;";

  private TypeGenerationUtils() {
  }

  /**
   * Load types for specified type loading stack.
   *
   * @param typeLoadingStack types stack
   * @param packageName      package for loading types
   * @param path             path to write
   * @throws IOException if path is incorrect
   */
  public static void loadTypes(final Stack<TypeDeclaration> typeLoadingStack, final String packageName,
                               final Path path) throws IOException {
    while (!typeLoadingStack.isEmpty()) {
      TypeDeclaration typeDeclaration = typeLoadingStack.pop();
      TypeSpec typeSpec = asTypeSpecification(typeDeclaration);
      JavaFile.builder(packageName, typeSpec).build().writeTo(path);
    }
  }

  /**
   * Represents type declaration as type specification.
   *
   * @param typeDeclaration type declaration
   * @return type spec
   */
  public static TypeSpec asTypeSpecification(final TypeDeclaration typeDeclaration) {
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

  private static boolean isParametrized(final TypeParameter[] parameters) {
    return parameters.length > 0;
  }

  private static MethodSpec createSetter(final String typeName, final Type fieldType) {
    return MethodSpec.methodBuilder(SETTER_METHOD_PREFIX + IonPluginMojo.asClassName(typeName))
      .addModifiers(Modifier.PUBLIC)
      .addCode(SETTER_CODE_BLOCK, typeName, typeName)
      .addParameter(ParameterSpec.builder(fieldType, IonPluginMojo.asFieldName(typeName)).build())
      .build();
  }

  private static MethodSpec createSetter(final String typeName, final ClassName fieldTypeName) {
    return MethodSpec.methodBuilder(SETTER_METHOD_PREFIX + IonPluginMojo.asClassName(typeName))
      .addModifiers(Modifier.PUBLIC)
      .addCode(SETTER_CODE_BLOCK, typeName, typeName)
      .addParameter(ParameterSpec.builder(fieldTypeName, IonPluginMojo.asFieldName(typeName)).build())
      .build();
  }

  private static MethodSpec createSetter(final String typeName, final ParameterizedTypeName fieldTypeName) {
    return MethodSpec.methodBuilder(SETTER_METHOD_PREFIX + IonPluginMojo.asClassName(typeName))
      .addModifiers(Modifier.PUBLIC)
      .addCode(SETTER_CODE_BLOCK, typeName, typeName)
      .addParameter(ParameterSpec.builder(fieldTypeName, IonPluginMojo.asFieldName(typeName)).build())
      .build();
  }

  private static FieldSpec createField(final PropertyType propertyType, final String typeName,
                                       final ClassName fieldTypeName) {
    return FieldSpec.builder(fieldTypeName, IonPluginMojo.asFieldName(typeName), Modifier.PRIVATE)
      .addJavadoc(propertyType.getDescription())
      .build();
  }

  private static FieldSpec createField(final PropertyType propertyType, final String typeName,
                                       final ParameterizedTypeName fieldTypeName) {
    return FieldSpec.builder(fieldTypeName, IonPluginMojo.asFieldName(typeName), Modifier.PRIVATE)
      .addJavadoc(propertyType.getDescription())
      .build();
  }

  private static FieldSpec createField(final PropertyType propertyType, final String typeName, final Type fieldType) {
    return FieldSpec.builder(fieldType, IonPluginMojo.asFieldName(typeName), Modifier.PRIVATE)
      .addJavadoc(propertyType.getDescription())
      .build();
  }

  private static MethodSpec createGetter(final String typeName, final ClassName fieldTypeName) {
    return MethodSpec.methodBuilder(GETTER_METHOD_PREFIX + IonPluginMojo.asClassName(typeName))
      .addModifiers(Modifier.PUBLIC)
      .addCode(PROCEDURE_RETURN_CODE_BLOCK, typeName)
      .returns(fieldTypeName)
      .build();
  }

  private static MethodSpec createGetter(final String typeName, final ParameterizedTypeName fieldTypeName) {
    return MethodSpec.methodBuilder(GETTER_METHOD_PREFIX + IonPluginMojo.asClassName(typeName))
      .addModifiers(Modifier.PUBLIC)
      .addCode(PROCEDURE_RETURN_CODE_BLOCK, typeName)
      .returns(fieldTypeName)
      .build();
  }

  private static MethodSpec createGetter(final String typeName, final Type fieldType) {
    return MethodSpec.methodBuilder(GETTER_METHOD_PREFIX + IonPluginMojo.asClassName(typeName))
      .addModifiers(Modifier.PUBLIC)
      .addCode(PROCEDURE_RETURN_CODE_BLOCK, typeName)
      .returns(fieldType)
      .build();
  }

  /**
   * @param schemaTypeDeclarationMap schema type declarations
   * @return type loading stack
   */
  public static Stack<TypeDeclaration> buildLoadingStack(final Map<String, TypeDeclaration> schemaTypeDeclarationMap) {
    Stack<TypeDeclaration> typeLoadingStack = new Stack<>();
    for (TypeDeclaration typeDeclaration : schemaTypeDeclarationMap.values()) {
      fillStack(typeDeclaration, typeLoadingStack, schemaTypeDeclarationMap);
    }
    return typeLoadingStack;
  }

  private static Stack<TypeDeclaration> fillStack(final TypeDeclaration typeDeclaration,
                                                  final Stack<TypeDeclaration> stack,
                                                  final Map<String, TypeDeclaration> schemaTypeDeclarationMap) {
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

  private static void fillStackWithParametrizedProperty(final Stack<TypeDeclaration> stack,
                                                        final Map<String, TypeDeclaration> schemaTypeDeclarationMap,
                                                        final String propertyTypeName) {
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

  /**
   * @param propertyTypeName property type name
   * @return true if specified type is generic
   */
  public static boolean isParametrizedProperty(final String propertyTypeName) {
    return propertyTypeName.contains(PARAMETRIZATION_SEPARATOR);
  }
}
