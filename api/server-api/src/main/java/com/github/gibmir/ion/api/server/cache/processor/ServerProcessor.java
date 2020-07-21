package com.github.gibmir.ion.api.server.cache.processor;

import com.github.gibmir.ion.api.dto.properties.SerializationProperties;
import com.github.gibmir.ion.api.dto.response.JsonRpcResponse;
import com.github.gibmir.ion.api.dto.response.transfer.batch.BatchResponseDto;
import com.github.gibmir.ion.api.dto.response.transfer.error.ErrorResponse;
import com.github.gibmir.ion.api.dto.response.transfer.error.Errors;
import com.github.gibmir.ion.api.dto.response.transfer.error.JsonRpcError;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.json.bind.Jsonb;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ServerProcessor {
  private final ProcedureProcessorRegistry procedureProcessorRegistry;

  public ServerProcessor(ProcedureProcessorRegistry procedureProcessorRegistry) {
    this.procedureProcessorRegistry = procedureProcessorRegistry;
  }

  public void process(JsonValue jsonStructure, Jsonb jsonb, Consumer<JsonRpcResponse> responseConsumer) {
    switch (jsonStructure.getValueType()) {
      case OBJECT:
        processObject((JsonObject) jsonStructure, jsonb, responseConsumer);
        return;
      case ARRAY:
        processBatch((JsonArray) jsonStructure, jsonb, responseConsumer);
        return;
      default:
        responseConsumer.accept(ErrorResponse.withNullId(Errors.INVALID_RPC.getError()));
    }
  }

  public void processBatch(JsonArray jsonArray, Jsonb jsonb, Consumer<JsonRpcResponse> responseConsumer) {
    int batchSize = jsonArray.size();
    List<JsonRpcResponse> jsonRpcResponseList = new ArrayList<>(batchSize);
    for (JsonValue jsonValue : jsonArray) {
      try {
        processObject((JsonObject) jsonValue, jsonb, jsonRpcResponseList::add);
      } catch (Exception e) {
        jsonRpcResponseList.add(ErrorResponse.withNullId(e));
      }
    }
    responseConsumer.accept(new BatchResponseDto(jsonRpcResponseList.toArray(new JsonRpcResponse[batchSize])));
  }

  public void processObject(JsonObject jsonObject, Jsonb jsonb, Consumer<JsonRpcResponse> responseConsumer) {
    JsonValue idValue = jsonObject.get(SerializationProperties.ID_KEY);
    if (idValue == null) {
      JsonRpcError error = Errors.INVALID_RPC.getError().appendMessage("Id was not present");
      responseConsumer.accept(ErrorResponse.withNullId(error));
      return;
    }
    String id = ((JsonString) idValue).getString();
    if (jsonObject.get(SerializationProperties.PROTOCOL_KEY) == null) {
      JsonRpcError jsonRpcError = Errors.INVALID_RPC.getError().appendMessage("Protocol was not present");
      responseConsumer.accept(ErrorResponse.fromJsonRpcError(id, jsonRpcError));
      return;
    }
    JsonValue methodValue = jsonObject.get(SerializationProperties.METHOD_KEY);
    if (methodValue == null) {
      JsonRpcError jsonRpcError = Errors.INVALID_RPC.getError().appendMessage("Method was not present");
      responseConsumer.accept(ErrorResponse.fromJsonRpcError(id, jsonRpcError));
      return;
    }
    procedureProcessorRegistry.process(id, ((JsonString) methodValue).getString(), jsonObject,
      jsonb, responseConsumer);
  }
}
