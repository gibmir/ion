package com.github.gibmir.ion.api.core.bind;

import com.github.gibmir.ion.api.core.request.positional.factory.PositionalRequestFactory;

public interface JsonRpcFactoryBinder {
  PositionalRequestFactory createRequestFactory();
}
