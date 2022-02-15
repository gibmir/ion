package com.github.gibmir.ion.maven.plugin.reader;

import com.github.gibmir.ion.api.schema.type.TypeDeclaration;
import org.junit.jupiter.api.Test;

import javax.json.JsonObject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

/**
 * Please checkout test/resources json files.
 */
class IonSchemaReaderTest {

  public static final String TEST_PARAMETRIZED_TYPE_NAME = "T";
  public static final String TEST_PARAMETRIZED_MAP_TYPE_NAME = "map<" + TEST_PARAMETRIZED_TYPE_NAME + "," + TEST_PARAMETRIZED_TYPE_NAME + ">";
  public static final String TEST_PARAMETRIZED_LIST_TYPE_NAME = "list<" + TEST_PARAMETRIZED_TYPE_NAME + ">";
  public static final String TEST_TYPE_NAME = "test" + TEST_PARAMETRIZED_TYPE_NAME + "ype";
  public static final String TEST_TYPE_DESCRIPTION = "type for testing";
  public static final String TEST_TYPE_PROPERTY_NAME = "test" + TEST_PARAMETRIZED_TYPE_NAME + "ypeProperty";
  public static final String TEST_TYPE_PROPERTY_DESCRIPTION = "property for test type";
  public static final String TEST_TYPE_PROPERTY_TYPE = "string";
  public static final String TEST_PARAMETRIZED_TYPE_DESCRIPTION = "parametrized parameter type";

  @Test
  void testReadTypeWithoutParametrizationWithoutProperties() {
    Jsonb jsonb = JsonbBuilder.newBuilder().build();
    JsonObject schema = jsonb.fromJson("{\n" +
      "  \"types\": {\n" +
      "    \"testType\": {\n" +
      "      \"description\": \"type for testing\"\n" +
      "    }\n" +
      "  }\n" +
      "}", JsonObject.class);
    Map<String, TypeDeclaration> typesDeclaration = IonSchemaReader.readTypes(schema);
    TypeDeclaration typeDeclaration = typesDeclaration.get(TEST_TYPE_NAME);
    assertThat(typeDeclaration, not(nullValue()));
    assertThat(typeDeclaration.getPropertyTypes(), arrayWithSize(0));
    assertThat(typeDeclaration.getParameters(), arrayWithSize(0));
  }

  @Test
  void testReadTypeWithoutParametrizationWithSingleSimpleProperty() {
    Jsonb jsonb = JsonbBuilder.newBuilder().build();
    JsonObject schema = jsonb.fromJson("{\n" +
      "  \"types\": {\n" +
      "    \"" + TEST_TYPE_NAME + "\": {\n" +
      "      \"description\": \"" + TEST_TYPE_DESCRIPTION + "\",\n" +
      "      \"properties\": {\n" +
      "        \"" + TEST_TYPE_PROPERTY_NAME + "\": {\n" +
      "          \"type\": \"" + TEST_TYPE_PROPERTY_TYPE + "\",\n" +
      "          \"description\": \"" + TEST_TYPE_PROPERTY_DESCRIPTION + "\"\n" +
      "        }\n" +
      "      }\n" +
      "    }\n" +
      "  }\n" +
      "}", JsonObject.class);
    Map<String, TypeDeclaration> typesDeclaration = IonSchemaReader.readTypes(schema);
    TypeDeclaration typeDeclaration = typesDeclaration.get(TEST_TYPE_NAME);
    assertThat(typeDeclaration, not(nullValue()));
    assertThat(typeDeclaration.getPropertyTypes(), arrayWithSize(1));
    assertThat(typeDeclaration.getPropertyTypes()[0].getTypeName(), equalTo(TEST_TYPE_PROPERTY_TYPE));
    assertThat(typeDeclaration.getPropertyTypes()[0].getDescription(), equalTo(TEST_TYPE_PROPERTY_DESCRIPTION));
    assertThat(typeDeclaration.getPropertyTypes()[0].getName(), equalTo(TEST_TYPE_PROPERTY_NAME));
    assertThat(typeDeclaration.getPropertyTypes()[0].getId(), equalTo(IonSchemaReader.DEFAULT_ID));
    assertThat(typeDeclaration.getParameters(), arrayWithSize(0));
  }

