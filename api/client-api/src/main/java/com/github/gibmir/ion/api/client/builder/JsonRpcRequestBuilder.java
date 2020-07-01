package com.github.gibmir.ion.api.client.builder;

import javax.json.bind.Jsonb;
import java.nio.charset.Charset;
import java.time.Duration;

public interface JsonRpcRequestBuilder<R> {

  JsonRpcRequestBuilder<R> charset(Charset charset);

  JsonRpcRequestBuilder<R> timeout(Duration timeout);

  JsonRpcRequestBuilder<R> jsonb(Jsonb jsonb);

  Charset charset();

  Duration timeout();

  Jsonb jsonb();
}
