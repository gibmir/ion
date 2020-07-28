package com.github.gibmir.ion.api.server.serialization;

import com.github.gibmir.ion.api.core.procedure.signature.Signature;
import com.github.gibmir.ion.api.dto.properties.SerializationProperties;
import com.github.gibmir.ion.api.dto.request.JsonRpcRequest;
import com.github.gibmir.ion.api.dto.request.transfer.RequestDto;
import com.github.gibmir.ion.api.server.cache.signature.SignatureRegistry;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonStructure;
import javax.json.JsonValue;
import javax.json.bind.Jsonb;
import java.lang.reflect.Type;

public class ServerSerializationUtils {

  private ServerSerializationUtils() {
  }

  public static JsonRpcRequest extractRequestFrom(JsonStructure jsonStructure, SignatureRegistry methodSignature,
                                                  Jsonb jsonb) {

    if (jsonStructure instanceof JsonArray) {
      //todo used only for testing
      throw new UnsupportedOperationException("Batch isn't implemented");
    } else {
      return extractRequest((JsonObject) jsonStructure, methodSignature, jsonb);
    }
  }

  private static JsonRpcRequest extractRequest(JsonObject object, SignatureRegistry methodSignature, Jsonb jsonb) {
    if (object.get(SerializationProperties.PROTOCOL_KEY) == null) {
      throw new IllegalArgumentException("Protocol key is not present in :" + object);
    }
    JsonValue idValue = object.get(SerializationProperties.ID_KEY);
    if (idValue == null) {
      throw new IllegalArgumentException("Id key is not present in:" + object);
    }
    String id = ((JsonString) idValue).getString();
    JsonValue methodValue = object.get(SerializationProperties.METHOD_KEY);
    if (methodValue == null) {
      throw new IllegalArgumentException("Method key is not present in:" + object);
    }
    String procedureName = ((JsonString) methodValue).getString();
    Signature signature = methodSignature.getProcedureSignatureFor(procedureName);
    if (signature == null) {
      //todo exception processing
      throw new UnsupportedOperationException("Method " + methodValue + " is unsupported. Request id " + id);
    }
    Object[] arguments = extractArguments(object, jsonb, signature);
    return RequestDto.positional(id, procedureName, arguments);
  }

  private static Object[] extractArguments(JsonObject object, Jsonb jsonb, Signature signature) {
    JsonValue paramsValue = object.get(SerializationProperties.PARAMS_KEY);
    Type[] argumentTypes = signature.getGenericTypes();
    int length = argumentTypes.length;
    Object[] arguments = new Object[length];
    if (paramsValue instanceof JsonArray) {
      JsonArray jsonValues = paramsValue.asJsonArray();
      for (int i = 0; i < length; i++) {
        arguments[i] = jsonb.fromJson(jsonValues.get(i).toString(), argumentTypes[i]);
      }
    }//todo else named request
    return arguments;
  }
}
