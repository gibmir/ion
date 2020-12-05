package com.github.gibmir.ion.maven.plugin.procedure;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github.gibmir.ion.maven.plugin.environment.IonMavenPluginTestEnvironment.TEST_DESCRIPTION;
import static com.github.gibmir.ion.maven.plugin.environment.IonMavenPluginTestEnvironment.TEST_ID;
import static com.github.gibmir.ion.maven.plugin.environment.IonMavenPluginTestEnvironment.TEST_PROCEDURE_NAME;
import static com.github.gibmir.ion.maven.plugin.environment.IonMavenPluginTestEnvironment.TEST_PROPERTY_NAME;
import static com.github.gibmir.ion.maven.plugin.environment.IonMavenPluginTestEnvironment.TEST_TYPE_NAME;
import static com.github.gibmir.ion.maven.plugin.environment.IonMavenPluginTestEnvironment.TestProcedure;
import static com.github.gibmir.ion.maven.plugin.environment.IonMavenPluginTestEnvironment.TestPropertyType;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.containsStringIgnoringCase;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.hasToString;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProcedureGenerationUtilsTest {

  @Test
  void testAsTypeSpecification() {
    TestPropertyType testPropertyType = new TestPropertyType(TEST_ID, TEST_PROPERTY_NAME, TEST_DESCRIPTION,
      TEST_TYPE_NAME);
    TestProcedure testProcedure = new TestProcedure(testPropertyType, TEST_DESCRIPTION, TEST_PROCEDURE_NAME, TEST_ID);
    TypeSpec typeSpec = ProcedureGenerationUtils.asTypeSpecification(testProcedure);
    assertThat(typeSpec.name, containsStringIgnoringCase(TEST_PROCEDURE_NAME));
    assertThat(typeSpec.javadoc.toString(), equalTo(TEST_DESCRIPTION));
    List<MethodSpec> methodSpecs = typeSpec.methodSpecs;
    assertThat(methodSpecs, hasSize(1));
    assertThat(methodSpecs, hasItem(hasToString(Matchers.containsStringIgnoringCase("call"))));
  }

  @Test
  void testAsTypeSpecificationWithOneProperty() {
    TestPropertyType testPropertyType = new TestPropertyType(TEST_ID, TEST_PROPERTY_NAME, TEST_DESCRIPTION,
      TEST_TYPE_NAME);
    TestProcedure testProcedure = new TestProcedure(testPropertyType, TEST_DESCRIPTION, TEST_PROCEDURE_NAME, TEST_ID,
      testPropertyType);
    TypeSpec typeSpec = ProcedureGenerationUtils.asTypeSpecification(testProcedure);
    assertThat(typeSpec.name, containsStringIgnoringCase(TEST_PROCEDURE_NAME));
    assertThat(typeSpec.javadoc.toString(), equalTo(TEST_DESCRIPTION));
    List<MethodSpec> methodSpecs = typeSpec.methodSpecs;
    assertThat(methodSpecs, hasSize(1));
    assertThat(methodSpecs, hasItem(hasToString(Matchers.containsStringIgnoringCase("call"))));
    MethodSpec methodSpec = methodSpecs.get(0);
    assertThat(methodSpec.parameters.size(), is(1));
  }

  @Test
  void testAsTypeSpecificationWithTwoProperty() {
    TestPropertyType testPropertyType = new TestPropertyType(TEST_ID, TEST_PROPERTY_NAME, TEST_DESCRIPTION,
      TEST_TYPE_NAME);
    TestProcedure testProcedure = new TestProcedure(testPropertyType, TEST_DESCRIPTION, TEST_PROCEDURE_NAME, TEST_ID,
      testPropertyType, testPropertyType);
    TypeSpec typeSpec = ProcedureGenerationUtils.asTypeSpecification(testProcedure);
    assertThat(typeSpec.name, containsStringIgnoringCase(TEST_PROCEDURE_NAME));
    assertThat(typeSpec.javadoc.toString(), equalTo(TEST_DESCRIPTION));
    List<MethodSpec> methodSpecs = typeSpec.methodSpecs;
    assertThat(methodSpecs, hasSize(1));
    assertThat(methodSpecs, hasItem(hasToString(Matchers.containsStringIgnoringCase("call"))));
    MethodSpec methodSpec = methodSpecs.get(0);
    assertThat(methodSpec.parameters.size(), is(2));
  }

  @Test
  void testAsTypeSpecificationWithThreeProperty() {
    TestPropertyType testPropertyType = new TestPropertyType(TEST_ID, TEST_PROPERTY_NAME, TEST_DESCRIPTION,
      TEST_TYPE_NAME);
    TestProcedure testProcedure = new TestProcedure(testPropertyType, TEST_DESCRIPTION, TEST_PROCEDURE_NAME, TEST_ID,
      testPropertyType, testPropertyType, testPropertyType);
    TypeSpec typeSpec = ProcedureGenerationUtils.asTypeSpecification(testProcedure);
    assertThat(typeSpec.name, containsStringIgnoringCase(TEST_PROCEDURE_NAME));
    assertThat(typeSpec.javadoc.toString(), equalTo(TEST_DESCRIPTION));
    List<MethodSpec> methodSpecs = typeSpec.methodSpecs;
    assertThat(methodSpecs, hasSize(1));
    assertThat(methodSpecs, hasItem(hasToString(Matchers.containsStringIgnoringCase("call"))));
    MethodSpec methodSpec = methodSpecs.get(0);
    assertThat(methodSpec.parameters.size(), is(3));
  }

  @Test
  void testAsTypeSpecificationWithFourProperties() {
    TestPropertyType testPropertyType = new TestPropertyType(TEST_ID, TEST_PROPERTY_NAME, TEST_DESCRIPTION,
      TEST_TYPE_NAME);
    TestProcedure testProcedure = new TestProcedure(testPropertyType, TEST_DESCRIPTION, TEST_PROCEDURE_NAME, TEST_ID,
      testPropertyType, testPropertyType, testPropertyType, testPropertyType);
    IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
      () -> ProcedureGenerationUtils.asTypeSpecification(testProcedure));
    String message = illegalArgumentException.getMessage();
    assertThat(message, containsString(TEST_PROCEDURE_NAME));
    //4 parameters
    assertThat(message, containsString(String.valueOf(4)));
  }
}
