package com.github.gibmir.ion.maven.plugin.type;

import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure1;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure2;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure3;
import com.github.gibmir.ion.api.core.procedure.scan.ProcedureScanner;
import com.github.gibmir.ion.api.schema.type.PropertyType;
import com.github.gibmir.ion.api.schema.type.TypeDeclaration;
import com.github.gibmir.ion.api.schema.type.Types;
import com.github.gibmir.ion.maven.plugin.IonPluginMojo;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.Map;
import java.util.Stack;

public class TypeGenerationUtils {

  public static final String GETTER_METHOD_PREFIX = "get";
  public static final String SETTER_METHOD_PREFIX = "set";

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
    for (PropertyType propertyType : typeDeclaration.getPropertyTypes()) {
      String typeName = propertyType.getName();
      Types type = Types.from(typeName);
      FieldSpec fieldSpec;
      MethodSpec getterMethodSpec;
      MethodSpec setterMethodSpec;
      if (Types.CUSTOM.equals(type)) {
        ClassName fieldTypeName = ClassName.bestGuess(IonPluginMojo.asClassName(typeName));
        fieldSpec = createField(propertyType, typeName, fieldTypeName);
        getterMethodSpec = createGetter(typeName, fieldTypeName);
        setterMethodSpec = createSetter(typeName, fieldTypeName);
      } else {
        Type fieldType = type.resolve();
        fieldSpec = createField(propertyType, typeName, fieldType);
        getterMethodSpec = createGetter(typeName, fieldType);
        setterMethodSpec = createSetter(typeName, fieldType);
      }
      typeSpecBuilder.addField(fieldSpec)
        .addMethod(getterMethodSpec)
        .addMethod(setterMethodSpec);
    }
    return typeSpecBuilder.build();
  }



  private static MethodSpec createSetter(String typeName, Type fieldType) {
    return MethodSpec.methodBuilder(SETTER_METHOD_PREFIX + IonPluginMojo.asClassName(typeName))
      .addModifiers(Modifier.PUBLIC)
      .addParameter(ParameterSpec.builder(fieldType, IonPluginMojo.asFieldName(typeName)).build())
      .build();
  }

  private static MethodSpec createSetter(String typeName, ClassName fieldTypeName) {
    return MethodSpec.methodBuilder(SETTER_METHOD_PREFIX + IonPluginMojo.asClassName(typeName))
      .addModifiers(Modifier.PUBLIC)
      .addParameter(ParameterSpec.builder(fieldTypeName, IonPluginMojo.asFieldName(typeName)).build())
      .build();
  }

  private static FieldSpec createField(PropertyType propertyType, String typeName, ClassName fieldTypeName) {
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
      .returns(fieldTypeName)
      .build();
  }

  private static MethodSpec createGetter(String typeName, Type fieldType) {
    return MethodSpec.methodBuilder(GETTER_METHOD_PREFIX + IonPluginMojo.asClassName(typeName))
      .addModifiers(Modifier.PUBLIC)
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
    if (!Types.CUSTOM.equals(Types.from(typeDeclaration.getName()))) {
      return stack;
    }
    //to prevent duplicates
    stack.remove(typeDeclaration);
    stack.push(typeDeclaration);
    for (PropertyType propertyType : typeDeclaration.getPropertyTypes()) {
      String propertyTypeName = propertyType.getName();
      if (Types.CUSTOM.equals(Types.from(propertyTypeName))) {
        TypeDeclaration propertyTypeDeclaration = schemaTypeDeclarationMap.get(propertyTypeName);
        if (propertyTypeDeclaration == null) {
          String message = String.format("Property type [%s] does not contains in %s",
            propertyTypeName, schemaTypeDeclarationMap);
          throw new IllegalArgumentException(message);
        }
        return fillStack(propertyTypeDeclaration, stack, schemaTypeDeclarationMap);
      }
    }
    return stack;
  }


}
