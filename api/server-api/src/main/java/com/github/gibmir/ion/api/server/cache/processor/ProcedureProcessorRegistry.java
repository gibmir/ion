package com.github.gibmir.ion.api.server.cache.processor;

import com.github.gibmir.ion.api.dto.response.JsonRpcResponse;

import javax.json.JsonObject;
import javax.json.bind.Jsonb;
import java.util.function.Consumer;

public interface ProcedureProcessorRegistry {
  /**
   * Provides procedure processor by specified name.
   *
   * @param procedureName procedure name
   * @return processor
   */
  JsonRpcRequestProcessor getProcedureProcessorFor(String procedureName);

  /**
   * Process request.
   *
   * @param id               request id
   * @param procedureName    procedure name
   * @param jsonObject       request json
   * @param jsonb            serializer
   * @param responseConsumer response callback
   */
  void process(String id, String procedureName, JsonObject jsonObject, Jsonb jsonb,
               Consumer<JsonRpcResponse> responseConsumer);

  /**
   * Process notification.
   *
   * @param procedureName procedure name
   * @param jsonObject    request json
   * @param jsonb         serializer
   */
  void process(String procedureName, JsonObject jsonObject, Jsonb jsonb);

  /**
   * Registers processor with specified name.
   *
   * @param procedureName           procedure name
   * @param jsonRpcRequestProcessor request processor
   */
  void register(String procedureName, JsonRpcRequestProcessor jsonRpcRequestProcessor);

  /**
   * Unregisters processor with specified name.
   *
   * @param procedureName procedure name
   */
  void unregister(String procedureName);
}
