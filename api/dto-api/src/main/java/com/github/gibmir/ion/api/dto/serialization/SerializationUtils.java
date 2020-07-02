package com.github.gibmir.ion.api.dto.serialization;

import com.github.gibmir.ion.api.dto.method.signature.Signature;
import com.github.gibmir.ion.api.dto.request.JsonRpcRequest;
import com.github.gibmir.ion.api.dto.request.transfer.RequestDto;
import com.github.gibmir.ion.api.dto.response.JsonRpcResponse;
import com.github.gibmir.ion.api.dto.response.transfer.error.ErrorResponse;
import com.github.gibmir.ion.api.dto.response.transfer.error.Errors;
import com.github.gibmir.ion.api.dto.response.transfer.success.SuccessResponse;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbException;
import java.util.Map;

public class SerializationUtils {

  private SerializationUtils() {
  }

  public static final String ID_KEY = "id";
  public static final String PROTOCOL_KEY = "jsonrpc";
  public static final String METHOD_KEY = "method";
  public static final String PARAMS_KEY = "params";

  public static JsonRpcRequest extractRequestFrom(JsonObject object, Map<String, Signature> methodSignature, Jsonb jsonb) {
    if (object.get(PROTOCOL_KEY) == null) {
      throw new IllegalArgumentException("Protocol key is not present in :" + object);
    }
    JsonValue idValue = object.get(ID_KEY);
    if (idValue == null) {
      throw new IllegalArgumentException("Id key is not present in:" + object);
    }
    String id = ((JsonString) idValue).getString();
    JsonValue methodValue = object.get(METHOD_KEY);
    if (methodValue == null) {
      throw new IllegalArgumentException("Method key is not present in:" + object);
    }
    String methodName = ((JsonString) methodValue).getString();
    Signature signature = methodSignature.get(methodName);
    if (signature == null) {
      throw new UnsupportedOperationException("Method " + methodValue + " is not supported");
    }
    Object[] arguments = extractArguments(object, jsonb, signature);
    return RequestDto.positional(id, methodName, arguments);
  }

  private static Object[] extractArguments(JsonObject object, Jsonb jsonb, Signature signature) {
    JsonValue paramsValue = object.get(PARAMS_KEY);
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

  public static final String RESULT_KEY = "result";
  public static final String ERROR_KEY = "error";

  public static <R> JsonRpcResponse extractResponseFrom(JsonObject object, Class<R> responseType, Jsonb jsonb) {
    try {
      if (object.get(PROTOCOL_KEY) == null) {
        throw new IllegalArgumentException("Protocol key is not present in :" + object);
      }
      String id = object.getString(ID_KEY);
      if (id == null) {
        throw new IllegalArgumentException("Id key is not present in:" + object);
      }
      JsonValue resultValue = object.get(RESULT_KEY);
      if (resultValue == null) {
        JsonValue error = object.get(ERROR_KEY);
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
