package com.github.gibmir.ion.maven.plugin.type;

import com.github.gibmir.ion.api.schema.type.TypeDeclaration;
import com.github.gibmir.ion.api.schema.type.TypeParameter;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import org.junit.jupiter.api.Test;

import javax.lang.model.element.Modifier;
import java.util.HashMap;
import java.util.List;
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
  void testCorrectTypeLoadingStack() {
//    String typeString = "a<b,c<d,e>,f<g,k<l>>>";
    String typeString = "map<string,list<list<number>>>";

    ParametrizedTypeTree parametrizedTypeTree = ParametrizedTypeTree.from(typeString);

    Stack<String> typeLoadingStack = parametrizedTypeTree.buildTypeLoadingStack();
    //for each type declaration
    assertThat(typeLoadingStack, hasSize(5));
    assertThat(typeLoadingStack, hasItem(equalToIgnoringCase("list")));
    assertThat(typeLoadingStack, hasItem(equalToIgnoringCase("map")));
    assertThat(typeLoadingStack, hasItem(equalToIgnoringCase("number")));
    assertThat(typeLoadingStack, hasItem(equalToIgnoringCase("string")));
  }

  @Test
  void testDifficultTypeLoadingStack() {
    String typeString = "a<b,c<d,e>,f<g,k<l>>>";

    ParametrizedTypeTree parametrizedTypeTree = ParametrizedTypeTree.from(typeString);

    Stack<String> typeLoadingStack = parametrizedTypeTree.buildTypeLoadingStack();
    //for each type declaration
    assertThat(typeLoadingStack, hasSize(9));
    assertThat(typeLoadingStack, hasItem(equalToIgnoringCase("a")));
    assertThat(typeLoadingStack, hasItem(equalToIgnoringCase("b")));
    assertThat(typeLoadingStack, hasItem(equalToIgnoringCase("c")));
    assertThat(typeLoadingStack, hasItem(equalToIgnoringCase("d")));
    assertThat(typeLoadingStack, hasItem(equalToIgnoringCase("e")));
    assertThat(typeLoadingStack, hasItem(equalToIgnoringCase("f")));
    assertThat(typeLoadingStack, hasItem(equalToIgnoringCase("g")));
    assertThat(typeLoadingStack, hasItem(equalToIgnoringCase("k")));
    assertThat(typeLoadingStack, hasItem(equalToIgnoringCase("l")));
  }

  @Test
  void testBuildTypeName() {
    String typeString = "map<string,list<list<number>>>";

    ParametrizedTypeTree parametrizedTypeTree = ParametrizedTypeTree.from(typeString);
    ParameterizedTypeName parameterizedTypeName = parametrizedTypeTree.buildTypeName();
    //map<...>
    assertThat(parameterizedTypeName.rawType, hasToString(containsString(Map.class.getName())));
    //<string,list<...>>
    assertThat(parameterizedTypeName.typeArguments, hasSize(2));
    assertThat(parameterizedTypeName.typeArguments, hasItem(hasToString(containsString(String.class.getName()))));
    assertThat(parameterizedTypeName.typeArguments, hasItem(hasToString(containsString(List.class.getName()))));
  }

  @Test
  void testDifficultTypeBuild() {
    String typeString = "a<b,c<d,e>,f<g,k<l>>>";

    ParametrizedTypeTree parametrizedTypeTree = ParametrizedTypeTree.from(typeString);
    //can't get type for -e-
    assertThrows(IllegalArgumentException.class, parametrizedTypeTree::buildTypeName);
  }

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
      "list<string>");
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

  @Test
  void testLoadingStackWithMultipleParametrization() {
    Map<String, TypeDeclaration> schemaTypeDeclarations = new HashMap<>();
    String multipleParametrizationTypeName = "list<list<string>>";
    TestPropertyType testPropertyType = new TestPropertyType(TEST_ID, TEST_ANOTHER_CUSTOM_TYPE_NAME, TEST_DESCRIPTION,
      multipleParametrizationTypeName);
    TestTypeDeclaration testTypeDeclaration = new TestTypeDeclaration(TEST_ID, TEST_CUSTOM_TYPE_NAME, TEST_DESCRIPTION,
      new TypeParameter[]{new TestTypeParameter(TEST_ID, "T", TEST_DESCRIPTION)}, testPropertyType);
    TestTypeDeclaration testPropertyTypeDeclaration = new TestTypeDeclaration(TEST_ID, TEST_ANOTHER_CUSTOM_TYPE_NAME,
      TEST_DESCRIPTION, TYPE_PARAMETERS);
    schemaTypeDeclarations.put(TEST_CUSTOM_TYPE_NAME, testTypeDeclaration);
    //register property type
    schemaTypeDeclarations.put(TEST_ANOTHER_CUSTOM_TYPE_NAME, testPropertyTypeDeclaration);

    Stack<TypeDeclaration> typeDeclarations = TypeGenerationUtils.buildLoadingStack(schemaTypeDeclarations);
    assertThat(typeDeclarations, hasSize(2));
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
      "list<" + TEST_CUSTOM_TYPE_NAME + '>');
    TestTypeDeclaration testTypeDeclaration = new TestTypeDeclaration(TEST_ID, TEST_CUSTOM_TYPE_NAME, TEST_DESCRIPTION,
      TYPE_PARAMETERS, testPropertyType);
    schemaTypeDeclarations.put(TEST_CUSTOM_TYPE_NAME, testTypeDeclaration);
    Stack<TypeDeclaration> typeDeclarations = TypeGenerationUtils.buildLoadingStack(schemaTypeDeclarations);
    assertThat(typeDeclarations, hasSize(1));
  }
}
