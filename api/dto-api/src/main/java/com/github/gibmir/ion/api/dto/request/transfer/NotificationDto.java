package com.github.gibmir.ion.api.dto.request.transfer;

import com.github.gibmir.ion.api.dto.processor.JsonRpcRequestProcessor;
import com.github.gibmir.ion.api.dto.response.JsonRpcResponse;

public class NotificationDto extends AbstractJsonRpcRequest {
  public NotificationDto() {
  }

  public NotificationDto(String procedureName, Object[] args) {
    super(procedureName, args);
  }

  @Override
  public JsonRpcResponse processWith(JsonRpcRequestProcessor jsonRpcRequestProcessor) {
    return jsonRpcRequestProcessor.process(this);
  }
}
