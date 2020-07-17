package com.github.gibmir.ion.api.dto.processor;

import com.github.gibmir.ion.api.dto.response.JsonRpcResponse;

import javax.json.JsonObject;
import javax.json.bind.Jsonb;
import java.util.function.Consumer;

public interface JsonRpcRequestProcessor {

  void process(String id, String procedureName, JsonObject jsonObject, Jsonb jsonb,
               Consumer<JsonRpcResponse> responseConsumer);
}
