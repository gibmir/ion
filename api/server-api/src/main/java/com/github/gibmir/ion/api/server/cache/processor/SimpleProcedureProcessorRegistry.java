package com.github.gibmir.ion.api.server.cache.processor;

import com.github.gibmir.ion.api.dto.response.JsonRpcResponse;
import com.github.gibmir.ion.api.dto.response.transfer.error.ErrorResponse;
import com.github.gibmir.ion.api.dto.response.transfer.error.Errors;
import com.github.gibmir.ion.api.dto.response.transfer.error.JsonRpcError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.JsonObject;
import javax.json.bind.Jsonb;
import java.util.Map;
import java.util.function.Consumer;

public class SimpleProcedureProcessorRegistry implements ProcedureProcessorRegistry {
  private static final Logger LOGGER = LoggerFactory.getLogger(SimpleProcedureProcessorRegistry.class);
  private final Map<String, JsonRpcRequestProcessor> processorMap;

  public SimpleProcedureProcessorRegistry(Map<String, JsonRpcRequestProcessor> processorMap) {
    this.processorMap = processorMap;
  }

  @Override
  public JsonRpcRequestProcessor getProcedureProcessorFor(String procedureName) {
    return processorMap.get(procedureName);
  }

  @Override
  public void process(String id, String procedureName, JsonObject jsonObject, Jsonb jsonb,
                      Consumer<JsonRpcResponse> responseConsumer) {
    processorMap.compute(procedureName, (procedure, jsonRpcRequestProcessor) -> {
      if (jsonRpcRequestProcessor != null) {
        jsonRpcRequestProcessor.process(id, procedureName, jsonObject, jsonb, responseConsumer);
      } else {
        JsonRpcError jsonRpcError = Errors.REQUEST_METHOD_NOT_FOUND.getError()
          .appendMessage("[" + procedureName + "] is unsupported");
        responseConsumer.accept(ErrorResponse.fromJsonRpcError(id, jsonRpcError));
      }
      return jsonRpcRequestProcessor;
    });
  }

  @Override
  public void process(String procedureName, JsonObject jsonObject, Jsonb jsonb) {
    processorMap.compute(procedureName, (procedure, jsonRpcRequestProcessor) -> {
      if (jsonRpcRequestProcessor != null) {
        jsonRpcRequestProcessor.process(procedureName, jsonObject, jsonb);
      } else {
        JsonRpcError jsonRpcError = Errors.REQUEST_METHOD_NOT_FOUND.getError()
          .appendMessage("[" + procedureName + "] is unsupported");
        LOGGER.error("Error [{}] occurred while processing notification", jsonRpcError);
      }
      return jsonRpcRequestProcessor;
    });
  }

  @Override
  public void register(String procedureName, JsonRpcRequestProcessor jsonRpcRequestProcessor) {
    processorMap.put(procedureName, jsonRpcRequestProcessor);
  }

  @Override
  public void unregister(String procedureName) {
    processorMap.computeIfPresent(procedureName, (k, v) -> null);
  }
}