  @Test
  void testReadTypeWithoutParametrizationWithSingleParametrizedProperty() {
    Jsonb jsonb = JsonbBuilder.newBuilder().build();
    JsonObject schema = jsonb.fromJson("{\n" +
      "  \"types\": {\n" +
      "    \"" + TEST_TYPE_NAME + "\": {\n" +
      "      \"description\": \"" + TEST_TYPE_DESCRIPTION + "\",\n" +
      "      \"parametrization\": {\n" +
      "        \"" + TEST_PARAMETRIZED_TYPE_NAME + "\": {\n" +
      "          \"description\": \"" + TEST_PARAMETRIZED_TYPE_DESCRIPTION + "\"\n" +
      "        }\n" +
      "      },\n" +
      "      \"properties\": {\n" +
      "        \"" + TEST_TYPE_PROPERTY_NAME + "\": {\n" +
      "          \"type\": \"" + TEST_PARAMETRIZED_TYPE_NAME + "\",\n" +
      "          \"description\": \"" + TEST_TYPE_PROPERTY_DESCRIPTION + "\"\n" +
      "        }\n" +
      "      }\n" +
      "    }\n" +
      "  }\n" +
      "}", JsonObject.class);
    Map<String, TypeDeclaration> typesDeclaration = IonSchemaReader.readTypes(schema);
    TypeDeclaration typeDeclaration = typesDeclaration.get(TEST_TYPE_NAME);
    assertThat(typeDeclaration, not(nullValue()));
    // parametrization block check
    assertThat(typeDeclaration.getParameters(), arrayWithSize(1));
    assertThat(typeDeclaration.getParameters()[0].getName(), equalTo(TEST_PARAMETRIZED_TYPE_NAME));
    assertThat(typeDeclaration.getParameters()[0].getDescription(), equalTo(TEST_PARAMETRIZED_TYPE_DESCRIPTION));
    assertThat(typeDeclaration.getParameters()[0].getId(), equalTo(IonSchemaReader.DEFAULT_ID));

    // properties description block check
    assertThat(typeDeclaration.getPropertyTypes(), arrayWithSize(1));
    assertThat(typeDeclaration.getPropertyTypes()[0].getTypeName(), equalTo(TEST_PARAMETRIZED_TYPE_NAME));
    assertThat(typeDeclaration.getPropertyTypes()[0].getDescription(), equalTo(TEST_TYPE_PROPERTY_DESCRIPTION));
    assertThat(typeDeclaration.getPropertyTypes()[0].getName(), equalTo(TEST_TYPE_PROPERTY_NAME));
    assertThat(typeDeclaration.getPropertyTypes()[0].getId(), equalTo(IonSchemaReader.DEFAULT_ID));
  }

