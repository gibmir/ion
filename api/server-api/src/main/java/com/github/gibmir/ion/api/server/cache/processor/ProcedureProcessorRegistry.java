package com.github.gibmir.ion.api.server.cache.processor;

import com.github.gibmir.ion.api.dto.processor.JsonRpcRequestProcessor;
import com.github.gibmir.ion.api.dto.response.JsonRpcResponse;

import javax.json.JsonObject;
import javax.json.bind.Jsonb;
import java.util.function.Consumer;

public interface ProcedureProcessorRegistry {
  JsonRpcRequestProcessor getProcedureProcessorFor(String procedureName);

  void process(String id, String procedureName, JsonObject jsonObject, Jsonb jsonb,
               Consumer<JsonRpcResponse> responseConsumer);

  void register(String procedureName, JsonRpcRequestProcessor jsonRpcRequestProcessor);

  void unregister(String procedureName);
}
