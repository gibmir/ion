package com.github.gibmir.ion.maven.plugin.service;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github.gibmir.ion.maven.plugin.environment.IonMavenPluginTestEnvironment.TEST_DESCRIPTION;
import static com.github.gibmir.ion.maven.plugin.environment.IonMavenPluginTestEnvironment.TEST_ID;
import static com.github.gibmir.ion.maven.plugin.environment.IonMavenPluginTestEnvironment.TEST_PROCEDURE_NAME;
import static com.github.gibmir.ion.maven.plugin.environment.IonMavenPluginTestEnvironment.TEST_PROPERTY_NAME;
import static com.github.gibmir.ion.maven.plugin.environment.IonMavenPluginTestEnvironment.TEST_SERVICE_NAME;
import static com.github.gibmir.ion.maven.plugin.environment.IonMavenPluginTestEnvironment.TEST_TYPE_NAME;
import static com.github.gibmir.ion.maven.plugin.environment.IonMavenPluginTestEnvironment.TestProcedure;
import static com.github.gibmir.ion.maven.plugin.environment.IonMavenPluginTestEnvironment.TestPropertyType;
import static com.github.gibmir.ion.maven.plugin.environment.IonMavenPluginTestEnvironment.TestService;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ServiceGenerationUtilsTest {

  @Test
  void testTypeSpecificationWithoutProcedures() throws NoSuchMethodException {
    TestService testService = new TestService(TEST_ID, TEST_SERVICE_NAME, TEST_DESCRIPTION);
    TypeSpec typeSpec = ServiceGenerationUtils.asTypeSpecification(testService);
    assertThat(typeSpec.name, equalToIgnoringCase(TEST_SERVICE_NAME));
  }

  @Test
  void testTypeSpecificationWithNoArgProcedure() throws NoSuchMethodException {
    TestPropertyType testPropertyType = new TestPropertyType(TEST_ID, TEST_PROPERTY_NAME, TEST_DESCRIPTION,
      TEST_TYPE_NAME);
    TestProcedure testProcedure = new TestProcedure(testPropertyType, TEST_DESCRIPTION, TEST_PROCEDURE_NAME, TEST_ID);
    TestService testService = new TestService(TEST_ID, TEST_SERVICE_NAME, TEST_DESCRIPTION, testProcedure);
    TypeSpec typeSpec = ServiceGenerationUtils.asTypeSpecification(testService);
    assertThat(typeSpec.name, equalToIgnoringCase(TEST_SERVICE_NAME));
    List<MethodSpec> methodSpecs = typeSpec.methodSpecs;
    assertThat(methodSpecs, not(empty()));
    assertThat(methodSpecs, hasItem(hasToString(containsStringIgnoringCase(TEST_PROCEDURE_NAME))));
    assertThat(methodSpecs, hasItem(hasToString(containsString(TEST_DESCRIPTION))));
  }

  @Test
  void testTypeSpecificationWithOneArgProcedure() throws NoSuchMethodException {
    TestPropertyType testPropertyType = new TestPropertyType(TEST_ID, TEST_PROPERTY_NAME, TEST_DESCRIPTION,
      TEST_TYPE_NAME);
    TestProcedure testProcedure = new TestProcedure(testPropertyType, TEST_DESCRIPTION, TEST_PROCEDURE_NAME, TEST_ID,
      //first arg type
      testPropertyType);
    TestService testService = new TestService(TEST_ID, TEST_SERVICE_NAME, TEST_DESCRIPTION, testProcedure);
    TypeSpec typeSpec = ServiceGenerationUtils.asTypeSpecification(testService);
    assertThat(typeSpec.name, equalToIgnoringCase(TEST_SERVICE_NAME));
    List<MethodSpec> methodSpecs = typeSpec.methodSpecs;
    assertThat(methodSpecs, not(empty()));
    assertThat(methodSpecs, hasItem(hasToString(containsStringIgnoringCase(TEST_PROCEDURE_NAME))));
    assertThat(methodSpecs, hasItem(hasToString(containsString(TEST_DESCRIPTION))));
  }

  @Test
  void testTypeSpecificationWithTwoArgProcedure() throws NoSuchMethodException {
    TestPropertyType testPropertyType = new TestPropertyType(TEST_ID, TEST_PROPERTY_NAME, TEST_DESCRIPTION,
      TEST_TYPE_NAME);
    TestProcedure testProcedure = new TestProcedure(testPropertyType, TEST_DESCRIPTION, TEST_PROCEDURE_NAME, TEST_ID,
      //first arg type
      testPropertyType,
      //second arg type
      testPropertyType);
    TestService testService = new TestService(TEST_ID, TEST_SERVICE_NAME, TEST_DESCRIPTION, testProcedure);
    TypeSpec typeSpec = ServiceGenerationUtils.asTypeSpecification(testService);
    assertThat(typeSpec.name, equalToIgnoringCase(TEST_SERVICE_NAME));
    List<MethodSpec> methodSpecs = typeSpec.methodSpecs;
    assertThat(methodSpecs, not(empty()));
    assertThat(methodSpecs, hasItem(hasToString(containsStringIgnoringCase(TEST_PROCEDURE_NAME))));
    assertThat(methodSpecs, hasItem(hasToString(containsString(TEST_DESCRIPTION))));
  }

  @Test
  void testTypeSpecificationWithThreeArgProcedure() throws NoSuchMethodException {
    TestPropertyType testPropertyType = new TestPropertyType(TEST_ID, TEST_PROPERTY_NAME, TEST_DESCRIPTION,
      TEST_TYPE_NAME);
    TestProcedure testProcedure = new TestProcedure(testPropertyType, TEST_DESCRIPTION, TEST_PROCEDURE_NAME, TEST_ID,
      //first arg type
      testPropertyType,
      //second arg type
      testPropertyType,
      //third arg type
      testPropertyType);
    TestService testService = new TestService(TEST_ID, TEST_SERVICE_NAME, TEST_DESCRIPTION, testProcedure);
    TypeSpec typeSpec = ServiceGenerationUtils.asTypeSpecification(testService);
    assertThat(typeSpec.name, equalToIgnoringCase(TEST_SERVICE_NAME));
    List<MethodSpec> methodSpecs = typeSpec.methodSpecs;
    assertThat(methodSpecs, not(empty()));
    assertThat(methodSpecs, hasItem(hasToString(containsStringIgnoringCase(TEST_PROCEDURE_NAME))));
    assertThat(methodSpecs, hasItem(hasToString(containsString(TEST_DESCRIPTION))));
  }

  @Test
  void testTypeSpecificationWithTooMuchArgumentsCount() {
    TestPropertyType testPropertyType = new TestPropertyType(TEST_ID, TEST_PROPERTY_NAME, TEST_DESCRIPTION,
      TEST_TYPE_NAME);
    TestProcedure testProcedure = new TestProcedure(testPropertyType, TEST_DESCRIPTION, TEST_PROCEDURE_NAME, TEST_ID,
      //first arg type
      testPropertyType,
      //second arg type
      testPropertyType,
      //third arg type
      testPropertyType,
      //unnecessary arg type
      testPropertyType);
    TestService testService = new TestService(TEST_ID, TEST_SERVICE_NAME, TEST_DESCRIPTION, testProcedure);
    IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
      () -> ServiceGenerationUtils.asTypeSpecification(testService));
    String message = illegalArgumentException.getMessage();
    assertThat(message, containsString(TEST_SERVICE_NAME));
    assertThat(message, containsString(TEST_PROCEDURE_NAME));
    assertThat(message, containsString(String.valueOf(testProcedure.getArgumentTypes().length)));
  }
}
