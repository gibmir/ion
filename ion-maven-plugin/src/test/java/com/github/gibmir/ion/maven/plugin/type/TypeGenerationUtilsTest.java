package com.github.gibmir.ion.maven.plugin.type;

import com.github.gibmir.ion.api.schema.type.TypeDeclaration;
import com.github.gibmir.ion.api.schema.type.TypeParameter;
import com.squareup.javapoet.TypeSpec;
import org.junit.jupiter.api.Test;

import javax.lang.model.element.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import static com.github.gibmir.ion.maven.plugin.environment.IonMavenPluginTestEnvironment.TEST_DESCRIPTION;
import static com.github.gibmir.ion.maven.plugin.environment.IonMavenPluginTestEnvironment.TEST_ID;
import static com.github.gibmir.ion.maven.plugin.environment.IonMavenPluginTestEnvironment.TEST_PROPERTY_NAME;
import static com.github.gibmir.ion.maven.plugin.environment.IonMavenPluginTestEnvironment.TEST_TYPE_NAME;
import static com.github.gibmir.ion.maven.plugin.environment.IonMavenPluginTestEnvironment.TestPropertyType;
import static com.github.gibmir.ion.maven.plugin.environment.IonMavenPluginTestEnvironment.TestTypeDeclaration;
import static com.github.gibmir.ion.maven.plugin.environment.IonMavenPluginTestEnvironment.TestTypeParameter;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.hasToString;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TypeGenerationUtilsTest {

  public static final String TEST_CUSTOM_TYPE_NAME = "custom";
  public static final String TEST_ANOTHER_CUSTOM_TYPE_NAME = "custom2";
  public static final TypeParameter[] TYPE_PARAMETERS = new TypeParameter[0];
  public static final String TEST_PARAMETER_NAME = "T";

  @Test
  void testTypeDeclarationWithoutProperties() {
    TestTypeDeclaration testTypeDeclaration = new TestTypeDeclaration(TEST_ID, TEST_TYPE_NAME, TEST_DESCRIPTION,
      TYPE_PARAMETERS);
    TypeSpec typeSpec = TypeGenerationUtils.asTypeSpecification(testTypeDeclaration);
    assertThat(typeSpec.javadoc.toString(), containsString(TEST_DESCRIPTION));
    assertThat(typeSpec.name, equalToIgnoringCase(TEST_TYPE_NAME));
    assertThat(typeSpec.hasModifier(Modifier.PUBLIC), equalTo(true));
  }

  @Test
  void testTypeDeclarationWithProperty() {
    TestPropertyType testPropertyType = new TestPropertyType(TEST_ID, TEST_PROPERTY_NAME, TEST_DESCRIPTION,
      TEST_TYPE_NAME);
    TestTypeDeclaration testTypeDeclaration = new TestTypeDeclaration(TEST_ID, TEST_TYPE_NAME, TEST_DESCRIPTION,
      new TypeParameter[0], testPropertyType);
    TypeSpec typeSpec = TypeGenerationUtils.asTypeSpecification(testTypeDeclaration);
    //type spec check
    assertThat(typeSpec.javadoc.toString(), containsString(TEST_DESCRIPTION));
    assertThat(typeSpec.name, equalToIgnoringCase(TEST_TYPE_NAME));
    assertThat(typeSpec.hasModifier(Modifier.PUBLIC), equalTo(true));
    //field spec check
    assertThat(typeSpec.fieldSpecs, hasSize(1));
    assertThat(typeSpec.fieldSpecs, hasItem(hasToString(containsString(String.class.getName()))));
    assertThat(typeSpec.fieldSpecs, hasItem(hasToString(containsString(TEST_DESCRIPTION))));
  }

  @Test
  void testTypeDeclarationWithCustomProperty() {
    TestPropertyType testPropertyType = new TestPropertyType(TEST_ID, TEST_PROPERTY_NAME, TEST_DESCRIPTION,
      TEST_CUSTOM_TYPE_NAME);
    TestTypeDeclaration testTypeDeclaration = new TestTypeDeclaration(TEST_ID, TEST_TYPE_NAME, TEST_DESCRIPTION,
      new TypeParameter[0], testPropertyType);
    TypeSpec typeSpec = TypeGenerationUtils.asTypeSpecification(testTypeDeclaration);
    //type spec check
    assertThat(typeSpec.javadoc.toString(), containsString(TEST_DESCRIPTION));
    assertThat(typeSpec.name, equalToIgnoringCase(TEST_TYPE_NAME));
    assertThat(typeSpec.hasModifier(Modifier.PUBLIC), equalTo(true));
    //field spec check
    assertThat(typeSpec.fieldSpecs, hasSize(1));
    assertThat(typeSpec.fieldSpecs, hasItem(hasToString(containsStringIgnoringCase(TEST_CUSTOM_TYPE_NAME))));
    assertThat(typeSpec.fieldSpecs, hasItem(hasToString(containsString(TEST_DESCRIPTION))));
  }

  @Test
  void testTypeDeclarationWithCustomParametrizedProperty() {
    TestPropertyType testPropertyType = new TestPropertyType(TEST_ID, TEST_PROPERTY_NAME, TEST_DESCRIPTION,
      TEST_CUSTOM_TYPE_NAME);
    TestTypeDeclaration testTypeDeclaration = new TestTypeDeclaration(TEST_ID, TEST_TYPE_NAME, TEST_DESCRIPTION,
      new TypeParameter[]{new TestTypeParameter(TEST_ID, TEST_PARAMETER_NAME, "generic")}, testPropertyType);
    TypeSpec typeSpec = TypeGenerationUtils.asTypeSpecification(testTypeDeclaration);
    //type spec check
    assertThat(typeSpec.typeVariables, hasSize(1));
    assertThat(typeSpec.typeVariables, hasItem(hasToString(containsString(TEST_PARAMETER_NAME))));
    assertThat(typeSpec.javadoc.toString(), containsString(TEST_DESCRIPTION));
    assertThat(typeSpec.name, equalToIgnoringCase(TEST_TYPE_NAME));
    assertThat(typeSpec.hasModifier(Modifier.PUBLIC), equalTo(true));
    //field spec check
    assertThat(typeSpec.fieldSpecs, hasSize(1));
    assertThat(typeSpec.fieldSpecs, hasItem(hasToString(containsStringIgnoringCase(TEST_CUSTOM_TYPE_NAME))));
    assertThat(typeSpec.fieldSpecs, hasItem(hasToString(containsString(TEST_DESCRIPTION))));
  }

  @Test
  void testLoadingStackWithOneCustomType() {
    Map<String, TypeDeclaration> schemaTypeDeclarations = new HashMap<>();
    TestTypeDeclaration testTypeDeclaration = new TestTypeDeclaration(TEST_ID, TEST_CUSTOM_TYPE_NAME, TEST_DESCRIPTION,
      TYPE_PARAMETERS);
    schemaTypeDeclarations.put(TEST_CUSTOM_TYPE_NAME, testTypeDeclaration);
    Stack<TypeDeclaration> typeDeclarationStack = TypeGenerationUtils.buildLoadingStack(schemaTypeDeclarations);

    assertThat(typeDeclarationStack, hasSize(1));
    TypeDeclaration typeDeclaration = typeDeclarationStack.peek();
    assertThat(typeDeclaration.getId(), equalTo(TEST_ID));
    assertThat(typeDeclaration.getName(), equalTo(TEST_CUSTOM_TYPE_NAME));
    assertThat(typeDeclaration.getDescription(), equalTo(TEST_DESCRIPTION));
  }

  @Test
  void testLoadingStackWithOneCustomTypeWithSimpleProperty() {
    Map<String, TypeDeclaration> schemaTypeDeclarations = new HashMap<>();
    TestPropertyType testPropertyType = new TestPropertyType(TEST_ID, TEST_PROPERTY_NAME, TEST_DESCRIPTION,
      TEST_TYPE_NAME);
    TestTypeDeclaration testTypeDeclaration = new TestTypeDeclaration(TEST_ID, TEST_CUSTOM_TYPE_NAME, TEST_DESCRIPTION,
      new TypeParameter[0], testPropertyType);

    schemaTypeDeclarations.put(TEST_CUSTOM_TYPE_NAME, testTypeDeclaration);
    Stack<TypeDeclaration> typeDeclarationStack = TypeGenerationUtils.buildLoadingStack(schemaTypeDeclarations);
    assertThat(typeDeclarationStack, hasSize(1));
    TypeDeclaration typeDeclaration = typeDeclarationStack.peek();
    assertThat(typeDeclaration.getId(), equalTo(TEST_ID));
    assertThat(typeDeclaration.getName(), equalTo(TEST_CUSTOM_TYPE_NAME));
    assertThat(typeDeclaration.getDescription(), equalTo(TEST_DESCRIPTION));
  }

  @Test
  void testLoadingStackWithOneCustomTypeWithRegisteredCustomProperty() {
    Map<String, TypeDeclaration> schemaTypeDeclarations = new HashMap<>();
    TestPropertyType testPropertyType = new TestPropertyType(TEST_ID, TEST_ANOTHER_CUSTOM_TYPE_NAME, TEST_DESCRIPTION,
      TEST_ANOTHER_CUSTOM_TYPE_NAME);
    TestTypeDeclaration testTypeDeclaration = new TestTypeDeclaration(TEST_ID, TEST_CUSTOM_TYPE_NAME, TEST_DESCRIPTION,
      new TypeParameter[0], testPropertyType);
    TestTypeDeclaration testPropertyTypeDeclaration = new TestTypeDeclaration(TEST_ID, TEST_ANOTHER_CUSTOM_TYPE_NAME, TEST_DESCRIPTION, TYPE_PARAMETERS);
    schemaTypeDeclarations.put(TEST_CUSTOM_TYPE_NAME, testTypeDeclaration);
    //register property type
    schemaTypeDeclarations.put(TEST_ANOTHER_CUSTOM_TYPE_NAME, testPropertyTypeDeclaration);

    Stack<TypeDeclaration> typeDeclarationStack = TypeGenerationUtils.buildLoadingStack(schemaTypeDeclarations);
    assertThat(typeDeclarationStack, hasSize(2));
  }

  @Test
  void testLoadingStackWithOneCustomTypeWithUnregisteredCustomProperty() {
    Map<String, TypeDeclaration> schemaTypeDeclarations = new HashMap<>();
    TestPropertyType testPropertyType = new TestPropertyType(TEST_ID, TEST_ANOTHER_CUSTOM_TYPE_NAME, TEST_DESCRIPTION,
      TEST_ANOTHER_CUSTOM_TYPE_NAME);
    TestTypeDeclaration testTypeDeclaration = new TestTypeDeclaration(TEST_ID, TEST_CUSTOM_TYPE_NAME, TEST_DESCRIPTION,
      TYPE_PARAMETERS, testPropertyType);

    schemaTypeDeclarations.put(TEST_CUSTOM_TYPE_NAME, testTypeDeclaration);
    IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
      () -> TypeGenerationUtils.buildLoadingStack(schemaTypeDeclarations));
    String message = illegalArgumentException.getMessage();
    assertThat(message, containsString(TEST_ANOTHER_CUSTOM_TYPE_NAME));
  }

  @Test
  void testLoadingStackWithOneType() {
    Map<String, TypeDeclaration> schemaTypeDeclarations = new HashMap<>();
    TestTypeDeclaration testTypeDeclaration = new TestTypeDeclaration(TEST_ID, TEST_TYPE_NAME, TEST_DESCRIPTION, TYPE_PARAMETERS);
    schemaTypeDeclarations.put(TEST_TYPE_NAME, testTypeDeclaration);
    Stack<TypeDeclaration> typeDeclarationStack = TypeGenerationUtils.buildLoadingStack(schemaTypeDeclarations);
    //Simple type already loaded
    assertThat(typeDeclarationStack, empty());
  }

  //parametrization

  @Test
  void testLoadingStackWithOneCustomParametrizedTypeWithRegisteredCustomProperty() {
    Map<String, TypeDeclaration> schemaTypeDeclarations = new HashMap<>();
    TestPropertyType testPropertyType = new TestPropertyType(TEST_ID, TEST_ANOTHER_CUSTOM_TYPE_NAME, TEST_DESCRIPTION,
      "list of string");
    TestTypeDeclaration testTypeDeclaration = new TestTypeDeclaration(TEST_ID, TEST_CUSTOM_TYPE_NAME, TEST_DESCRIPTION,
      new TypeParameter[]{new TestTypeParameter(TEST_ID, "T", TEST_DESCRIPTION)}, testPropertyType);
    TestTypeDeclaration testPropertyTypeDeclaration = new TestTypeDeclaration(TEST_ID, TEST_ANOTHER_CUSTOM_TYPE_NAME,
      TEST_DESCRIPTION, TYPE_PARAMETERS);
    schemaTypeDeclarations.put(TEST_CUSTOM_TYPE_NAME, testTypeDeclaration);
    //register property type
    schemaTypeDeclarations.put(TEST_ANOTHER_CUSTOM_TYPE_NAME, testPropertyTypeDeclaration);

    Stack<TypeDeclaration> typeDeclarationStack = TypeGenerationUtils.buildLoadingStack(schemaTypeDeclarations);
    assertThat(typeDeclarationStack, hasSize(2));

  }

  //fix with recursive parameters scan
  @Test
  void testLoadingStackWithOneCustomIncorrectParametrizedTypeWithRegisteredCustomProperty() {
    Map<String, TypeDeclaration> schemaTypeDeclarations = new HashMap<>();
    String multipleParametrizationTypeName = "list of list of string";
    TestPropertyType testPropertyType = new TestPropertyType(TEST_ID, TEST_ANOTHER_CUSTOM_TYPE_NAME, TEST_DESCRIPTION,
      multipleParametrizationTypeName);
    TestTypeDeclaration testTypeDeclaration = new TestTypeDeclaration(TEST_ID, TEST_CUSTOM_TYPE_NAME, TEST_DESCRIPTION,
      new TypeParameter[]{new TestTypeParameter(TEST_ID, "T", TEST_DESCRIPTION)}, testPropertyType);
    TestTypeDeclaration testPropertyTypeDeclaration = new TestTypeDeclaration(TEST_ID, TEST_ANOTHER_CUSTOM_TYPE_NAME,
      TEST_DESCRIPTION, TYPE_PARAMETERS);
    schemaTypeDeclarations.put(TEST_CUSTOM_TYPE_NAME, testTypeDeclaration);
    //register property type
    schemaTypeDeclarations.put(TEST_ANOTHER_CUSTOM_TYPE_NAME, testPropertyTypeDeclaration);

    UnsupportedOperationException unsupportedOperationException = assertThrows(UnsupportedOperationException.class,
      () -> TypeGenerationUtils.buildLoadingStack(schemaTypeDeclarations));
    assertThat(unsupportedOperationException.getMessage(), containsString(multipleParametrizationTypeName));
  }

  @Test
  void testLoadingStackWithOneCustomIncorrectParametrizedTypeWithUnregisteredCustomProperty() {
    Map<String, TypeDeclaration> schemaTypeDeclarations = new HashMap<>();
    TestPropertyType testPropertyType = new TestPropertyType(TEST_ID, TEST_ANOTHER_CUSTOM_TYPE_NAME, TEST_DESCRIPTION,
      "list of " + TEST_ANOTHER_CUSTOM_TYPE_NAME);
    TestTypeDeclaration testTypeDeclaration = new TestTypeDeclaration(TEST_ID, TEST_CUSTOM_TYPE_NAME, TEST_DESCRIPTION,
      TYPE_PARAMETERS, testPropertyType);

    schemaTypeDeclarations.put(TEST_CUSTOM_TYPE_NAME, testTypeDeclaration);
    IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
      () -> TypeGenerationUtils.buildLoadingStack(schemaTypeDeclarations));
    String message = illegalArgumentException.getMessage();
    assertThat(message, containsString(TEST_ANOTHER_CUSTOM_TYPE_NAME));
  }

  @Test
  void testLoadingStackWithOneCustomCorrectlyParametrizedTypeWithRegisteredCustomProperty() {
    Map<String, TypeDeclaration> schemaTypeDeclarations = new HashMap<>();
    TestPropertyType testPropertyType = new TestPropertyType(TEST_ID, TEST_CUSTOM_TYPE_NAME, TEST_DESCRIPTION,
      "list of " + TEST_CUSTOM_TYPE_NAME);
    TestTypeDeclaration testTypeDeclaration = new TestTypeDeclaration(TEST_ID, TEST_CUSTOM_TYPE_NAME, TEST_DESCRIPTION,
      TYPE_PARAMETERS, testPropertyType);
    schemaTypeDeclarations.put(TEST_CUSTOM_TYPE_NAME, testTypeDeclaration);
    Stack<TypeDeclaration> typeDeclarations = TypeGenerationUtils.buildLoadingStack(schemaTypeDeclarations);
    assertThat(typeDeclarations, hasSize(1));
  }
}
