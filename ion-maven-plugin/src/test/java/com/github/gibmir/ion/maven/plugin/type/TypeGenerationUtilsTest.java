package com.github.gibmir.ion.maven.plugin.type;

import com.github.gibmir.ion.api.schema.type.TypeDeclaration;
import com.github.gibmir.ion.maven.plugin.environment.IonMavenPluginTestEnvironment;
import com.github.gibmir.ion.maven.plugin.procedure.ProcedureGenerationUtils;
import com.github.gibmir.ion.maven.plugin.service.ServiceGenerationUtils;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Stack;

class TypeGenerationUtilsTest {


  @Test
  void testLoadTypes() throws IOException {
    IonMavenPluginTestEnvironment.TestTypeDeclaration simpleType = new IonMavenPluginTestEnvironment.TestTypeDeclaration("simple", "simpleType", "desc",
      new IonMavenPluginTestEnvironment.TestPropertyType("pog", "string", "pog simple", "string"));
    IonMavenPluginTestEnvironment.TestTypeDeclaration composedType = new IonMavenPluginTestEnvironment.TestTypeDeclaration("composed", "composedType", "desc",
      new IonMavenPluginTestEnvironment.TestPropertyType("pog", "simple", "pog simple", "string"));
    Map<String, TypeDeclaration> typeDeclarationMap = Map.of("composed", composedType, "simple", simpleType);
    Stack<TypeDeclaration> typeDeclarations = TypeGenerationUtils.buildLoadingStack(typeDeclarationMap);
    TypeGenerationUtils.loadTypes(typeDeclarations, "com.github.gibmir.ion",
      Path.of("F:\\Projects\\ion\\ion-maven-plugin\\src\\main\\resources"));
  }

  @Test
  void testProcedure() throws IOException {
    TypeSpec typeSpec = ProcedureGenerationUtils.asTypeSpecification(new IonMavenPluginTestEnvironment.TestProcedure(
      new IonMavenPluginTestEnvironment.TestPropertyType("id", "string", "", "string"), "description", "procedure", "id"));
    JavaFile.builder("com.github.gibmir.ion", typeSpec).build()
      .writeTo(Path.of("F:\\Projects\\ion\\ion-maven-plugin\\src\\main\\resources"));
  }

  @Test
  void testService() throws IOException, NoSuchMethodException {
    IonMavenPluginTestEnvironment.TestProcedure procedure = new IonMavenPluginTestEnvironment.TestProcedure(
      new IonMavenPluginTestEnvironment.TestPropertyType("id", "string", "", "string"),
      "description", "procedure", "id");

    TypeSpec typeSpec = ProcedureGenerationUtils.asTypeSpecification(procedure);
    JavaFile.builder("com.github.gibmir.ion", typeSpec).build()
      .writeTo(Path.of("F:\\Projects\\ion\\ion-maven-plugin\\src\\main\\resources"));
    IonMavenPluginTestEnvironment.TestService testService = new IonMavenPluginTestEnvironment.TestService("id",
      "testService", "pog service", procedure);


    TypeSpec serviceTypeSpec = ServiceGenerationUtils.asTypeSpecification(testService);
    JavaFile.builder("com.github.gibmir.ion", serviceTypeSpec).build()
      .writeTo(Path.of("F:\\Projects\\ion\\ion-maven-plugin\\src\\main\\resources"));

  }
}
