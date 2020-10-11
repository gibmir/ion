package com.github.gibmir.ion.maven.plugin;

import com.github.gibmir.ion.api.schema.Schema;

import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.json.bind.Jsonb;

public class IonSchemaReader {
  public static final String SCHEMA_TYPES_KEY = "types";

  private final Jsonb jsonb;

  public IonSchemaReader(Jsonb jsonb) {
    this.jsonb = jsonb;
  }

  public Schema readFrom(String json) {
    JsonObject jsonObject = jsonb.fromJson(json, JsonObject.class);
    JsonValue types = jsonObject.get(SCHEMA_TYPES_KEY);
    return null;
  }

}
