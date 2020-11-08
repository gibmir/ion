package com.github.gibmir.ion.maven.plugin.reader;

import com.github.gibmir.ion.api.schema.Schema;
import com.github.gibmir.ion.api.schema.service.Service;
import com.github.gibmir.ion.api.schema.type.TypeDeclaration;
import com.github.gibmir.ion.lib.schema.SchemaBean;
import com.github.gibmir.ion.lib.schema.service.ServiceBean;
import org.junit.jupiter.api.Test;

import javax.json.JsonObject;
import javax.json.JsonStructure;
import javax.json.JsonValue;
import javax.json.bind.Jsonb;
import javax.json.bind.spi.JsonbProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    JsonValue schemaJsonStructure = jsonb.fromJson(SCHEMA_JSON, JsonValue.class);
    Map<String, TypeDeclaration> typeDeclarations = IonSchemaReader.readTypes(schemaJsonStructure);
    List<Service> services = new ArrayList<>();
  }
}
