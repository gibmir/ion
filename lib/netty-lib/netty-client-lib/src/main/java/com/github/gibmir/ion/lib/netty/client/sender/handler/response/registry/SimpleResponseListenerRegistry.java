package com.github.gibmir.ion.lib.netty.client.sender.handler.response.registry;

import com.github.gibmir.ion.api.dto.processor.JsonRpcResponseProcessor;
import com.github.gibmir.ion.api.dto.processor.exception.JsonRpcProcessingException;
import com.github.gibmir.ion.api.dto.properties.SerializationProperties;
import com.github.gibmir.ion.api.dto.response.transfer.batch.BatchResponseDto;
import com.github.gibmir.ion.api.dto.response.transfer.error.ErrorResponse;
import com.github.gibmir.ion.api.dto.response.transfer.error.Errors;
import com.github.gibmir.ion.api.dto.response.transfer.success.SuccessResponse;
import com.github.gibmir.ion.lib.netty.client.sender.handler.response.future.ResponseFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.json.bind.Jsonb;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

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
  public void notifyListenerWith(JsonValue jsonValue, Jsonb jsonb) {
    try {
      switch (jsonValue.getValueType()) {
        case OBJECT:
          processRequest((JsonObject) jsonValue, jsonb);
          return;
        case ARRAY:
          for (JsonValue batchJsonObject : jsonValue.asJsonArray()) {
            processRequest((JsonObject) batchJsonObject, jsonb);
          }
          return;
        default:
          LOGGER.error("Error [{}] occurred while deserialize response. ", Errors.INVALID_RPC.getError());
      }
    } catch (Exception parseException) {
      LOGGER.error("Error [{}] occurred while deserialize response. ", Errors.INVALID_RPC.getError());
    }
  }

  private void processRequest(JsonObject jsonObject, Jsonb jsonb) {
    JsonValue idValue = jsonObject.get(SerializationProperties.ID_KEY);
    if (idValue == null) {
      LOGGER.error("Error [{}] occurred while deserialize response. Id was not present ",
        Errors.INVALID_RPC.getError());
      return;
    }
    String id = ((JsonString) idValue).getString();
    idPerResponseListener.compute(id,
      (key, responseFuture) -> computeResponse(jsonb, jsonObject, key, responseFuture));
  }

  private ResponseFuture computeResponse(Jsonb jsonb, JsonObject jsonObject, String id,
                                         ResponseFuture responseFuture) {
    if (responseFuture != null) {
      RegistryResponseProcessor responseProcessor = new RegistryResponseProcessor(responseFuture.getFuture());
      try {
        compute(jsonb, jsonObject, id, responseFuture, responseProcessor);
      } catch (Exception e) {
        responseFuture.completeExceptionally(e);
      }
    } else {
      LOGGER.error("There is no response future listener for id [{}] ", id);
    }
    return null;
  }

  private void compute(Jsonb jsonb, JsonObject jsonObject, String id, ResponseFuture responseFuture,
                       RegistryResponseProcessor responseProcessor) {
    if (jsonObject.get(SerializationProperties.PROTOCOL_KEY) == null) {
      ErrorResponse.fromJsonRpcError(id, Errors.INVALID_RPC.getError().appendMessage("Protocol was not present"))
        .processWith(responseProcessor);
    }
    JsonValue resultValue = jsonObject.get(SerializationProperties.RESULT_KEY);
    if (resultValue != null) {
      new SuccessResponse(id, jsonb.fromJson(resultValue.toString(), responseFuture.getReturnType()))
        .processWith(responseProcessor);
    } else {
      JsonValue error = jsonObject.get(SerializationProperties.ERROR_KEY);
      if (error != null) {
        jsonb.fromJson(jsonObject.toString(), ErrorResponse.class).processWith(responseProcessor);
      } else {
        ErrorResponse.withNullId(Errors.INVALID_RPC.getError().appendMessage("There is no body"))
          .processWith(responseProcessor);
      }
    }
  }

  private static class RegistryResponseProcessor implements JsonRpcResponseProcessor {
    private final CompletableFuture<Object> resultFuture;

    private RegistryResponseProcessor(CompletableFuture<Object> resultFuture) {
      this.resultFuture = resultFuture;
    }

    @Override
    public void process(ErrorResponse errorResponse) {
      resultFuture.completeExceptionally(new JsonRpcProcessingException(errorResponse));
    }

    @Override
    public void process(SuccessResponse successResponse) {
      resultFuture.complete(successResponse.getResult());
    }

    @Override
    public void process(BatchResponseDto batchResponseDto) {

    }
  }
}
