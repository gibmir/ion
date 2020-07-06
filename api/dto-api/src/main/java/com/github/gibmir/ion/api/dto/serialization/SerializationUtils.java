package com.github.gibmir.ion.api.dto.serialization;

import com.github.gibmir.ion.api.dto.properties.SerializationProperties;
import com.github.gibmir.ion.api.dto.response.JsonRpcResponse;
import com.github.gibmir.ion.api.dto.response.transfer.error.ErrorResponse;
import com.github.gibmir.ion.api.dto.response.transfer.error.Errors;
import com.github.gibmir.ion.api.dto.response.transfer.success.SuccessResponse;

import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbException;

public class SerializationUtils {

  private SerializationUtils() {
  }

  public static <R> JsonRpcResponse extractResponseFrom(JsonObject object, Class<R> responseType, Jsonb jsonb) {
    try {
      if (object.get(SerializationProperties.PROTOCOL_KEY) == null) {
        throw new IllegalArgumentException("Protocol key is not present in :" + object);
      }
      String id = object.getString(SerializationProperties.ID_KEY);
      if (id == null) {
        throw new IllegalArgumentException("Id key is not present in:" + object);
      }
      JsonValue resultValue = object.get(SerializationProperties.RESULT_KEY);
      if (resultValue == null) {
        JsonValue error = object.get(SerializationProperties.ERROR_KEY);
        if (error != null) {
          return jsonb.fromJson(object.toString(), ErrorResponse.class);
        } else {
          throw new IllegalArgumentException("Incorrect response");
        }
      } else {
        return new SuccessResponse(id, jsonb.fromJson(resultValue.toString(), responseType));
      }
    } catch (JsonbException parseException) {
      return ErrorResponse.withJsonRpcError(null, Errors.INVALID_CHARACTER.getError());
    }
  }
}
