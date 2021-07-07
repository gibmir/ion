package com.github.gibmir.ion.api.dto.serialization;

import com.github.gibmir.ion.api.dto.properties.SerializationProperties;
import com.github.gibmir.ion.api.dto.response.JsonRpcResponse;
import com.github.gibmir.ion.api.dto.response.transfer.batch.BatchResponseDto;
import com.github.gibmir.ion.api.dto.response.transfer.error.ErrorResponse;
import com.github.gibmir.ion.api.dto.response.transfer.error.Errors;
import com.github.gibmir.ion.api.dto.response.transfer.success.SuccessResponse;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbException;
import java.util.List;

public final class SerializationUtils {

  private SerializationUtils() {
  }

  /**
   * Extract response from json structure.
   *
   * @param structure    json object representation
   * @param responseType response type
   * @param jsonb        serializer
   * @param <R>          response type
   * @return response
   */
  public static <R> JsonRpcResponse extractResponseFrom(final JsonValue structure, final Class<R> responseType,
                                                        final Jsonb jsonb) {
    try {
      switch (structure.getValueType()) {
        case OBJECT:
          return extractBatchResponse((JsonObject) structure, responseType, jsonb);
        case ARRAY:
        default:
          return ErrorResponse.withNullId(Errors.INVALID_RPC.getError());
      }
    } catch (JsonbException parseException) {
      return ErrorResponse.withNullId(parseException);
    }
  }

  /**
   * Extract response from json structure.
   *
   * @param structure     json object representation
   * @param responseTypes response types
   * @param jsonb         serializer
   * @return response
   */
  public static JsonRpcResponse extractBatchResponseFrom(final JsonValue structure, final List<Class<?>> responseTypes,
                                                         final Jsonb jsonb) {
    try {
      switch (structure.getValueType()) {
        case ARRAY:
          return extractBatchResponse((JsonArray) structure, responseTypes, jsonb);
        case OBJECT:
        default:
          return ErrorResponse.fromJsonRpcError(null, Errors.INVALID_RPC.getError());
      }
    } catch (JsonbException parseException) {
      return ErrorResponse.withNullId(parseException);
    }
  }


  private static <R> JsonRpcResponse extractBatchResponse(final JsonObject object, final Class<R> responseType,
                                                          final Jsonb jsonb) {
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

  private static JsonRpcResponse extractBatchResponse(final JsonArray array, final List<Class<?>> responseTypes,
                                                      final Jsonb jsonb) {
    try {
      int batchSize = array.size();
      JsonRpcResponse[] responses = new JsonRpcResponse[batchSize];
      for (int i = 0; i < batchSize; i++) {
        responses[i] = extractResponseFrom(array.get(i), responseTypes.get(i), jsonb);
      }
      return new BatchResponseDto(responses);
    } catch (Exception e) {
      return ErrorResponse.withNullId(e);
    }
  }
}
