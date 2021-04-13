package com.github.gibmir.ion.api.server.cache.processor;

import com.github.gibmir.ion.api.dto.response.JsonRpcResponse;

import javax.json.JsonObject;
import java.util.function.Consumer;

public interface JsonRpcRequestProcessor {

  void process(String id, String procedureName, JsonObject jsonObject,
               Consumer<JsonRpcResponse> responseConsumer);

  void process(String procedureName, JsonObject jsonObject);

  JsonRpcResponse processRequest(String id, String procedureName, String argumentsJson);

  void processNotification(String procedureName, String argumentsJson);
}
