package com.github.gibmir.ion.api.server.serialization;

import com.github.gibmir.ion.api.dto.method.signature.Signature;
import com.github.gibmir.ion.api.dto.properties.SerializationProperties;
import com.github.gibmir.ion.api.dto.request.JsonRpcRequest;
import com.github.gibmir.ion.api.dto.request.transfer.RequestDto;
import com.github.gibmir.ion.api.server.cache.signature.SignatureRegistry;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.json.bind.Jsonb;

public class SerializationUtils {
  private SerializationUtils() {
  }

  public static JsonRpcRequest extractRequestFrom(JsonObject object, SignatureRegistry methodSignature, Jsonb jsonb) {
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
      throw new UnsupportedOperationException("Method " + methodValue + " is not supported");
    }
    Object[] arguments = extractArguments(object, jsonb, signature);
    return RequestDto.positional(id, procedureName, arguments);
  }

  private static Object[] extractArguments(JsonObject object, Jsonb jsonb, Signature signature) {
    JsonValue paramsValue = object.get(SerializationProperties.PARAMS_KEY);
    Class<?>[] argumentTypes = signature.getArgumentTypes();
    int length = argumentTypes.length;
    Object[] arguments = new Object[length];
    if (paramsValue instanceof JsonArray) {
      JsonArray jsonValues = paramsValue.asJsonArray();
      for (int i = 0; i < length; i++) {
        arguments[i] = jsonb.fromJson(jsonValues.get(i).toString(), argumentTypes[i]);
      }
    } else {
      arguments[0] = jsonb.fromJson(paramsValue.toString(), signature.getHead());
    }
    return arguments;
  }
}
