package com.github.gibmir.ion.api.client.request.positional.factory.provider;

import com.github.gibmir.ion.api.client.request.positional.factory.JsonRpcClientFactory;

public interface JsonRpcRequestFactoryProvider {
  JsonRpcClientFactory provide();
}