  @Test
  void testReadTypeWithoutParametrizationWithSingleParametrizedListProperty() {
    Jsonb jsonb = JsonbBuilder.newBuilder().build();
    JsonObject schema = jsonb.fromJson("{\n" +
      "  \"types\": {\n" +
      "    \"" + TEST_TYPE_NAME + "\": {\n" +
      "      \"description\": \"" + TEST_TYPE_DESCRIPTION + "\",\n" +
      "      \"parametrization\": {\n" +
      "        \"" + TEST_PARAMETRIZED_TYPE_NAME + "\": {\n" +
      "          \"description\": \"" + TEST_PARAMETRIZED_TYPE_DESCRIPTION + "\"\n" +
      "        }\n" +
      "      },\n" +
      "      \"properties\": {\n" +
      "        \"" + TEST_TYPE_PROPERTY_NAME + "\": {\n" +
      "          \"type\": \"" + TEST_PARAMETRIZED_LIST_TYPE_NAME + "\",\n" +
      "          \"description\": \"" + TEST_TYPE_PROPERTY_DESCRIPTION + "\"\n" +
      "        }\n" +
      "      }\n" +
      "    }\n" +
      "  }\n" +
      "}", JsonObject.class);
    Map<String, TypeDeclaration> typesDeclaration = IonSchemaReader.readTypes(schema);
    TypeDeclaration typeDeclaration = typesDeclaration.get(TEST_TYPE_NAME);
    assertThat(typeDeclaration, not(nullValue()));
    // parametrization block check
    assertThat(typeDeclaration.getParameters(), arrayWithSize(1));
    assertThat(typeDeclaration.getParameters()[0].getName(), equalTo(TEST_PARAMETRIZED_TYPE_NAME));
    assertThat(typeDeclaration.getParameters()[0].getDescription(), equalTo(TEST_PARAMETRIZED_TYPE_DESCRIPTION));
    assertThat(typeDeclaration.getParameters()[0].getId(), equalTo(IonSchemaReader.DEFAULT_ID));

    // properties description block check
    assertThat(typeDeclaration.getPropertyTypes(), arrayWithSize(1));
    assertThat(typeDeclaration.getPropertyTypes()[0].getTypeName(), equalTo(TEST_PARAMETRIZED_LIST_TYPE_NAME));
    assertThat(typeDeclaration.getPropertyTypes()[0].getDescription(), equalTo(TEST_TYPE_PROPERTY_DESCRIPTION));
    assertThat(typeDeclaration.getPropertyTypes()[0].getName(), equalTo(TEST_TYPE_PROPERTY_NAME));
    assertThat(typeDeclaration.getPropertyTypes()[0].getId(), equalTo(IonSchemaReader.DEFAULT_ID));
  }

  @Test
  void testReadTypeWithoutParametrizationWithSingleParametrizedMapProperty() {
    Jsonb jsonb = JsonbBuilder.newBuilder().build();
    JsonObject schema = jsonb.fromJson("{\n" +
      "  \"types\": {\n" +
      "    \"" + TEST_TYPE_NAME + "\": {\n" +
      "      \"description\": \"" + TEST_TYPE_DESCRIPTION + "\",\n" +
      "      \"parametrization\": {\n" +
      "        \"" + TEST_PARAMETRIZED_TYPE_NAME + "\": {\n" +
      "          \"description\": \"" + TEST_PARAMETRIZED_TYPE_DESCRIPTION + "\"\n" +
      "        }\n" +
      "      },\n" +
      "      \"properties\": {\n" +
      "        \"" + TEST_TYPE_NAME + "Property\": {\n" +
      "          \"type\": \"" + TEST_PARAMETRIZED_MAP_TYPE_NAME + "\",\n" +
      "          \"description\": \"" + TEST_TYPE_PROPERTY_DESCRIPTION + "\"\n" +
      "        }\n" +
      "      }\n" +
      "    }\n" +
      "  }\n" +
      "}", JsonObject.class);

    Map<String, TypeDeclaration> typesDeclaration = IonSchemaReader.readTypes(schema);
    TypeDeclaration typeDeclaration = typesDeclaration.get(TEST_TYPE_NAME);
    assertThat(typeDeclaration, not(nullValue()));
    // parametrization block check
    assertThat(typeDeclaration.getParameters(), arrayWithSize(1));
    assertThat(typeDeclaration.getParameters()[0].getName(), equalTo(TEST_PARAMETRIZED_TYPE_NAME));
    assertThat(typeDeclaration.getParameters()[0].getDescription(), equalTo(TEST_PARAMETRIZED_TYPE_DESCRIPTION));
    assertThat(typeDeclaration.getParameters()[0].getId(), equalTo(IonSchemaReader.DEFAULT_ID));

    // properties description block check
    assertThat(typeDeclaration.getPropertyTypes(), arrayWithSize(1));
    assertThat(typeDeclaration.getPropertyTypes()[0].getTypeName(), equalTo(TEST_PARAMETRIZED_MAP_TYPE_NAME));
    assertThat(typeDeclaration.getPropertyTypes()[0].getDescription(), equalTo(TEST_TYPE_PROPERTY_DESCRIPTION));
    assertThat(typeDeclaration.getPropertyTypes()[0].getName(), equalTo(TEST_TYPE_PROPERTY_NAME));
    assertThat(typeDeclaration.getPropertyTypes()[0].getId(), equalTo(IonSchemaReader.DEFAULT_ID));
  }

  @Test
  void testReadProcedures() {

  }
}
