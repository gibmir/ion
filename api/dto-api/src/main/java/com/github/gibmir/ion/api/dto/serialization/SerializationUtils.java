package com.github.gibmir.ion.api.dto.serialization;

import com.github.gibmir.ion.api.dto.properties.SerializationProperties;
import com.github.gibmir.ion.api.dto.response.JsonRpcResponse;
import com.github.gibmir.ion.api.dto.response.transfer.error.ErrorResponse;
import com.github.gibmir.ion.api.dto.response.transfer.error.Errors;
import com.github.gibmir.ion.api.dto.response.transfer.success.SuccessResponse;

import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonStructure;
import javax.json.JsonValue;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbException;

public class SerializationUtils {

  private SerializationUtils() {
  }

  public static <R> JsonRpcResponse extractResponseFrom(JsonStructure structure, Class<R> responseType, Jsonb jsonb) {
    try {
      switch (structure.getValueType()) {
        case OBJECT:
          return extractResponse((JsonObject) structure, responseType, jsonb);
        case ARRAY:
        default:
          return ErrorResponse.fromJsonRpcError(null, Errors.INVALID_RPC.getError());
      }
    } catch (JsonbException parseException) {
      return ErrorResponse.fromJsonRpcError(null, Errors.INVALID_CHARACTER.getError());
    }
  }

  private static <R> JsonRpcResponse extractResponse(JsonObject object, Class<R> responseType, Jsonb jsonb) {
    JsonValue idValue = object.get(SerializationProperties.ID_KEY);
    if (idValue == null) {
      return ErrorResponse.withNullId(Errors.INVALID_RPC.getError());
    }
    String id = idValue.toString();

    if (object.get(SerializationProperties.PROTOCOL_KEY) == null) {
      return ErrorResponse.withNullId(Errors.INVALID_RPC.getError());
    }
    JsonValue resultValue = object.get(SerializationProperties.RESULT_KEY);
    if (resultValue == null) {
      JsonValue error = object.get(SerializationProperties.ERROR_KEY);
      if (error != null) {
        return jsonb.fromJson(object.toString(), ErrorResponse.class);
      } else {
        return ErrorResponse.withNullId(Errors.INVALID_RPC.getError());
      }
    } else {
      return new SuccessResponse(id, jsonb.fromJson(resultValue.toString(), responseType));
    }
  }

  public static String extractIdFrom(JsonStructure jsonStructure) {
    //todo jsonStructure.getValueType()
    JsonValue idValue = jsonStructure.getValue(SerializationProperties.ID_KEY);
    if (idValue == null) {
      throw new IllegalArgumentException("Id key is not present in:" + jsonStructure);
    }
    return ((JsonString) idValue).getString();
  }
}
