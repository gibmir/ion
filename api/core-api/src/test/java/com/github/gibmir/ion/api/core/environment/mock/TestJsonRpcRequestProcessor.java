package com.github.gibmir.ion.api.core.environment.mock;

import com.github.gibmir.ion.api.dto.processor.JsonRpcRequestProcessor;
import com.github.gibmir.ion.api.dto.request.transfer.positional.PositionalRequest;
import com.github.gibmir.ion.api.dto.response.JsonRpcResponse;

import java.util.Optional;

public class TestJsonRpcRequestProcessor implements JsonRpcRequestProcessor {
  private PositionalRequest positionalRequest;

  @Override
  public JsonRpcResponse process(PositionalRequest positionalRequest) {
    this.positionalRequest = positionalRequest;
    return null;
  }

  public Optional<PositionalRequest> getPositionalRequest() {
    return Optional.ofNullable(positionalRequest);
  }
}
