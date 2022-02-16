package com.github.gibmir.ion.maven.plugin.reader;

import com.github.gibmir.ion.api.schema.procedure.Procedure;
import com.github.gibmir.ion.api.schema.type.TypeDeclaration;
import org.junit.jupiter.api.Test;

import javax.json.JsonObject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.emptyArray;
import static org.hamcrest.Matchers.emptyCollectionOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Please checkout test/resources json files.
 */
class IonSchemaReaderTest {
  // types
  public static final String TEST_PARAMETRIZED_TYPE_NAME = "T";
  public static final String TEST_PARAMETRIZED_MAP_TYPE_NAME = "map<" + TEST_PARAMETRIZED_TYPE_NAME + "," + TEST_PARAMETRIZED_TYPE_NAME + ">";
  public static final String TEST_PARAMETRIZED_LIST_TYPE_NAME = "list<" + TEST_PARAMETRIZED_TYPE_NAME + ">";
  public static final String TEST_TYPE_NAME = "test" + TEST_PARAMETRIZED_TYPE_NAME + "ype";
  public static final String TEST_TYPE_DESCRIPTION = "type for testing";
  public static final String TEST_TYPE_PROPERTY_NAME = "test" + TEST_PARAMETRIZED_TYPE_NAME + "ypeProperty";
  public static final String TEST_TYPE_PROPERTY_DESCRIPTION = "property for test type";
  public static final String TEST_TYPE_PROPERTY_TYPE = "string";
  public static final String TEST_PARAMETRIZED_TYPE_DESCRIPTION = "parametrized parameter type";
  // procedures
  public static final String TEST_PROCEDURE_NAME = "testProcedure";
  public static final String TEST_PROCEDURE_DESCRIPTION = "This is test procedure";
  public static final String TEST_PROCEDURE_ARGUMENT_NAME = "testArgument";
  public static final String TEST_PROCEDURE_ARGUMENT_TYPE = "string";
  public static final String TEST_PROCEDURE_GENERIC_ARGUMENT_TYPE = "testType<string>";
  public static final String TEST_PROCEDURE_ARGUMENT_DESCRIPTION = "test argument";
  public static final String TEST_RETURN_ARGUMENT_DESCRIPTION = "test return argument";
  public static final String TEST_RETURN_ARGUMENT_TYPE = "test return argument";

  // types
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

  // procedures
  @Test
  void testReadProcedureEmptyJson() {
    Jsonb jsonb = JsonbBuilder.newBuilder().build();
    JsonObject schema = jsonb.fromJson("{}", JsonObject.class);
    List<Procedure> procedures = IonSchemaReader.readProcedures(schema);
    assertThat(procedures, emptyCollectionOf(Procedure.class));
  }

  @Test
  void testReadProcedureSimpleArgumentSimpleReturnArgument() {
    Jsonb jsonb = JsonbBuilder.newBuilder().build();
    JsonObject schema = jsonb.fromJson("{\n" +
      "  \"procedures\": {\n" +
      "    \"" + TEST_PROCEDURE_NAME + "\": {\n" +
      "      \"description\": \"" + TEST_PROCEDURE_DESCRIPTION + "\",\n" +
      "      \"arguments\": {\n" +
      "        \"" + TEST_PROCEDURE_ARGUMENT_NAME + "\": {\n" +
      "          \"type\": \"" + TEST_PROCEDURE_ARGUMENT_TYPE + "\",\n" +
      "          \"description\": \"" + TEST_PROCEDURE_ARGUMENT_DESCRIPTION + "\"\n" +
      "        }\n" +
      "      },\n" +
      "      \"return\": {\n" +
      "        \"type\": \"" + TEST_RETURN_ARGUMENT_TYPE + "\",\n" +
      "        \"description\": \"" + TEST_RETURN_ARGUMENT_DESCRIPTION + "\"\n" +
      "      }\n" +
      "    }\n" +
      "  }\n" +
      "}", JsonObject.class);
    List<Procedure> procedures = IonSchemaReader.readProcedures(schema);
    assertThat(procedures, hasSize(1));
    assertThat(procedures.get(0).getId(), equalTo(IonSchemaReader.DEFAULT_ID));
    assertThat(procedures.get(0).getName(), equalTo(TEST_PROCEDURE_NAME));
    assertThat(procedures.get(0).getDescription(), equalTo(TEST_PROCEDURE_DESCRIPTION));

    assertThat(procedures.get(0).getArgumentTypes()[0].getId(), equalTo(IonSchemaReader.DEFAULT_ID));
    assertThat(procedures.get(0).getArgumentTypes()[0].getName(), equalTo(TEST_PROCEDURE_ARGUMENT_NAME));
    assertThat(procedures.get(0).getArgumentTypes()[0].getTypeName(), equalTo(TEST_PROCEDURE_ARGUMENT_TYPE));
    assertThat(procedures.get(0).getArgumentTypes()[0].getDescription(), equalTo(TEST_PROCEDURE_ARGUMENT_DESCRIPTION));

    assertThat(procedures.get(0).getReturnArgumentType().getId(), equalTo(IonSchemaReader.DEFAULT_ID));
    assertThat(procedures.get(0).getReturnArgumentType().getName(), equalTo(IonSchemaReader.RETURN_TYPE_KEY));
    assertThat(procedures.get(0).getReturnArgumentType().getTypeName(), equalTo(TEST_RETURN_ARGUMENT_TYPE));
    assertThat(procedures.get(0).getReturnArgumentType().getDescription(), equalTo(TEST_RETURN_ARGUMENT_DESCRIPTION));
  }

