package com.github.gibmir.ion.maven.plugin.reader;

import com.github.gibmir.ion.api.schema.procedure.Procedure;
import com.github.gibmir.ion.api.schema.type.PropertyType;
import com.github.gibmir.ion.api.schema.type.TypeDeclaration;
import com.github.gibmir.ion.api.schema.type.TypeParameter;
import com.github.gibmir.ion.lib.schema.procedure.ProcedureBean;
import com.github.gibmir.ion.lib.schema.type.PropertyTypeBean;
import com.github.gibmir.ion.lib.schema.type.TypeDeclarationBean;
import com.github.gibmir.ion.lib.schema.type.TypeParameterBean;

import javax.json.JsonObject;
import javax.json.JsonValue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class IonSchemaReader {
  public static final String SCHEMA_TYPES_KEY = "types";
  public static final String DESCRIPTION_KEY = "description";
  public static final String DEFAULT_DESCRIPTION = "empty description";
  public static final String ID_KEY = "id";
  public static final String DEFAULT_ID = "empty id";
  public static final String PROCEDURES_KEY = "procedures";
  public static final String PROPERTIES_KEY = "properties";
  public static final String RETURN_TYPE_KEY = "return";
  public static final String ARGUMENTS_KEY = "arguments";
  public static final String TYPE_KEY = "type";
  public static final String PARAMETRIZATION_KEY = "parametrization";

  private IonSchemaReader() {
  }

  /**
   * Read types from json schema.
   *
   * @param schema ion json schema
   * @return types
   */
  public static Map<String, TypeDeclaration> readTypes(final JsonObject schema) {
    Map<String, TypeDeclaration> namePerDeclaration = new HashMap<>();
    JsonValue typesJson = schema.get(SCHEMA_TYPES_KEY);
    if (typesJson != null) {
      JsonObject typesObject = typesJson.asJsonObject();
      Set<String> typeNames = typesObject.keySet();
      for (String typeName : typeNames) {
        JsonValue typeJson = typesObject.get(typeName);
        if (typeJson != null) {
          JsonObject typeObject = typeJson.asJsonObject();
          String typeId = typeObject.getString(ID_KEY, DEFAULT_ID);
          String typeDescription = typeObject.getString(DESCRIPTION_KEY, DEFAULT_DESCRIPTION);
          Set<PropertyType> typeProperties = getTypeProperties(typeObject.get(PROPERTIES_KEY));
          Set<TypeParameter> typeParameters = getTypeParameters(typeObject.get(PARAMETRIZATION_KEY));
          TypeDeclaration typeDeclaration =
            new TypeDeclarationBean(typeId, typeName, typeDescription, typeProperties, typeParameters);
          namePerDeclaration.put(typeName, typeDeclaration);
        }
      }
    }
    return namePerDeclaration;
  }

  /**
   * Reads procedures from json ion schema.
   *
   * @param schema json ion schema
   * @return procedures
   */
  public static List<Procedure> readProcedures(final JsonObject schema) {
    JsonValue proceduresValue = schema.get(PROCEDURES_KEY);
    if (proceduresValue != null) {
      JsonObject proceduresJsonObject = proceduresValue.asJsonObject();
      Set<String> procedureNames = proceduresJsonObject.keySet();
      int proceduresCount = procedureNames.size();
      List<Procedure> procedureBeans = new ArrayList<>(proceduresCount);
      for (String procedureName : procedureNames) {
        JsonObject procedureObject = proceduresJsonObject.get(procedureName).asJsonObject();
        JsonObject returnTypeObject = procedureObject.get(RETURN_TYPE_KEY).asJsonObject();
        PropertyTypeBean returnType = new PropertyTypeBean(returnTypeObject.getString(ID_KEY, DEFAULT_ID),
          "return", returnTypeObject.getString(DESCRIPTION_KEY, DEFAULT_DESCRIPTION), returnTypeObject.getString(TYPE_KEY));
        JsonValue argumentsJson = procedureObject.get(ARGUMENTS_KEY);
        List<PropertyType> propertyTypes = getArguments(argumentsJson);
        PropertyType[] properties = new PropertyType[propertyTypes.size()];
        procedureBeans.add(new ProcedureBean(procedureObject.getString(ID_KEY, DEFAULT_ID), procedureName,
          procedureObject.getString(DESCRIPTION_KEY, DEFAULT_DESCRIPTION), propertyTypes.toArray(properties), returnType));
      }
      return procedureBeans;
    } else {
      return Collections.emptyList();
    }
  }

  private static List<PropertyType> getArguments(final JsonValue argumentsJson) {
    if (argumentsJson != null) {
      JsonObject argumentsObject = argumentsJson.asJsonObject();
      Set<String> argumentTypeNames = argumentsObject.keySet();
      int argumentsCount = argumentTypeNames.size();
      List<PropertyType> propertyTypeBeans = new ArrayList<>(argumentsCount);
      for (String argumentTypeName : argumentTypeNames) {
        JsonObject argumentObject = argumentsObject.get(argumentTypeName).asJsonObject();
        propertyTypeBeans.add(new PropertyTypeBean(argumentObject.getString(ID_KEY, DEFAULT_ID),
          argumentTypeName, argumentObject.getString(DESCRIPTION_KEY, DEFAULT_DESCRIPTION),
          argumentObject.getString(TYPE_KEY)));
      }
      return propertyTypeBeans;
    } else {
      return Collections.emptyList();
    }
  }

  /**
   * Reads type properties.
   *
   * @param typeProperties type properties json
   * @return property types
   */
  public static Set<PropertyType> getTypeProperties(final JsonValue typeProperties) {
    if (typeProperties != null) {
      JsonObject propertiesObject = typeProperties.asJsonObject();
      Set<PropertyType> propertyTypes = new HashSet<>();
      for (String propertyName : propertiesObject.keySet()) {
        JsonObject propertyObject = propertiesObject.get(propertyName).asJsonObject();
        propertyTypes.add(new PropertyTypeBean(propertyObject.getString(ID_KEY, DEFAULT_ID), propertyName,
          propertyObject.getString(DESCRIPTION_KEY, DEFAULT_DESCRIPTION), propertyObject.getString(TYPE_KEY)));
      }
      return propertyTypes;
    } else {
      return Collections.emptySet();
    }
  }

  /**
   * Reads type parameters.
   *
   * @param typeParametersJson type parameters json
   * @return type parameters
   */
  public static Set<TypeParameter> getTypeParameters(final JsonValue typeParametersJson) {
    if (typeParametersJson != null) {
      JsonObject parametersObject = typeParametersJson.asJsonObject();
      Set<TypeParameter> typeParameters = new HashSet<>();
      for (String parameterName : parametersObject.keySet()) {
        JsonObject parameterObject = parametersObject.get(parameterName).asJsonObject();
        typeParameters.add(new TypeParameterBean(parameterObject.getString(ID_KEY, DEFAULT_ID),
          parameterName, parameterObject.getString(DESCRIPTION_KEY, DEFAULT_DESCRIPTION)));
      }
      return typeParameters;
    } else {
      return Collections.emptySet();
    }
  }
}
