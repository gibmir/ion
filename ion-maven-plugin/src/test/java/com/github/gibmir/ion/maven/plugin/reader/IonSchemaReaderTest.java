package com.github.gibmir.ion.maven.plugin.reader;

import com.github.gibmir.ion.api.schema.service.Service;
import com.github.gibmir.ion.api.schema.type.TypeDeclaration;
import org.junit.jupiter.api.Test;

import javax.json.JsonValue;
import javax.json.bind.Jsonb;
import javax.json.bind.spi.JsonbProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class IonSchemaReaderTest {

  public static final String SCHEMA_JSON = "{\n" +
    "  \"types\": {\n" +
    "    \"testType\": {\n" +
    "      \"description\": \"Type for testing.\",\n" +
    "      \"properties\": {\n" +
    "        \"testTypeProperty\": {\n" +
    "          \"type\": \"string\",\n" +
    "          \"description\": \"property for test type\"\n" +
    "        }\n" +
    "      }\n" +
    "    },\n" +
    "    \"testComposedType\": {\n" +
    "      \"description\": \"composed type\",\n" +
    "      \"properties\": {\n" +
    "        \"numericProperty\": {\n" +
    "          \"type\": \"number\",\n" +
    "          \"description\": \"numeric property\"\n" +
    "        },\n" +
    "        \"typedProperty\": {\n" +
    "          \"type\": \"testType\",\n" +
    "          \"description\": \"test type property\"\n" +
    "        }\n" +
    "      }\n" +
    "    }\n" +
    "  },\n" +
    "  \"services\": {\n" +
    "    \"testService\": {\n" +
    "      \"description\": \"Service is a set of procedures\",\n" +
    "      \"procedures\": {\n" +
    "        \"testProcedure\": {\n" +
    "          \"description\": \"This is test procedure\",\n" +
    "          \"arguments\": {\n" +
    "            \"testArgument\": {\n" +
    "              \"type\": \"testType\",\n" +
    "              \"description\": \"test argument argument\"\n" +
    "            },\n" +
    "            \"testComposedArgument\": {\n" +
    "              \"type\": \"testComposedType\",\n" +
    "              \"description\": \"test composed argument\"\n" +
    "            }\n" +
    "          },\n" +
    "          \"return\": {\n" +
    "            \"type\": \"string\",\n" +
    "            \"description\": \"test return argument\"\n" +
    "          }\n" +
    "        },\n" +
    "        \"otherTestProcedure\": {\n" +
    "          \"description\": \"This is test procedure\",\n" +
    "          \"arguments\": {\n" +
    "            \"testArgument\": {\n" +
    "              \"type\": \"testType\",\n" +
    "              \"description\": \"test argument argument\"\n" +
    "            }\n" +
    "          },\n" +
    "          \"return\": {\n" +
    "            \"type\": \"string\",\n" +
    "            \"description\": \"test return argument\"\n" +
    "          }\n" +
    "        }\n" +
    "      }\n" +
    "    }\n" +
    "  }\n" +
    "}";

  @Test
  void testRead() {
    Jsonb jsonb = JsonbProvider.provider().create().build();

    JsonValue schemaJsonStructure = jsonb.fromJson(SCHEMA_JSON, JsonValue.class);
    Map<String, TypeDeclaration> typeDeclarations = IonSchemaReader.readTypes(schemaJsonStructure);
    List<Service> services = new ArrayList<>();
  }
}