  @Test
  void testReadProcedureGenericArgumentSimpleReturnArgument() {
    Jsonb jsonb = JsonbBuilder.newBuilder().build();
    JsonObject schema = jsonb.fromJson("{\n" +
      "  \"procedures\": {\n" +
      "    \"" + TEST_PROCEDURE_NAME + "\": {\n" +
      "      \"description\": \"" + TEST_PROCEDURE_DESCRIPTION + "\",\n" +
      "      \"arguments\": {\n" +
      "        \"" + TEST_PROCEDURE_ARGUMENT_NAME + "\": {\n" +
      "          \"type\": \"" + TEST_PROCEDURE_GENERIC_ARGUMENT_TYPE + "\",\n" +
      "          \"description\": \"" + TEST_PROCEDURE_ARGUMENT_DESCRIPTION + "\"\n" +
      "        }\n" +
      "      },\n" +
      "      \"return\": {\n" +
      "        \"type\": \"" + TEST_RETURN_ARGUMENT_TYPE + "\",\n" +
      "        \"description\": \"" + TEST_RETURN_ARGUMENT_DESCRIPTION + "\"\n" +
      "      }\n" +
      "    }\n" +
      "  }\n" +
      "}", JsonObject.class);
    List<Procedure> procedures = IonSchemaReader.readProcedures(schema);
    assertThat(procedures, hasSize(1));
    assertThat(procedures.get(0).getId(), equalTo(IonSchemaReader.DEFAULT_ID));
    assertThat(procedures.get(0).getName(), equalTo(TEST_PROCEDURE_NAME));
    assertThat(procedures.get(0).getDescription(), equalTo(TEST_PROCEDURE_DESCRIPTION));

    assertThat(procedures.get(0).getArgumentTypes()[0].getId(), equalTo(IonSchemaReader.DEFAULT_ID));
    assertThat(procedures.get(0).getArgumentTypes()[0].getName(), equalTo(TEST_PROCEDURE_ARGUMENT_NAME));
    assertThat(procedures.get(0).getArgumentTypes()[0].getTypeName(), equalTo(TEST_PROCEDURE_GENERIC_ARGUMENT_TYPE));
    assertThat(procedures.get(0).getArgumentTypes()[0].getDescription(), equalTo(TEST_PROCEDURE_ARGUMENT_DESCRIPTION));

    assertThat(procedures.get(0).getReturnArgumentType().getId(), equalTo(IonSchemaReader.DEFAULT_ID));
    assertThat(procedures.get(0).getReturnArgumentType().getName(), equalTo(IonSchemaReader.RETURN_TYPE_KEY));
    assertThat(procedures.get(0).getReturnArgumentType().getTypeName(), equalTo(TEST_RETURN_ARGUMENT_TYPE));
    assertThat(procedures.get(0).getReturnArgumentType().getDescription(), equalTo(TEST_RETURN_ARGUMENT_DESCRIPTION));
  }

  @Test
  void testReadProcedureGenericArgumentWithoutReturnArgument() {
    Jsonb jsonb = JsonbBuilder.newBuilder().build();
    JsonObject schema = jsonb.fromJson("{\n" +
      "  \"procedures\": {\n" +
      "    \"" + TEST_PROCEDURE_NAME + "\": {\n" +
      "      \"description\": \"" + TEST_PROCEDURE_DESCRIPTION + "\",\n" +
      "      \"arguments\": {\n" +
      "        \"" + TEST_PROCEDURE_ARGUMENT_NAME + "\": {\n" +
      "          \"type\": \"" + TEST_PROCEDURE_GENERIC_ARGUMENT_TYPE + "\",\n" +
      "          \"description\": \"" + TEST_PROCEDURE_ARGUMENT_DESCRIPTION + "\"\n" +
      "        }\n" +
      "      }\n" +
      "    }\n" +
      "  }\n" +
      "}", JsonObject.class);
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
      () -> IonSchemaReader.readProcedures(schema));
    assertThat(exception.getMessage(), containsString(TEST_PROCEDURE_NAME));
  }

  @Test
  void testReadProcedureWithoutArgumentWithSimpleReturnArgument() {
    Jsonb jsonb = JsonbBuilder.newBuilder().build();
    JsonObject schema = jsonb.fromJson("{\n" +
      "  \"procedures\": {\n" +
      "    \"" + TEST_PROCEDURE_NAME + "\": {\n" +
      "      \"description\": \"" + TEST_PROCEDURE_DESCRIPTION + "\",\n" +
      "      \"return\": {\n" +
      "        \"type\": \"" + TEST_RETURN_ARGUMENT_TYPE + "\",\n" +
      "        \"description\": \"" + TEST_RETURN_ARGUMENT_DESCRIPTION + "\"\n" +
      "      }\n" +
      "    }\n" +
      "  }\n" +
      "}", JsonObject.class);
    List<Procedure> procedures = IonSchemaReader.readProcedures(schema);
    assertThat(procedures, hasSize(1));
    assertThat(procedures.get(0).getId(), equalTo(IonSchemaReader.DEFAULT_ID));
    assertThat(procedures.get(0).getName(), equalTo(TEST_PROCEDURE_NAME));
    assertThat(procedures.get(0).getDescription(), equalTo(TEST_PROCEDURE_DESCRIPTION));

    assertThat(procedures.get(0).getArgumentTypes(), emptyArray());

    assertThat(procedures.get(0).getReturnArgumentType().getId(), equalTo(IonSchemaReader.DEFAULT_ID));
    assertThat(procedures.get(0).getReturnArgumentType().getName(), equalTo(IonSchemaReader.RETURN_TYPE_KEY));
    assertThat(procedures.get(0).getReturnArgumentType().getTypeName(), equalTo(TEST_RETURN_ARGUMENT_TYPE));
    assertThat(procedures.get(0).getReturnArgumentType().getDescription(), equalTo(TEST_RETURN_ARGUMENT_DESCRIPTION));
  }
}
