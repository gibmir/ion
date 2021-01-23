package com.github.gibmir.ion.lib.netty.client.channel.handler.response.registry;

import com.github.gibmir.ion.api.dto.properties.SerializationProperties;
import com.github.gibmir.ion.api.dto.response.transfer.error.ErrorResponse;
import com.github.gibmir.ion.api.dto.response.transfer.error.Errors;
import com.github.gibmir.ion.lib.netty.client.channel.handler.response.future.ResponseFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.json.bind.Jsonb;
import java.util.Map;

public class SimpleResponseListenerRegistry implements ResponseListenerRegistry {
  private static final Logger LOGGER = LoggerFactory.getLogger(SimpleResponseListenerRegistry.class);
  private final Map<String, ResponseFuture> idPerResponseListener;

  public SimpleResponseListenerRegistry(Map<String, ResponseFuture> idPerResponseListener) {
    this.idPerResponseListener = idPerResponseListener;
  }

  @Override
  public void register(ResponseFuture responseFuture) {
    String id = responseFuture.getId();
    idPerResponseListener.compute(id, (key, completableFuture) -> {
      if (completableFuture != null) {
        String message = "Response with id [" + id + "] already registered.";
        responseFuture.completeExceptionally(new IllegalStateException(message));
        return completableFuture;
      } else {
        return responseFuture;
      }
    });
  }

  @Override
  public void notifyListenerWith(JsonValue jsonValue, Jsonb defaultJsonb) {
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
          LOGGER.error("Error [{}] occurred while deserialize response. ", Errors.INVALID_RPC.getError());
      }
    } catch (Exception parseException) {
      LOGGER.error("Error [{}] occurred while deserialize response. ", Errors.INVALID_RPC.getError()
        .appendMessage(parseException.getMessage()));
    }
  }

  private void processRequest(JsonObject jsonObject) {
    JsonValue idValue = jsonObject.get(SerializationProperties.ID_KEY);
    if (idValue == null) {
      LOGGER.error("Error [{}] occurred while deserialize response. Id was not present ",
        Errors.INVALID_RPC.getError());
      return;
    }
    String id = ((JsonString) idValue).getString();
    idPerResponseListener.compute(id,
      (key, responseFuture) -> computeResponse(jsonObject, key, responseFuture));
  }

  private ResponseFuture computeResponse(JsonObject jsonObject, String id,
                                         ResponseFuture responseFuture) {
    if (responseFuture != null) {
      try {
        compute(jsonObject, id, responseFuture);
      } catch (Exception e) {
        responseFuture.completeExceptionally(e);
      }
    } else {
      LOGGER.error("There is no response future listener for id [{}] ", id);
    }
    //removes from map
    return null;
  }

  private void compute(JsonObject jsonObject, String id, ResponseFuture responseFuture) {
    if (jsonObject.get(SerializationProperties.PROTOCOL_KEY) == null) {
      ErrorResponse.fromJsonRpcError(id, Errors.INVALID_RPC.getError().appendMessage("Protocol was not present"))
        .processWith(responseFuture);
    }
    JsonValue resultValue = jsonObject.get(SerializationProperties.RESULT_KEY);
    if (resultValue != null) {
      responseFuture.complete(resultValue);
    } else {
      JsonValue error = jsonObject.get(SerializationProperties.ERROR_KEY);
      if (error != null) {
        responseFuture.getResponseJsonb()
          .fromJson(jsonObject.toString(), ErrorResponse.class)
          .processWith(responseFuture);
      } else {
        ErrorResponse.withNullId(Errors.INVALID_RPC.getError().appendMessage("There is no body"))
          .processWith(responseFuture);
      }
    }
  }
}
