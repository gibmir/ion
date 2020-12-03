package com.github.gibmir.ion.maven.plugin.reader;

import com.github.gibmir.ion.api.schema.service.Service;
import com.github.gibmir.ion.api.schema.type.PropertyType;
import com.github.gibmir.ion.api.schema.type.TypeDeclaration;
import com.github.gibmir.ion.lib.schema.service.ServiceBean;
import com.github.gibmir.ion.lib.schema.service.procedure.ProcedureBean;
import com.github.gibmir.ion.lib.schema.type.PropertyTypeBean;
import com.github.gibmir.ion.lib.schema.type.TypeDeclarationBean;

import javax.json.JsonObject;
import javax.json.JsonValue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class IonSchemaReader {
  public static final String SCHEMA_TYPES_KEY = "types";
  public static final String SCHEMA_SERVICES_KEY = "services";
  public static final String DESCRIPTION_KEY = "description";
  public static final String DEFAULT_DESCRIPTION = "empty description";
  public static final String ID_KEY = "id";
  public static final String DEFAULT_ID = "empty id";
  public static final String PROCEDURES_KEY = "procedures";
  public static final String PROPERTIES = "properties";
  public static final String RETURN_TYPE_KEY = "return";
  public static final String ARGUMENTS_KEY = "arguments";
  public static final String TYPE_KEY = "type";

  private IonSchemaReader() {
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

  public static Service[] readServices(JsonValue jsonSchema) {
    List<Service> services = new ArrayList<>();
    JsonObject schemaObject = jsonSchema.asJsonObject();
    JsonObject servicesObject = schemaObject.get(SCHEMA_SERVICES_KEY).asJsonObject();
    Set<String> serviceNames = servicesObject.keySet();
    for (String serviceName : serviceNames) {
      JsonObject serviceObject = servicesObject.get(serviceName).asJsonObject();
      String serviceId = serviceObject.getString(ID_KEY, DEFAULT_ID);
      String serviceDescription = serviceObject.getString(DESCRIPTION_KEY, DEFAULT_DESCRIPTION);
      List<ProcedureBean> procedures = getProcedures(serviceObject);
      ProcedureBean[] procedureBeans = new ProcedureBean[procedures.size()];
      services.add(new ServiceBean(serviceId, serviceName, serviceDescription, procedures.toArray(procedureBeans)));
    }
    Service[] servicesArray = new Service[services.size()];
    return services.toArray(servicesArray);
  }

  private static List<ProcedureBean> getProcedures(JsonObject serviceObject) {
    JsonValue proceduresValue = serviceObject.get(PROCEDURES_KEY);
    if (proceduresValue != null) {
      JsonObject proceduresJsonObject = proceduresValue.asJsonObject();
      Set<String> procedureNames = proceduresJsonObject.keySet();
      int proceduresCount = procedureNames.size();
      List<ProcedureBean> procedureBeans = new ArrayList<>(proceduresCount);
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

  private static List<PropertyType> getArguments(JsonValue argumentsJson) {
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

  public static Set<PropertyType> getTypeProperties(JsonValue typeProperties) {
    if (typeProperties != null) {
      JsonObject propertiesObject = typeProperties.asJsonObject();
      Set<PropertyType> propertyTypes = new HashSet<>();
      for (String propertyName : propertiesObject.keySet()) {
        JsonObject propertyObject = propertiesObject.get(propertyName).asJsonObject();
        propertyTypes.add(new PropertyTypeBean(propertyObject.getString(ID_KEY, DEFAULT_ID)
          , propertyName, propertyObject.getString(DESCRIPTION_KEY, DEFAULT_DESCRIPTION),
          propertyObject.getString(TYPE_KEY)));
      }
      return propertyTypes;
    } else {
      return Collections.emptySet();
    }
  }
}
