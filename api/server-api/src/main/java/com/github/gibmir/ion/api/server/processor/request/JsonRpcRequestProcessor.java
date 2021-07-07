package com.github.gibmir.ion.api.server.processor.request;

import com.github.gibmir.ion.api.dto.response.JsonRpcResponse;

/**
 * Encapsulates user request processor.
 */
public interface JsonRpcRequestProcessor {

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
