package com.github.gibmir.ion.lib.netty.client.common.channel.handler.response.registry;

import com.github.gibmir.ion.api.dto.properties.SerializationProperties;
import com.github.gibmir.ion.api.dto.response.transfer.error.ErrorResponse;
import com.github.gibmir.ion.api.dto.response.transfer.error.Errors;
import com.github.gibmir.ion.lib.netty.client.common.channel.handler.response.future.ResponseFuture;
import org.slf4j.Logger;

import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;
import java.util.Map;

public final class SimpleResponseListenerRegistry implements ResponseListenerRegistry {
  private final Map<String, ResponseFuture> idPerResponseListener;
  private final Logger logger;

  public SimpleResponseListenerRegistry(final Map<String, ResponseFuture> idPerResponseListener,
                                        final Logger logger) {
    this.idPerResponseListener = idPerResponseListener;
    this.logger = logger;
  }

  @Override
  public void register(final ResponseFuture responseFuture) {
    String id = responseFuture.getId();
    idPerResponseListener.compute(id, (key, future) -> computeRegistration(responseFuture, key, future));
  }

  /**
   * Registers future in listener.
   *
   * @param currentlyRegisteredFuture response future to be registered
   * @param id                        request identifier
   * @param future                    response future from
   * @return registered response
   */
  public static ResponseFuture computeRegistration(final ResponseFuture currentlyRegisteredFuture, final String id,
                                                   final ResponseFuture future) {
    if (future != null) {
      String message = "Response with id [" + id + "] already registered.";
      currentlyRegisteredFuture.completeExceptionally(new IllegalStateException(message));
      return future;
    } else {
      return currentlyRegisteredFuture;
    }
  }

  @Override
  public void notifyListenerWith(final JsonValue jsonValue) {
    try {
      switch (jsonValue.getValueType()) {
        case OBJECT:
          processRequest((JsonObject) jsonValue);
          return;
        case ARRAY:
          for (JsonValue batchJsonObject : jsonValue.asJsonArray()) {
            processRequest((JsonObject) batchJsonObject);
          }
          return;
        default:
          logger.error("Error [{}] occurred during response deserialization. ", Errors.INVALID_RPC.getError());
      }
    } catch (Exception parseException) {
      logger.error("Error [{}] occurred during response deserialization. ",
        Errors.INVALID_RPC.getError().appendMessage(parseException.getMessage()));
    }
  }

  private void processRequest(final JsonObject jsonObject) {
    JsonValue idJson = jsonObject.get(SerializationProperties.ID_KEY);
    //id null check
    if (idJson == null) {
      logger.error("Error [{}] occurred during response deserialization. Id was not present ",
        Errors.INVALID_RPC.getError());
      return;
    }
    //id type check
    switch (idJson.getValueType()) {
      case STRING:
        idPerResponseListener.compute(((JsonString) idJson).getString(),
          (responseId, responseFuture) -> computeResponse(jsonObject, responseId, responseFuture));
        break;
      case NUMBER:
        idPerResponseListener.compute(idJson.toString(),
          (responseId, responseFuture) -> computeResponse(jsonObject, responseId, responseFuture));
        break;
      default:
        logger.error("Response id [{}] has incorrect type [{}]. Must be string or number",
          idJson, idJson.getValueType());
        break;
    }
  }

  private ResponseFuture computeResponse(final JsonObject jsonObject, final String id,
                                         final ResponseFuture responseFuture) {
    if (responseFuture != null) {
      try {
        compute(jsonObject, id, responseFuture);
      } catch (Exception e) {
        responseFuture.completeExceptionally(e);
      }
    } else {
      logger.error("There is no response future listener for id [{}] ", id);
    }
    //removes from map
    return null;
  }

  private void compute(final JsonObject jsonObject, final String id, final ResponseFuture responseFuture) {
    if (jsonObject.get(SerializationProperties.PROTOCOL_KEY) == null) {
      ErrorResponse errorResponse = ErrorResponse.fromJsonRpcError(id,
        Errors.INVALID_RPC.getError().appendMessage("Protocol was not present"));
      responseFuture.completeError(errorResponse);
      return;
    }
    JsonValue resultValue = jsonObject.get(SerializationProperties.RESULT_KEY);
    if (resultValue != null) {
      responseFuture.complete(resultValue.toString());
    } else {
      JsonValue error = jsonObject.get(SerializationProperties.ERROR_KEY);
      if (error != null) {
        responseFuture.completeError(responseFuture.getResponseJsonb()
          .fromJson(jsonObject.toString(), ErrorResponse.class));
      } else {
        responseFuture.completeError(ErrorResponse.fromJsonRpcError(id,
          Errors.INVALID_RPC.getError().appendMessage("There is no body")));
      }
    }
  }
}
