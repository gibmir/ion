package com.github.gibmir.ion.api.server.cache.processor;

import com.github.gibmir.ion.api.dto.response.JsonRpcResponse;

import javax.json.JsonObject;
import java.util.function.Consumer;

/**
 * Encapsulates user request processor.
 */
public interface JsonRpcRequestProcessor {
  /**
   * Process raw request with callback.
   *
   * @param id               request id
   * @param procedureName    procedure name
   * @param jsonObject       request json
   * @param responseConsumer response callback
   */
  void process(String id, String procedureName, JsonObject jsonObject,
               Consumer<JsonRpcResponse> responseConsumer);

  /**
   * Process raw request without callback (for notifications).
   *
   * @param procedureName procedure name
   * @param jsonObject    request json
   */
  void process(String procedureName, JsonObject jsonObject);

  /**
   * Process request.
   *
   * @param id            request id
   * @param procedureName procedure name
   * @param argumentsJson arguments
   * @return response
   */
  JsonRpcResponse processRequest(String id, String procedureName, String argumentsJson);

  /**
   * Process notification.
   *
   * @param procedureName procedure name
   * @param argumentsJson arguments
   */
  void processNotification(String procedureName, String argumentsJson);
}
