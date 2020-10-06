package com.github.gibmir.ion.api.schema;

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

public class ResearchTest {

  public static final String SCHEMA =
    "{\n" +
      "    \"$schema\": \"http://json-schema.org/draft-04/schema#\",\n" +
      "    \"title\": \"Product\",\n" +
      "    \"description\": \"A product from the catalog\",\n" +
      "    \"type\": \"object\",\n" +
      "    \"properties\": {\n" +
      "        \"id\": {\n" +
      "            \"description\": \"The unique identifier for a product\",\n" +
      "            \"type\": \"integer\"\n" +
      "        },\n" +
      "        \"name\": {\n" +
      "            \"description\": \"Name of the product\",\n" +
      "            \"type\": \"string\"\n" +
      "        },\n" +
      "        \"price\": {\n" +
      "            \"type\": \"number\",\n" +
      "            \"minimum\": 0,\n" +
      "            \"exclusiveMinimum\": true\n" +
      "        }\n" +
      "    },\n" +
      "    \"required\": [\"id\", \"name\", \"price\"]\n" +
      "}";

  @Test
  void smoke() {
    Schema schema = SchemaLoader.load(new JSONObject(SCHEMA));
    System.out.println(schema);
  }
}
