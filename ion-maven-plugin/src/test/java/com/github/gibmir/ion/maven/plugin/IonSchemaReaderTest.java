package com.github.gibmir.ion.maven.plugin;

import org.junit.jupiter.api.Test;

import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.json.bind.Jsonb;
import javax.json.bind.spi.JsonbProvider;

class IonSchemaReaderTest {

  public static final String SCHEMA_JSON = "{\n" +
    "  \"types\": {\n" +
    "    \"simpleTypeName\": {\n" +
    "      \"description\": \"simple type description\",\n" +
    "      \"properties\": {\n" +
    "        \"simpleProperty\": {\n" +
    "          \"type\": \"string\",\n" +
    "          \"description\": \"simpleStringProperty\"\n" +
    "        }\n" +
    "      }\n" +
    "    },\n" +
    "    \"composedTypeName\": {\n" +
    "      \"description\": \"some type description\",\n" +
    "      \"properties\": {\n" +
    "        \"propertyName\": {\n" +
    "          \"type\": \"integer\",\n" +
    "          \"description\": \"property description\"\n" +
    "        },\n" +
    "        \"otherHardProperty\": {\n" +
    "          \"type\": \"simpleTypeName\",\n" +
    "          \"description\": \"simple type property\"\n" +
    "        }\n" +
    "      }\n" +
    "    }\n" +
    "  },\n" +
    "  \"testService\": {\n" +
    "    \"description\": \"Service is a set of procedures\",\n" +
    "    \"procedures\": {\n" +
    "      \"testProcedureName\": {\n" +
    "        \"description\": \"This is test procedure\",\n" +
    "        \"arguments\": {\n" +
    "          \"testArgumentName\": {\n" +
    "            \"type\": \"id\",\n" +
    "            \"description\": \"test argument description\"\n" +
    "          }\n" +
    "        },\n" +
    "        \"return\": {\n" +
    "          \"type\": \"string\",\n" +
    "          \"description\": \"test return argument\"\n" +
    "        }\n" +
    "      }\n" +
    "    }\n" +
    "  }\n" +
    "}";

  @Test
  void testRead() {
    Jsonb jsonb = JsonbProvider.provider().create().build();

    JsonObject schemaJsonStructure = jsonb.fromJson(SCHEMA_JSON, JsonObject.class);
    JsonValue types = schemaJsonStructure.get("types");
    System.out.println(types.toString());
  }
}
