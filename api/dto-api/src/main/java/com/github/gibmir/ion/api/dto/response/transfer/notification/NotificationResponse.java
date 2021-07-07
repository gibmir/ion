package com.github.gibmir.ion.api.dto.response.transfer.notification;

import com.github.gibmir.ion.api.dto.processor.JsonRpcResponseProcessor;
import com.github.gibmir.ion.api.dto.response.JsonRpcResponse;

public final class NotificationResponse implements JsonRpcResponse {
  public static final NotificationResponse INSTANCE = new NotificationResponse();

  @Override
  public void processWith(final JsonRpcResponseProcessor jsonRpcResponseProcessor) {
    jsonRpcResponseProcessor.process(this);
  }
}
