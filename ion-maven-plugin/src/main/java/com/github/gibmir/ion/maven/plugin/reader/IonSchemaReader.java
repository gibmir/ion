package com.github.gibmir.ion.maven.plugin.reader;

import com.github.gibmir.ion.api.schema.Schema;
import com.github.gibmir.ion.api.schema.type.PropertyType;
import com.github.gibmir.ion.api.schema.type.TypeDeclaration;
import com.github.gibmir.ion.lib.schema.type.PropertyTypeBean;
import com.github.gibmir.ion.lib.schema.type.TypeDeclarationBean;

import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.json.bind.Jsonb;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class IonSchemaReader {
  public static final String SCHEMA_TYPES_KEY = "types";
  public static final String DESCRIPTION_KEY = "description";
  public static final String DEFAULT_DESCRIPTION = "empty description";
  public static final String ID_KEY = "id";
  public static final String DEFAULT_ID = "empty id";
  public static final String PROPERTIES = "properties";
  public static final String NAME_KEY = "name";

  private final Jsonb jsonb;

  public IonSchemaReader(Jsonb jsonb) {
    this.jsonb = jsonb;
  }

  public Schema readFrom(String json) {
    JsonObject jsonObject = jsonb.fromJson(json, JsonObject.class);
    JsonValue types = jsonObject.get(SCHEMA_TYPES_KEY);
    return null;
  }

  public static Map<String, TypeDeclaration> readTypes(JsonValue jsonSchema) {
    Map<String, TypeDeclaration> namePerDeclaration = new HashMap<>();
    JsonObject schemaObject = jsonSchema.asJsonObject();
    JsonValue typesJson = schemaObject.get(SCHEMA_TYPES_KEY);
    if (typesJson != null) {
      JsonObject typesObject = typesJson.asJsonObject();
      Set<String> typeNames = typesObject.keySet();
      for (String typeName : typeNames) {
        JsonValue typeJson = typesObject.get(typeName);
        if (typeJson != null) {
          JsonObject typeObject = typeJson.asJsonObject();
          String typeId = typeObject.getString(ID_KEY, DEFAULT_ID);
          String typeDescription = typeObject.getString(DESCRIPTION_KEY, DEFAULT_DESCRIPTION);
          Set<PropertyType> typeProperties = getTypeProperties(typeObject.get(PROPERTIES));

          TypeDeclaration typeDeclaration =
            new TypeDeclarationBean(typeId, typeName, typeDescription, typeProperties);
          namePerDeclaration.put(typeName, typeDeclaration);
        }
      }
    }
    return namePerDeclaration;
  }

  public static Set<PropertyType> getTypeProperties(JsonValue typeProperties) {
    if (typeProperties != null) {
      JsonObject propertiesObject = typeProperties.asJsonObject();
      Set<PropertyType> propertyTypes = new HashSet<>();
      for (String propertyName : propertiesObject.keySet()) {
        JsonObject propertyObject = propertiesObject.get(propertyName).asJsonObject();
        propertyTypes.add(new PropertyTypeBean(propertyObject.getString(ID_KEY, DEFAULT_ID)
          , propertyName, propertyObject.getString(DESCRIPTION_KEY, DEFAULT_DESCRIPTION)));
      }
      return propertyTypes;
    } else {
      return Collections.emptySet();
    }
  }
}
