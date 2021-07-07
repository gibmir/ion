package com.github.gibmir.ion.api.dto.response;

import com.github.gibmir.ion.api.dto.processor.JsonRpcResponseProcessor;

public interface JsonRpcResponse {
  /**
   * Process <b>this</b> response with provided processor.
   *
   * @param jsonRpcResponseProcessor processor to process response
   */
  void processWith(JsonRpcResponseProcessor jsonRpcResponseProcessor);
}
